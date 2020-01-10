package ru.lofitsky.foldersSize.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class MyFile {

    private static int sortOrder = SortOrder.REVERSED;
    private boolean isTopLevel;
    private MyFile parent;
    private File thisFile;
    private long size = -1;
    private boolean isFolder;
    private String path;
    private MyFile[] children;

    public MyFile(String fullPath, MyFile parent) {
        this.parent = parent;
        thisFile = new File(fullPath);
        this.path = fullPath;
        isFolder = thisFile.isDirectory();

        isTopLevel = thisFile.getParent() == null;

        children = retrieveChildren();
        size = getSize();
    }

    public static int getSortOrder() {
        return sortOrder;
    }

    public static void setSortOrder(int sortOrder) {
        MyFile.sortOrder = sortOrder;
    }

    public static void setNaturalSortOrder() {
        sortOrder = SortOrder.NATURAL;
    }

    public static void setReversedSortOrder() {
        sortOrder = SortOrder.REVERSED;
    }

    public boolean isTopLevel() {
        return isTopLevel;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public long getSizeCached() {
        if (size != -1) {
            return size;
        } else {
            return getSize();
        }
    }

    private long getSize() {
        size = 0;

        if (isFolder) {
            for (MyFile myFile : children) {
                size += myFile.getSize();
            }
        } else {
            size = thisFile.length();
        }

        return size;
    }

    private MyFile[] retrieveChildren() {
        if (!isFolder)
            return null;

        File[] childFiles;
        try {
            childFiles = thisFile.listFiles();
        } catch (SecurityException e) {
            System.out.println("Read access denied for " + path);
            return null;
        }

        if (childFiles == null || childFiles.length == 0) {
            return new MyFile[0];
        } else {
            return Arrays.stream(childFiles)
                    .map(file -> new MyFile(file.getAbsolutePath(), this))
                    .toArray(MyFile[]::new);
        }
    }

    public String getPath() {
        return path;
    }

    public String getShortName() {
        return thisFile.getName();
    }

    public MyFile getParent() {
        return parent;
    }

    public String getParentPath() {
        return thisFile.getParent();
    }

    public FileSizeEntry[] getChildren() {
        return getChildren(sortOrder);
    }

    public FileSizeEntry[] getChildren(int order) {
        MyFile self = this;
        setSortOrder(order);

        if (children == null) {
            FileSizeEntry[] emptyFileSizeEntry = {new FileSizeEntry(path, getSizeCached(), isFolder, self, getShortName())};
            return emptyFileSizeEntry;
        }

        Comparator<MyFile> comparator = Comparator.comparingLong(MyFile::getSizeCached);
        if (sortOrder == SortOrder.REVERSED) {
            comparator = comparator.reversed();
        }

        FileSizeEntry[] files = Arrays.stream(children)
                .sorted(comparator)
                .map(file -> new FileSizeEntry(file.path, file.getSizeCached(), file.isFolder, self, file.getShortName()))
                .toArray(FileSizeEntry[]::new);

        return files;
    }

    public String getParentFolder() {
        return thisFile.getParent();
    }

    public static String getPrettyPrintedSize(long abstractSize) {
        String[] prefixes = {"", "K", "M", "G", "T", "P"};

        double base = 1024.0d;
        int pow = (abstractSize != 0) ? (int) (Math.log(abstractSize) / Math.log(base)) : 0;
        double prettySize = abstractSize / Math.pow(base, pow);

        return String.format("%3.2f %sBytes", prettySize, prefixes[pow]);
    }

    @Override
    public String toString() {
        return "MyFile{" +
                (isFolder ? "[" : "") +
                "path='" + thisFile.getParent() + '\'' +
                ", name='" + getShortName() + '\'' +
                (isFolder ? "]" : "") +
                ", size=" + size +
                '}';
    }
}