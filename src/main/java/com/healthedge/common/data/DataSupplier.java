package com.healthedge.common.data;

import com.healthedge.exceptions.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation for Data object
 *
 */
public class DataSupplier {

    private static final Logger LOG = LogManager.getLogger(DataSupplier.class);
    private final Object data;
    private final String filePath;

    DataSupplier(String filePath, String fileName, Class<? extends Data> clazz) {
        this.filePath = filePath + fileName;
        LOG.debug("Reading in YAML file at location: " + this.filePath);
        try (InputStream inputStream = new FileInputStream(this.filePath)) {
            data = new Yaml(new Constructor(clazz, new LoaderOptions())).loadAs(inputStream, clazz);
            LOG.debug("File read successful");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new DataException("Unable to load test data file, check path :" + filePath + " " + fileName);
        }
    }

    protected Object getDataObject() {
        return data;
    }

    protected String getFilePath() {
        return filePath;
    }

}
