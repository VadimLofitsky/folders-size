package ru.lofitsky.folderssize;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MyFile implements Comparable {

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
        children = retrieveChildren();
        size = getSize();
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

        if (!isFolder) {
            size = thisFile.length();
        } else {
            for (MyFile myFile : children) {
                size += myFile.getSize();
            }
        }

        if (children != null) {
            Arrays.sort(children, (f1, f2) -> {
                return f1.compareTo(f2);
            });
        }

        return size;
    }

    public boolean isFolder() {
        return isFolder;
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
                    .map(file -> {
                        return new MyFile(file.getAbsolutePath(), this);
                    })
                    .collect(Collectors.toList())
                    .toArray(new MyFile[0]);
        }
    }

    public String getPath() {
        return path;
    }

    public String getShortName() {
        return thisFile.getName();
    }

    public MyFile getParen() {
        return parent;
    }

    public String getParentPath() {
        return thisFile.getParent();
    }

    public FileSizeEntry[] getChildren() {
        if(children == null) {
            FileSizeEntry[] singleFileSizeEntry = { new FileSizeEntry(path, getSizeCached(), isFolder) };
            return singleFileSizeEntry;
        }

        FileSizeEntry[] files = (FileSizeEntry[]) Arrays.stream(children)
                .map(file -> {
                    return new FileSizeEntry(file.path, file.getSizeCached(), file.isFolder);
                })
                .collect(Collectors.toList())
                .toArray(new FileSizeEntry[0]);

        Arrays.sort(files, (f1, f2) -> {
            return f1.compareTo(f2);
        });
        return files;
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

    public int compareTo(Object o) {
        long thisSize = getSizeCached();
        long thatSize = ((MyFile) o).getSizeCached();
        return (int) (thisSize - thatSize);
    }
}