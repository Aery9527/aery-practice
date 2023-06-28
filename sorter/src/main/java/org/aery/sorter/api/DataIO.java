package org.aery.sorter.api;

import java.io.Closeable;
import java.io.IOException;

public interface DataIO extends Closeable {

    void open() throws IOException;

    void close() throws IOException;

}
