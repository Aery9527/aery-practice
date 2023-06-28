package org.aery.sorter.api.vo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SortData {

    private final Map<String, String> data;

    private final Map<String, Object> metadata = new HashMap<>();

    public SortData(Map<String, String> data) {
        this.data = Collections.unmodifiableMap(data);
    }

    public SortData putMetadata(String metakey, Object metadata) {
        this.metadata.put(metakey, metadata);
        return this;
    }

    public <MetaType> MetaType getMetadata(String metakey, Function<Map<String, String>, MetaType> metadataProvider) {
        MetaType metadata = (MetaType) this.metadata.get(metakey);
        if (metadata == null) {
            metadata = metadataProvider.apply(this.data);
            this.metadata.put(metakey, metadata);
        }
        return metadata;
    }

    public Optional<String> getData(String key) {
        return Optional.ofNullable(this.data.get(key));
    }

    public Map<String, String> getData() {
        return this.data;
    }

}
