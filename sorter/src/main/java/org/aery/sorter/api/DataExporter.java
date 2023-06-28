package org.aery.sorter.api;

import org.aery.sorter.api.vo.SortData;

public interface DataExporter extends DataIO {

    void accept(SortData data);

}
