package org.aery.sorter.api.impl;

import org.aery.sorter.api.DataExporter;
import org.aery.sorter.api.DataProvider;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.api.Sorter;
import org.aery.sorter.constant.PropertiesKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@ConfigurationProperties(prefix = PropertiesKeys.Sorter.DATE_PREFIX)
public class DateSorter implements Sorter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private DataExporter dataExporter;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Sorter#DATE_PREFIX}, which key is date column
     */
    private String key;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Sorter#DATE_PREFIX}, the date format
     */
    private String format;

    @PostConstruct
    public void initial() {
        int titleLength = 12;
        this.logger.info(this.formatter.format(titleLength, "provider", this.dataProvider.getClass()));
        this.logger.info(this.formatter.format(titleLength, "exporter", this.dataExporter.getClass()));
        this.logger.info(this.formatter.format(titleLength, "date-key", this.key));
        this.logger.info(this.formatter.format(titleLength, "date-format", this.format));
    }

    @Override
    public void sort() {
        try {
            this.dataProvider.open();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
