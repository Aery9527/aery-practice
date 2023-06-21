package org.aery.sorter.api;

import java.util.List;

public interface propertyFormatter {

    String format(int titleLength, String title, String property);

    String format(int titleLength, String title, Class<?> clazz);

    String format(int titleLength, String title, List<?> list);

}
