package org.aery.sorter.impl;

import org.aery.sorter.api.DataExporter;
import org.aery.sorter.api.DataProvider;
import org.aery.sorter.api.Sorter;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.api.vo.SortData;
import org.aery.sorter.constant.PropertiesKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

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

    private SimpleDateFormat dateTimeFormatter;

    @PostConstruct
    public void initial() {
        int titleLength = 12;
        this.logger.info(this.formatter.format(titleLength, "provider", this.dataProvider.getClass()));
        this.logger.info(this.formatter.format(titleLength, "exporter", this.dataExporter.getClass()));
        this.logger.info(this.formatter.format(titleLength, "date-key", this.key));
        this.logger.info(this.formatter.format(titleLength, "date-format", this.format));

        this.dateTimeFormatter = new SimpleDateFormat(this.format);
    }

    @Override
    public void sort() {
        TreeSet<SortData> datas = new TreeSet<>(this::compare);

        Optional<SortData> dataOptional = this.dataProvider.offer();
        while (dataOptional.isPresent()) {
            datas.add(dataOptional.get());
            dataOptional = this.dataProvider.offer();
        }

        this.logger.info("receive data " + datas.size());

        datas.forEach(this.dataExporter::accept);
    }

    public int compare(SortData data1, SortData data2) {
        String dateKey = "sortDate";
        long data1Ts = data1.getMetadata(dateKey, this::convertDate);
        long data2Ts = data2.getMetadata(dateKey, this::convertDate);

        return data1Ts < data2Ts ? -1 : 1;
    }

    public long convertDate(Map<String, String> data) {
        String dateString = data.get(this.key);

        try {
            Date date = this.dateTimeFormatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
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
