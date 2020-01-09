package ru.lofitsky.foldersSize.util;

public class FileSizeEntry {
    private String path;
    private long size;
    private boolean isFolder;

    public FileSizeEntry(String path, long size, boolean isFolder) {
        this.path = path;
        this.size = size;
        this.isFolder = isFolder;
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

    @Override
    public String toString() {
        return (isFolder ? "[" : "") +
                path +
                (isFolder ? "]" : "") +
                " = " +
                size;
    }
}