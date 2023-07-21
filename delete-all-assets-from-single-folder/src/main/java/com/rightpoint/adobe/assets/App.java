package com.rightpoint.adobe.assets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.rightpoint.adobe.assets.configuration.elem.FolderConfiguration;
import com.rightpoint.adobe.assets.exceptions.AssetsException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rightpoint.adobe.assets.automation.Engine;
import com.rightpoint.adobe.assets.configuration.ConfigurationReader;
import com.rightpoint.adobe.assets.configuration.elem.ServerConfiguration;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            CliWrapper cli = new CliWrapper();

            if (cli.parse(args)) {
                logger.info("Starting delete assets from folder process");

                final ConfigurationReader configReader = new ConfigurationReader();

                logger.info("Reading server and folder configuration info");
                final ServerConfiguration serverConfig = configReader.readServerConfiguration(cli.getServerFilePath());
                final FolderConfiguration folderConfig = configReader.readFolderConfiguration(cli.getFolderFilePath());

                logger.debug("Server config details: \naccessToken: {}\ntimeout: {}:", serverConfig.getAccessToken(), serverConfig.getTimeout());
                logger.debug("Folder config details: \npathToFolder: {}", folderConfig.getPathToFolder());

                Engine engine = new Engine();


                logger.info("End of deletion process");

            } else {
                cli.showUsage();
            }
        } catch (AssetsException e) {
            logger.error("Unexpected error during delete assets from folder process. See error log for further information");
            logger.debug("Unexpected error", e);
        } finally {
            logger.info("Total delete assets from folder execution time: {}ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
            stopWatch.stop();
        }
    }
}
