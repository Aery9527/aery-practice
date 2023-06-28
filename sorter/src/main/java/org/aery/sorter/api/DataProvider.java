package org.aery.sorter.api;

import org.aery.sorter.api.vo.SortData;

import java.util.Optional;

public interface DataProvider extends DataIO {

    Optional<SortData> offer();

}
