package org.aery.sorter.impl.exporter.table;

import org.aery.sorter.impl.exporter.table.formatter.TableCellFormatter;

public interface TableFormatterFactory {
    TableCellFormatter create(int index, String delimiter, String rawKey, String key);
}
