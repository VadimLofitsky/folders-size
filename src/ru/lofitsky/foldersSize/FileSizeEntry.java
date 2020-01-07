package ru.lofitsky.foldersSize;

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
}
