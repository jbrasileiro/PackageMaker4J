package org.packageinfo.maker.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Runner {

    static PackageInfoMaker maker = new PackageInfoMaker();

    public static void main(
        final String[] args) {
        final List<String> asList = Arrays.asList(args);
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Must pass the source folder.");
        }
        if (asList.contains("--D")) {
            String value = asList.get(asList.indexOf("--D") + 1);
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Must pass the source folder.");
            } else {
                maker.mkJavaPackageInfo(new File(value));
            }
        } else {
            throw new IllegalArgumentException("Must pass the source folder.");
        }
    }
}

class PackageInfoMaker {

    public void mkJavaPackageInfo(
        final File file) {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(
                final File dir,
                final String name) {
                return name.equals("src");
            }
        };
        FilenameFilter filterII = new FilenameFilter() {

            public boolean accept(
                final File dir,
                final String name) {
                return name.equals("main") || name.equals("test");
            }
        };
        List<File> files = Arrays.asList(file.listFiles(filter)[0].listFiles(filterII));
        for (File dir : files) {
            FilenameFilter filterIII = new FilenameFilter() {

                public boolean accept(
                    final File dir,
                    final String name) {
                    return name.equals("java");
                }
            };
            mkPackageInfo(dir.listFiles(filterIII)[0]);
        }
    }

    private void mkPackageInfo(
        final File dir) {
        if (dir.isDirectory()) {
            FileFilter filter = new FileFilter() {

                public boolean accept(
                    final File file) {
                    return file.isDirectory();
                }
            };
            List<File> files = Arrays.asList(dir.listFiles(filter));
            for (File file : files) {
                doMaker(dir, file);
            }
        }
    }

    private void doMaker(
        final File parent,
        final File dir) {
        if (dir.isDirectory()) {
            if (hasFile(dir)) {
                PackageInfo.createJava(parent, dir);
            }
            FileFilter filter = new FileFilter() {

                public boolean accept(
                    final File file) {
                    return file.isDirectory();
                }
            };
            List<File> files = Arrays.asList(dir.listFiles(filter));
            for (File file : files) {
                doMaker(parent, file);
            }
        }
    }

    private boolean hasFile(
        final File dir) {
        FileFilter filter = new FileFilter() {

            public boolean accept(
                final File file) {
                return file.isFile();
            }
        };
        return dir.listFiles(filter).length > 0;
    }
}

class PackageInfo {

    public static void createJava(
        final File parent, final File dir) {
        try {
            File file = new File(dir, "package-info.java");
            if(file.exists()){
                return;
            }
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("package ".concat(dir.getPath().substring(parent.getPath().length()+1).replaceAll("(\\\\)+|(/)+", ".")).concat(";"));
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
