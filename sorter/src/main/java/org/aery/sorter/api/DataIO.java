package org.aery.sorter.api;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;

public interface DataIO extends Closeable {

    @PostConstruct
    void open() throws IOException;

    @PreDestroy
    void close() throws IOException;

}
