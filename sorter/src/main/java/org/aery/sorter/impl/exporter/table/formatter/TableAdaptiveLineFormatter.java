package org.aery.sorter.impl.exporter.table.formatter;

import org.aery.sorter.api.vo.SortData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class TableAdaptiveLineFormatter extends TableCellFormatter {

    private static final String SPLIT_LINE = "splitLines";

    public static boolean match(String identifier) {
        return Objects.nonNull(identifier) && identifier.equals(Prefix.ADAPTIVE_LINE);
    }

    protected TableAdaptiveLineFormatter(int index, String delimiter, String rawKey, String key) {
        super(index, delimiter, rawKey, key);
    }

    @Override
    public int calculateMaxLength(String content, BiConsumer<String, Object> metaDataSetter) {
        String systemLineSeparator = System.lineSeparator();

        List<String> splitLineList = new ArrayList<>();
        int startIndex = 0;
        int lineSeparatorIndex = content.indexOf(systemLineSeparator);
        while (lineSeparatorIndex > 0) {
            String subContent = content.substring(startIndex, lineSeparatorIndex);
            splitLineList.add(subContent);
            startIndex = lineSeparatorIndex + systemLineSeparator.length();
            lineSeparatorIndex = content.indexOf(systemLineSeparator, startIndex);
        }
        splitLineList.add(content.substring(startIndex));

        super.setMetadata(metaDataSetter, SPLIT_LINE, splitLineList);

        return splitLineList.stream().mapToInt(String::length).max().getAsInt();
    }

    @Override
    protected void doOutput(SortData data, int needLength, StringBuilder sb) {
        // TODO
        String key = super.getKey();
        String content = data.getData(key);
        String formatted = String.format("%-" + needLength + "s", content);
        sb.append(formatted);
    }

    @Override
    public boolean isAdaptiveLineFormatter() {
        return true;
    }
}
