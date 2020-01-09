package ru.lofitsky.foldersSize.util;

public class FileSizeEntry {
    private final String path;
    private final long size;
    private final boolean isFolder;
    private final MyFile parent;
    private final String shortName;

    public FileSizeEntry(String path, long size, boolean isFolder, MyFile parent, String shortName) {
        this.path = path;
        this.size = size;
        this.isFolder = isFolder;
        this.parent = parent;
        this.shortName = shortName;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public MyFile getParent() {
        return parent;
    }

    public String getShortName() {
        return shortName;
    }

    public float getRatioToParent() {
        return (float) size / parent.getSizeCached();
    }

    @Override
    public String toString() {
        return (isFolder ? "[" : "") +
                path +
                (isFolder ? "]" : "") +
                " = " +
                size;
    }
}