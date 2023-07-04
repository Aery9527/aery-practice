package org.aery.sorter.impl.exporter.table.formatter;

import org.aery.sorter.api.vo.SortData;
import org.aery.sorter.impl.exporter.table.TableFormatterFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class TableCellFormatter {

    public static class Prefix {

        public static final String ANCHOR = ":";

        public static final String NUMBER = ANCHOR + "%";

        public static final String KEEP_RIGHT = ANCHOR + "!";

        public static final String ADAPTIVE_LINE = ANCHOR + "|";
    }

    private static final Map<Predicate<String>, TableFormatterFactory> ALL;

    static {
        Map<Predicate<String>, TableFormatterFactory> all = new HashMap<>();
        all.put(TableNormalFormatter::match, TableNormalFormatter::new);
        all.put(TableNumberFormatter::match, TableNumberFormatter::new);
        all.put(TableKeepRightFormatter::match, TableKeepRightFormatter::new);
        all.put(TableAdaptiveLineFormatter::match, TableAdaptiveLineFormatter::new);
        ALL = Collections.unmodifiableMap(all);
    }

    public static TableCellFormatter of(int index, String rawKey, String delimiter) {
        int anchorIndex = rawKey.lastIndexOf(":");

        String key;
        Predicate<Map.Entry<Predicate<String>, TableFormatterFactory>> filter;
        if (anchorIndex < 0) {
            key = rawKey;
            filter = entry -> entry.getKey().test(null);
        } else {
            key = rawKey.substring(0, anchorIndex);
            String identifier = rawKey.substring(anchorIndex);
            filter = entry -> entry.getKey().test(identifier);
        }

        TableFormatterFactory factory = ALL.entrySet().stream()
                .filter(filter)
                .map(Map.Entry::getValue)
                .findFirst().get();
        return factory.create(index, delimiter, rawKey, key);
    }

    private final int index;

    private final String delimiter;

    private final String rawKey;

    private final String key;

    private final String identifier;

    private final String metaPrefix;

    private final Consumer<StringBuilder> startOutput;

    protected TableCellFormatter(int index, String delimiter, String rawKey, String key) {
        this.index = index;
        this.delimiter = " " + delimiter + " ";
        this.rawKey = rawKey;
        this.key = key;

        if (rawKey.equals(key)) {
            this.identifier = "";
        } else {
            this.identifier = rawKey.substring(this.key.length() + Prefix.ANCHOR.length());
        }

        long identifyCode = System.currentTimeMillis() + rawKey.hashCode() + ((int) (Math.random() * 100));
        this.metaPrefix = Long.toString(identifyCode, Character.MAX_RADIX) + ":";

        this.startOutput = index == 0 ? sb -> sb.append(this.delimiter) : sb -> {
        };
    }

    protected <MetadataType> void setMetadata(BiConsumer<String, MetadataType> metaDataSetter, String term, MetadataType metadata) {
        metaDataSetter.accept(this.metaPrefix + ":" + term, metadata);
    }

    protected <MetadataType> MetadataType getMetadata(Function<String, MetadataType> metaDataGetter, String term) {
        return metaDataGetter.apply(this.metaPrefix + ":" + term);
    }

    public int calculateMaxLength(String content, BiConsumer<String, Object> metaDataSetter) {
        return content.length();
    }

    public void output(SortData data, int needLength, StringBuilder sb) {
        this.startOutput.accept(sb);
        doOutput(data, needLength, sb);
        sb.append(this.delimiter);
    }

    protected abstract void doOutput(SortData data, int needLength, StringBuilder sb);

    public boolean isAdaptiveLineFormatter() {
        return false;
    }

    //

    public int getIndex() {
        return index;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getRawKey() {
        return rawKey;
    }

    public String getKey() {
        return key;
    }

    public String getIdentifier() {
        return identifier;
    }


}
