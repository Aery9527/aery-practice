package org.aery.sorter.api.vo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SortData {

    private final Map<String, String> data;

    private final Map<String, Object> metadataMap = new HashMap<>();

    public SortData(Map<String, String> data) {
        this.data = Collections.unmodifiableMap(data);
    }

    public SortData setMetadata(String metakey, Object metadata) {
        this.metadataMap.put(metakey, metadata);
        return this;
    }

    public <MetaType> MetaType getMetadata(String metakey) {
        return (MetaType) this.metadataMap.get(metakey);
    }

    public <MetaType> MetaType getMetadata(String metakey, Function<Map<String, String>, MetaType> metadataProvider) {
        MetaType metadata = (MetaType) this.metadataMap.get(metakey);
        if (metadata == null) {
            metadata = metadataProvider.apply(this.data);
            this.metadataMap.put(metakey, metadata);
        }
        return metadata;
    }

    public String getData(String key) {
        return this.data.getOrDefault(key, "");
    }

    public Map<String, String> getData() {
        return this.data;
    }

}
