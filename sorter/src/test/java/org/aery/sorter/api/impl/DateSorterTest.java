package org.aery.sorter.api.impl;

import org.aery.sorter.Application;
import org.aery.sorter.api.Sorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
class DateSorterTest {

    @Autowired
    private Sorter sorter;

    @Test
    void sort() {
        this.sorter.sort();
    }

}
