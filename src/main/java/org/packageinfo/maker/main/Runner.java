package org.packageinfo.maker.main;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.packageinfo.maker.file.SimpleFileFilter;

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
                maker.mkJavaPackageInfo(new File(value), true);
            }
        } else {
            throw new IllegalArgumentException("Must pass the source folder.");
        }
    }
}

class PackageInfoMaker {

    public void mkJavaPackageInfo(
        final File file, final boolean delete) {
        FilenameFilter filter = (
            dir,
            name) -> name.equals("src");
        FilenameFilter filterII = (
            dir,
            name) -> name.equals("main") || name.equals("test");
        List<File> files = Arrays.asList(file.listFiles(filter)[0].listFiles(filterII));
        for (File dir : files) {
            File[] subFiles = dir.listFiles(new SimpleFileFilter("java"));
            if(subFiles.length != 0){
                File dir2 = subFiles[0];
                mkPackageInfo(dir2, delete);
            }
        }
    }

    private void mkPackageInfo(
        final File dir,
        final boolean delete) {
        if (dir.isDirectory()) {
            FileFilter filter = file -> file.isDirectory();
            List<File> files = Arrays.asList(dir.listFiles(filter));
            for (File file : files) {
                doMaker(dir, file, delete);
            }
        }
    }

    private void doMaker(
        final File parent,
        final File dir,
        final boolean delete) {
        if (dir.isDirectory()) {
            if (hasFile(dir)) {
                PackageInfo.createJava(parent, dir, delete);
            }
            FileFilter filter = file -> file.isDirectory();
            List<File> files = Arrays.asList(dir.listFiles(filter));
            for (File file : files) {
                doMaker(parent, file, delete);
            }
        }
    }

    private boolean hasFile(
        final File dir) {
        FileFilter filter = file -> file.isFile();
        return dir.listFiles(filter).length > 0;
    }
}

class PackageInfo {

    public static void createJava(
        final File parent,
        final File dir,
        final boolean delete) {
        try {
            File file = new File(dir, "package-info.java");
            if (file.exists() && delete) {
                file.delete();
            } else {
                System.err.println("Already exist :".concat(file.getPath()));
                return;
            }
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(getContent(parent, dir));
            writer.close();
            System.err.println("Create file :".concat(file.getPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getContent(
        final File parent,
        final File dir) {
        StringBuilder builder = new StringBuilder();
        builder.append("/*\n");
        builder.append("/*******************************************************\n");
        builder.append(" * Copyright CWI/Riachuello - All Rights Reserved\n");
        builder.append(" * \n");
        builder.append(
            " * Unauthorized copying of this file, via any medium is strictly prohibited\n");
        builder.append(" * Proprietary and confidential\n");
        builder.append(" *******************************************************\n");
        builder.append(" */\n");
        builder.append("package ".concat(
            dir.getPath().substring(parent.getPath().length() + 1).replaceAll("(\\\\)+|(/)+", "."))
            .concat(";"));
        return builder.toString();
    }
}
