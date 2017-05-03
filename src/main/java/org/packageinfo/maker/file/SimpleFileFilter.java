package org.packageinfo.maker.file;

import java.io.File;
import java.io.FilenameFilter;

public class SimpleFileFilter
    implements
    FilenameFilter {

    private final String name;

    public SimpleFileFilter(
        final String name) {
        super();
        this.name = name;
    }

    @Override
    public boolean accept(
        final File dir,
        final String name) {
        return name.equals(this.name);
    }
}
