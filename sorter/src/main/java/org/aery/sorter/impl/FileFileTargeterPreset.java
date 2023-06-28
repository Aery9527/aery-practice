package org.aery.sorter.impl;

import org.aery.sorter.api.FileTargeter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Component
public class FileFileTargeterPreset implements FileTargeter {

    @Override
    public Optional<File> target(String location) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL fileURL = classLoader.getResource(location);
        return fileURL == null ? Optional.empty() : Optional.of(new File(fileURL.getFile()));
    }

    @Override
    public File renew(String location) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL fileURL = classLoader.getResource(location);

        File file;
        if (fileURL == null) {
            URL rootURL = classLoader.getResource("");
            String fileString = rootURL.getFile();
            file = new File(fileString + File.separator + location);
        } else {
            file = new File(fileURL.getFile());
            file.delete();
        }

        file.createNewFile();

        return file;
    }

}
