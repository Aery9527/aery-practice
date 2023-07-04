package org.aery.sorter.impl.exporter.table;

import org.aery.sorter.api.DataExporter;
import org.aery.sorter.api.FileTargeter;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.api.vo.SortData;
import org.aery.sorter.constant.PropertiesKeys;
import org.aery.sorter.impl.exporter.table.formatter.TableBoundaryFormatter;
import org.aery.sorter.impl.exporter.table.formatter.TableCellFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(
        name = PropertiesKeys.Exporter.USE,
        havingValue = PropertiesKeys.ExporterUse.TABLE
)
@ConfigurationProperties(prefix = PropertiesKeys.Exporter.TABLE_FILE)
public class TableFileExporter implements DataExporter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    @Autowired
    private FileTargeter fileTargeted;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#TABLE_FILE}
     */
    private String location;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#TABLE_FILE}
     */
    private List<String> sortKeys;

    private String cornerDelimiter = "+";

    private String lineDelimiter = "|";

    private String rowDelimiter = "-";

    /**
     * form {@link #sortKeys}
     */
    private List<TableCellFormatter> formatterList;

    private int[] columnMaxLength;

    private File targetFile;

    private List<SortData> dataList = new ArrayList<>();

    @PostConstruct
    @Override
    public void open() throws IOException {
        int titleLength = 8;
        this.logger.info(this.formatter.format(titleLength, "location", this.location));
        this.logger.info(this.formatter.format(titleLength, "sortKeys", this.sortKeys));

        AtomicInteger index = new AtomicInteger();
        this.formatterList = Collections.unmodifiableList(
                sortKeys.stream()
                        .map(key -> TableCellFormatter.of(index.getAndIncrement(), key.trim(), this.lineDelimiter))
                        .collect(Collectors.toList())
        );

        this.columnMaxLength = this.formatterList.stream()
                .map(TableCellFormatter::getKey)
                .mapToInt(String::length)
                .toArray();

        this.targetFile = this.fileTargeted.renew(this.location);
        this.logger.info("renew " + PropertiesKeys.ExporterUse.TABLE + " : " + location);
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.targetFile))) {
            StringBuilder sb = new StringBuilder();

            Runnable output = () -> {
//                System.out.println(sb.toString());
                try {
                    writer.write(sb.toString());
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                sb.delete(0, sb.length());
            };

            TableBoundaryFormatter boundaryFormatter = new TableBoundaryFormatter(this.cornerDelimiter, this.lineDelimiter, this.rowDelimiter, this.columnMaxLength);

            boundaryFormatter.printTitle(sb, this.formatterList);
            output.run();

            this.dataList.forEach(data -> {
                for (int index = 0; index < this.formatterList.size(); index++) {
                    TableCellFormatter formatter = this.formatterList.get(index);
                    int length = this.columnMaxLength[index];
                    formatter.output(data, length, sb);
                }

                output.run();
            });

            boundaryFormatter.printTail(sb);
            output.run();

            this.logger.info("sorted result sava in " + this.targetFile);
        }
    }

    @Override
    public void accept(SortData data) {
        refreshMaxLength(data);
        this.dataList.add(data);
    }

    private void refreshMaxLength(SortData data) {
        Map<String, String> dataMap = data.getData();
        for (int index = 0; index < this.formatterList.size(); index++) {
            TableCellFormatter formatter = this.formatterList.get(index);
            String key = formatter.getKey();
            String content = dataMap.getOrDefault(key, "");
            int length = formatter.calculateMaxLength(content, data::setMetadata);
            this.columnMaxLength[index] = Math.max(length, this.columnMaxLength[index]);
        }
    }

    //

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

    public String getCornerDelimiter() {
        return cornerDelimiter;
    }

    public void setCornerDelimiter(String cornerDelimiter) {
        this.cornerDelimiter = cornerDelimiter;
    }

    public String getLineDelimiter() {
        return lineDelimiter;
    }

    public void setLineDelimiter(String lineDelimiter) {
        this.lineDelimiter = lineDelimiter;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }
}
