package org.aery.sorter.api.impl.provider;

import org.aery.sorter.api.DataProvider;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.constant.PropertiesKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@ConfigurationProperties(prefix = PropertiesKeys.Provider.CSV_FILE)
public class CsvFileProvider implements DataProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Provider#CSV_FILE}
     */
    private String location;

    @PostConstruct
    public void initial() {
        int titleLength = 8;
        this.logger.info(this.formatter.format(titleLength, "location", this.location));
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
}
