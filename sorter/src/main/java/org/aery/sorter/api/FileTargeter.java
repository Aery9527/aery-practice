package org.aery.sorter.api;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileTargeter {

    Optional<File> target(String location) throws IOException;

    File renew(String location) throws IOException;

}
