package org.aery.sorter.impl;

import org.aery.sorter.api.propertyFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class propertyFormatterPreset implements propertyFormatter {

    @Override
    public String format(int titleLength, String title, String property) {
        return String.format("%-" + titleLength + "s : %s", title, property);
    }

    @Override
    public String format(int titleLength, String title, Class<?> clazz) {
        return String.format("%-" + titleLength + "s : %s (%s)", title, clazz.getSimpleName(), clazz);
    }

    @Override
    public String format(int titleLength, String title, List<?> list) {
        return format(titleLength, title, list.toString());
    }

}
