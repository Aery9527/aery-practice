package org.aery.sorter.api.impl.exporter;

import org.aery.sorter.api.DataExporter;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.constant.PropertiesKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
@ConfigurationProperties(prefix = PropertiesKeys.Exporter.TABLE_FILE)
public class TableFileExporter implements DataExporter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#TABLE_FILE}
     */
    private String location;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#TABLE_FILE}
     */
    private List<String> sortKeys;

    @PostConstruct
    public void initial() {
        int titleLength = 8;
        this.logger.info(this.formatter.format(titleLength, "location", this.location));
        this.logger.info(this.formatter.format(titleLength, "sortKeys", this.sortKeys));
    }

    @Override
    public void open() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSortKeys() {
        return sortKeys;
    }

    public void setSortKeys(List<String> sortKeys) {
        this.sortKeys = sortKeys;
    }
}
