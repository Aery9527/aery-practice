package org.aery.sorter.impl.exporter.table.formatter;

import org.aery.sorter.api.vo.SortData;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TableNumberFormatter extends TableCellFormatter {

    private static final String FORMATTED = "formatted";

    public static boolean match(String identifier) {
        return Objects.nonNull(identifier) && identifier.startsWith(Prefix.NUMBER) && (identifier.endsWith("d") || identifier.endsWith("f"));
    }

    private final Function<String, Number> formatter;

    protected TableNumberFormatter(int index, String delimiter, String rawKey, String key) {
        super(index, delimiter, rawKey, key);
        String formatter = super.getIdentifier();

        if (formatter.endsWith("d")) {
            this.formatter = Long::parseLong;
        } else { // formatter.endsWith("f")
            this.formatter = Double::parseDouble;
        }
    }

    @Override
    public int calculateMaxLength(String content, BiConsumer<String, Object> metaDataSetter) {
        String formatter = super.getIdentifier();
        Number number = this.formatter.apply(content);
        String formatted = String.format(formatter, number);
        super.setMetadata(metaDataSetter, FORMATTED, formatted);
        return formatted.length();
    }

    @Override
    protected void doOutput(SortData data, int needLength, StringBuilder sb) {
        String numberFormatted = super.getMetadata(data::getMetadata, FORMATTED);
        String formatted = String.format("%" + needLength + "s", numberFormatted);
        sb.append(formatted);
    }
}
