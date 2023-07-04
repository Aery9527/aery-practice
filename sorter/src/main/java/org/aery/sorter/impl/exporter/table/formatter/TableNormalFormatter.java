package org.aery.sorter.impl.exporter.table.formatter;

import org.aery.sorter.api.vo.SortData;

import java.util.Objects;

public class TableNormalFormatter extends TableCellFormatter {

    public static boolean match(String identifier) {
        return Objects.isNull(identifier);
    }

    protected TableNormalFormatter(int index, String delimiter, String rawKey, String key) {
        super(index, delimiter, rawKey, key);
    }

    @Override
    protected void doOutput(SortData data, int needLength, StringBuilder sb) {
        String key = super.getKey();
        String content = data.getData(key);
        String formatted = String.format("%-" + needLength + "s", content);
        sb.append(formatted);
    }
}
