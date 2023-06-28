package org.aery.sorter.impl.io;

import org.aery.sorter.api.DataIO;
import org.aery.sorter.api.FileTargeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class CsvFileDataIO<TargetType extends Closeable> implements DataIO {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final boolean resetFile;

    @Autowired
    private FileTargeter fileTargeted;

    private File targetFile;

    private TargetType target;

    public CsvFileDataIO(boolean resetFile) {
        this.resetFile = resetFile;
    }

    @PostConstruct
    @Override
    public void open() throws IOException {
        String location = getLocation();
        Optional<File> fileOptional = this.fileTargeted.target(location);

        boolean needResetFile = !fileOptional.isPresent() || this.resetFile;
        this.targetFile = needResetFile ? this.fileTargeted.renew(location) : fileOptional.get();
        this.logger.info((needResetFile ? "renew" : "open") + " csv : " + location);

        this.target = openTarget(this.targetFile);
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        String location = getLocation();
        this.logger.info("close csv file : " + location);

        this.target.close();
    }

    protected abstract TargetType openTarget(File file) throws IOException;

    protected File getTargetFile() {
        return this.targetFile;
    }

    protected TargetType getTarget() {
        return this.target;
    }

    public abstract String getLocation();

}
