package com.rightpoint.adobe.assets;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CliWrapper {

    private static final Logger LOG = LogManager.getLogger(CliWrapper.class);
    private static final String OPT_SERVER = "s";
    private static final String OPT_FOLDER = "f";
    private static final String OPT_SERVER_MSG = "file path for server configuration.";
    private static final String OPT_FOLDER_MSG = "file path for folder configuration.";
    private CommandLine cli;
    private Options options;

    CliWrapper() {
        createOptions();
    }

    boolean parse(final String[] args) {
        boolean argsParsed = true;
        CommandLineParser parser = new DefaultParser();
        try {
            cli = parser.parse(options, args);
        } catch (MissingOptionException moe) {
            LOG.error("Missing required options in command arguments", moe);
            argsParsed = false;
        } catch (ParseException e) {
            LOG.error("Error while parsing command line options", e);
            argsParsed = false;
        }
        return argsParsed;
    }

    private void createOptions() {
        // create Options object
        options = new Options();

        //Add to options object
        options.addOption(
                Option.builder(OPT_SERVER)
                        .hasArg(true)
                        .desc(OPT_SERVER_MSG)
                        .required().build())
                .addOption(
                        Option.builder(OPT_FOLDER)
                                .hasArg(true)
                                .desc(OPT_FOLDER_MSG)
                                .required().build());
    }

    String getServerFilePath() {
        return cli != null ? cli.getOptionValue(OPT_SERVER) : null;
    }

    String getFolderFilePath() {
        return cli != null ? cli.getOptionValue(OPT_FOLDER) : null;
    }

    void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar " + getJarFileName(), options);
    }

    private String getJarFileName() {
        return new java.io.File(CliWrapper.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }
}
