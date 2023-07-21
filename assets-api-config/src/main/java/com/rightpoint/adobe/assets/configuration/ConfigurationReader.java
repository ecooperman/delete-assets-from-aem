package com.rightpoint.adobe.assets.configuration;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.rightpoint.adobe.assets.configuration.elem.FolderConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rightpoint.adobe.assets.configuration.elem.ObjectFactory;
import com.rightpoint.adobe.assets.configuration.elem.ServerConfiguration;
import com.rightpoint.adobe.assets.exceptions.AssetsException;

public class ConfigurationReader {

    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class);

    private Unmarshaller unmarshaller = null;

    public ConfigurationReader() throws AssetsException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

            unmarshaller = jaxbContext.createUnmarshaller();

        } catch (JAXBException e) {
            logger.error("Unable to create configuration files parser");
            throw new AssetsException(e);
        }
    }

    private <T> T readFile(String filePath, Class<T> clazz) throws AssetsException {
        logger.debug("Reading file from path {} and casting to class {}", filePath, clazz.getCanonicalName());
        try {
            Object obj = unmarshaller.unmarshal(new File(filePath));
            return obj != null && clazz.isInstance(obj) ? (T) obj : null;
        } catch (JAXBException e) {
            logger.error("Unable to read and parse file '{}'", filePath);
            throw new AssetsException(e);
        }
    }

    public ServerConfiguration readServerConfiguration(String filePath) throws AssetsException {
        return readFile(filePath, ServerConfiguration.class);
    }

    public FolderConfiguration readFolderConfiguration(String filePath) throws AssetsException {
        return readFile(filePath, FolderConfiguration.class);
    }

}
