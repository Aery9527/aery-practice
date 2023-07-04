package org.aery.sorter.impl.provider;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.aery.sorter.api.DataProvider;
import org.aery.sorter.api.propertyFormatter;
import org.aery.sorter.api.vo.SortData;
import org.aery.sorter.constant.PropertiesKeys;
import org.aery.sorter.impl.io.CsvFileDataIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ConfigurationProperties(prefix = PropertiesKeys.Provider.CSV_FILE)
public class CsvFileProvider extends CsvFileDataIO<CSVReader> implements DataProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private propertyFormatter formatter;

    /**
     * auto bind by {@link ConfigurationProperties} with prefix {@link PropertiesKeys.Provider#CSV_FILE}
     */
    private String location;

    private List<String> titles;

    public CsvFileProvider() {
        super(false);
    }

    @PostConstruct
    public void showProperties() {
        int titleLength = 8;
        this.logger.info(this.formatter.format(titleLength, "location", this.location));
    }

    @Override
    public void open() throws IOException {
        super.open();

        CSVReader csvReader = super.getTarget();
        try {
            String[] readTitles = csvReader.readNext();
            this.titles = Stream.of(readTitles).collect(Collectors.toList());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected CSVReader openTarget(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
//        CSVReader reader = new CSVReader(fileReader);
        CSVReaderBuilder builder = new CSVReaderBuilder(fileReader);
        return builder
//                .withKeepCarriageReturn(true)
//                .withMultilineLimit(Integer.MAX_VALUE)
//                .withRowProcessor(new RowProcessor() {
//                    @Override
//                    public String processColumnItem(String column) {
//                        return column;
//                    }
//
//                    @Override
//                    public void processRow(String[] row) {
//                        for (int i = 0; i < row.length; i++) {
//
//                        }
//                    }
//                })
                .build();
    }

    @Override
    public Optional<SortData> offer() {
        CSVReader csvReader = super.getTarget();
        try {
            String[] nextLine = csvReader.readNext();
            if (nextLine == null) {
                return Optional.empty();
            }

            Map<String, String> dataMap = new HashMap<>();
            for (int i = 0; i < this.titles.size(); i++) {
                String key = this.titles.get(i);
                String value = nextLine[i];
                value = value.trim();
                dataMap.put(key, value);
            }

            return Optional.of(new SortData(dataMap));

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
