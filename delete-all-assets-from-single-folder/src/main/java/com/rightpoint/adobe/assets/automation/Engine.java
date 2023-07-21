package com.rightpoint.adobe.assets.automation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rightpoint.adobe.assets.exceptions.AssetsException;
import com.rightpoint.adobe.assets.exceptions.ServerUnavailableException;
import com.rightpoint.adobe.assets.exceptions.UnexpectedResponseException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class Engine {

    private static final Logger logger = LogManager.getLogger(Engine.class);
    public static final int MAXIMUM_NUMBER_ELEMENTS = 250000;
    public static final int MAXIMUM_DURATION = 120;

    private final Cache<String, String> assetListCache;

    private long totalAssetsProcessed = 0;
    private long movedAssets = 0;
    private long nonMatchingAssets = 0;
    private long assetError = 0;

    public Engine() {
        assetListCache = CacheBuilder.newBuilder()
                .maximumSize(MAXIMUM_NUMBER_ELEMENTS)
                .expireAfterWrite(MAXIMUM_DURATION, TimeUnit.MINUTES)
                .build();
    }

    public void retrieveAssetList(final String folderPath)
            throws AssetsException {

        //logger.info("Processing results -> Total Assets: '{}', Assets moved: '{}', Non matching assets: '{}', Failed Assets: '{}'",
        //        totalAssetsProcessed, movedAssets, nonMatchingAssets, assetError);
    }

    private boolean deleteAsset(final String assetPath)
            throws AssetsException {
        StopWatch stopWatch = StopWatch.createStarted();

        logger.info("Deleting asset {}", assetPath);
        // add delete logic here
        return true;
    }
}
