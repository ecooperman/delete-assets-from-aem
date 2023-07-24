package com.rightpoint.adobe.assets.automation;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rightpoint.adobe.assets.exceptions.AssetsException;
import com.rightpoint.adobe.assets.exceptions.ServerUnavailableException;
import com.rightpoint.adobe.assets.exceptions.UnexpectedResponseException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Engine {

    private static final Logger logger = LogManager.getLogger(Engine.class);
    public static final int MAXIMUM_NUMBER_ELEMENTS = 250000;
    public static final int MAXIMUM_DURATION = 120;

    private final Cache<String, String> assetListCache;

    private final String aemEndpoint;
    private final String accessToken;
    private final int timeout;

    private long totalAssetsProcessed = 0;
    private long movedAssets = 0;
    private long nonMatchingAssets = 0;
    private long assetError = 0;

    private static final String ASSET_CLASS = "assets/asset";
    private static final String FOLDER_CLASS = "assets/folder";

    public Engine(final String aemEndpoint, final String accessToken, final int timeout) {
        assetListCache = CacheBuilder.newBuilder()
                .maximumSize(MAXIMUM_NUMBER_ELEMENTS)
                .expireAfterWrite(MAXIMUM_DURATION, TimeUnit.MINUTES)
                .build();
        this.aemEndpoint = aemEndpoint;
        this.accessToken = accessToken;
        this.timeout = timeout;
    }

    public void retrieveAndDeleteAssets(final String pathToFolder)
            throws AssetsException {

        StopWatch stopWatch = StopWatch.createStarted();

        List<String> deletedAssets = new ArrayList<>();
        List<String> failedToDeleteAssets = new ArrayList<>();
        //logger.info("Processing results -> Total Assets: '{}', Assets moved: '{}', Non matching assets: '{}', Failed Assets: '{}'",
        //        totalAssetsProcessed, movedAssets, nonMatchingAssets, assetError);

        Map<String, String> parameters = new HashMap<>();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            logger.debug("Making AEM Assets API call with endpoint: {}", aemEndpoint);

            HttpGet request = new HttpGet(aemEndpoint + "/api/assets" + pathToFolder);

            request.addHeader(new BasicHeader("Authorization", "Bearer " + accessToken));
            //parameters.put("accessToken", accessToken);
            /*String formData = parameters.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));*/

            //httpPost.addHeader(new BasicHeader("kl-region", region));

            /*StringEntity body = new StringEntity(formData, ContentType.APPLICATION_FORM_URLENCODED);
            httpPost.setEntity(body);*/
            CloseableHttpResponse response = client.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                final String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                final JSONObject json = new JSONObject(responseString);
                logger.debug("Got response: {}", responseString);
                final JSONArray entities = json.getJSONArray("entities");
                logger.debug("Entities: {}", entities);
                for (int entityCount = 0; entityCount < entities.length(); entityCount++) {
                    final JSONObject entity = entities.getJSONObject(entityCount);
                    final JSONArray assetTypes = entity.getJSONArray("class");

                    // loop through all types and make sure at least one of them is asset/asset
                    for (int assetTypesCount = 0; assetTypesCount < assetTypes.length(); assetTypesCount++) {
                        final String assetType = assetTypes.getString(assetTypesCount);
                        // delete only if this is an asset
                        if (StringUtils.equalsAnyIgnoreCase(ASSET_CLASS, assetType)) {
                        /*if (entity.has("properties")) {
                            JSONObject properties = entity.getJSONObject("properties");
                            if (properties.has("name")) {
                                String name = properties.getString("name");
                            }
                        }*/
                            if (entity.has("links")) {
                                JSONArray links = entity.getJSONArray("links");
                                for (int linksCount = 0; linksCount < links.length(); linksCount++) {
                                    final JSONObject link = links.getJSONObject(linksCount);

                                    // we only want to grab the main asset, which has rel = self
                                    if (link.has("rel") && link.has("href")) {
                                        JSONArray rels = link.getJSONArray("rel");
                                        for (int relCount = 0; relCount < rels.length(); relCount++) {
                                            if (StringUtils.equalsAnyIgnoreCase(rels.getString(relCount), "self")) {
                                                final String href = link.getString("href");
                                                logger.debug("found an asset to delete: {}", href);

                                                // now delete the asset
                                                if (deleteAsset(href)) {
                                                    deletedAssets.add(href);
                                                } else {
                                                    failedToDeleteAssets.add(href);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            } else {
                logger.error("Failed to retrieve response with params, status code = {}, params = {}", statusCode, parameters);
            }
        } catch (Exception e) { // TODO: Refactor to catch specific exceptions rather than a generic one
            logger.error("Failed to retrieve response from AEM Assets API with params {}.  exception: {0}", parameters, e);
        }

        stopWatch.stop();

        logger.debug("Successfully deleted the following assets: \n{}", deletedAssets.stream().collect(
                Collectors.joining("\n")));
        logger.debug("\n\n\n\n");
        logger.debug("Failed to delete the following assets: \n{}", failedToDeleteAssets.stream().collect(
                Collectors.joining("\n")));

        logger.debug("Completed delete process in {} minutes", stopWatch.getTime(TimeUnit.MINUTES));
    }

    public boolean deleteAsset(final String assetApiPath)
            throws AssetsException, IOException {
        boolean success = false;
        StopWatch stopWatch = StopWatch.createStarted();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            //logger.debug("Making AEM Assets API call with endpoint: {}", aemEndpoint);

            HttpDelete request = new HttpDelete(assetApiPath);

            request.addHeader(new BasicHeader("Authorization", "Bearer " + accessToken));
            CloseableHttpResponse response = client.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                logger.debug("[Deleted] {}", assetApiPath);
                success = true;
            } else {
                logger.debug("** [Failed] {} **", assetApiPath);
            }
        }
        return success;
    }
}
