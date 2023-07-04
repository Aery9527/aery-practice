package org.aery.sorter.impl.exporter;

import com.opencsv.CSVWriter;
import org.aery.sorter.api.DataExporter;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.api.vo.SortData;
import org.aery.sorter.constant.PropertiesKeys;
import org.aery.sorter.impl.io.CsvFileDataIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(
        name = PropertiesKeys.Exporter.USE,
        havingValue = PropertiesKeys.ExporterUse.CSV,
        matchIfMissing = true
)
@ConfigurationProperties(prefix = PropertiesKeys.Exporter.CSV_FILE)
public class CsvFileExporter extends CsvFileDataIO<CSVWriter> implements DataExporter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#CSV_FILE}
     */
    private String location;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Exporter#CSV_FILE}
     */
    private List<String> sortKeys;

    public CsvFileExporter() {
        super(true);
    }

    @PostConstruct
    public void showProperties() {
        int titleLength = 8;
        this.logger.info(this.formatter.format(titleLength, "location", this.location));
        this.logger.info(this.formatter.format(titleLength, "sortKeys", this.sortKeys));
    }

    @Override
    public void open() throws IOException {
        super.open();

        save(this.sortKeys.toArray(new String[0])); // write title
    }

    @Override
    public void close() throws IOException {
        super.close();

        File targetFile = super.getTargetFile();
        this.logger.info("sorted result sava in " + targetFile);
    }

    @Override
    protected CSVWriter openTarget(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        return new CSVWriter(fileWriter);
    }

    @Override
    public void accept(SortData data) {
        Map<String, String> dataMap = data.getData();
        String[] content = this.sortKeys.stream().map(dataMap::get).toArray(String[]::new);
        save(content);
    }

    protected void save(String[] content) {
        CSVWriter csvWriter = super.getTarget();
        csvWriter.writeNext(content);
    }

    @Override
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
