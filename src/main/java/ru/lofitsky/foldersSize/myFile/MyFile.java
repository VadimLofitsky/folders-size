package ru.lofitsky.foldersSize.myFile;

import ru.lofitsky.foldersSize.util.PrettyPrint;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Comparator;

public class MyFile {

    public static final String pathSeparator = FileSystems.getDefault().getSeparator();

    static final String fileSystemRootElementPathPseudonym = "/";

    static int sortOrder = SortOrder.REVERSED;

    boolean isTopLevel;
    MyFile parent;
    File thisFile;
    long size = -1;
    boolean isFolder;
    String path;
    MyFile[] children;
    String[] files;

    MyFile() {
    }

    public MyFile(String fullPath) {
        System.out.println("MyFile(String) constructor: fullPath = <" + fullPath + ">");
        thisFile = new File(fullPath);
        this.path = fullPath;
        isFolder = thisFile.isDirectory();

        isTopLevel = thisFile.getParent() == null;

        files = getFiles();

//        children = retrieveChildren();
    }

    public MyFile(String fullPath, boolean calcSize) {
        this(fullPath);

        // Not necessary "if". The calcSize argument is used only to show that calculating is needed while creation
        if(calcSize)
            calculateSize();
    }

    public MyFile(String fullPath, MyFile parent) {
        this(fullPath);

        this.parent = parent;
    }

    public MyFile(String fullPath, MyFile parent, boolean calcSize) {
        this(fullPath, parent);

        if(calcSize)    // Not necessary "if". The calcSize argument used only to overload the constructor
            calculateSize();
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

    public long getSize() {
        return size;
    }

    public long calculateSize() {
        if(size != -1) return size;

        size = 0L;

        if(isFolder) {
            if(children == null) {
                children = retrieveChildren();
            }

            for(MyFile child : children) {
                size += child.calculateSize();
            }
        } else {
            size = thisFile.length();
        }

        return size;
    }

    public String[] getFiles() {
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
            return null;
        } else {
            return Arrays.stream(childFiles)
                    .map(File::getName)
                    .toArray(String[]::new);
        }
    }

    public MyFile[] retrieveChildren() {
        if(!isFolder)
            return null;

        if(files == null || files.length == 0) {
            return new MyFile[0];
        } else {
            // It seems that MyFile[] children is needed only when calculating the size
            String thisPath = getPath() + pathSeparator;
            return Arrays.stream(files)
                    .map(path -> new MyFile(thisPath + path, this, true))
                    .toArray(MyFile[]::new);
        }
    }

    public FileSizeEntry[] getChildren() {
        return getChildren(sortOrder);
    }
    public FileSizeEntry[] getChildren(int order) {
        MyFile self = this;
        setSortOrder(order);

        if (children == null) {
            FileSizeEntry[] emptyFileSizeEntry = {new FileSizeEntry(path, getSize(), isFolder, self, getShortName())};
            return emptyFileSizeEntry;
        }

        Comparator<MyFile> comparator = Comparator.comparingLong(MyFile::getSize);
        if (sortOrder == SortOrder.REVERSED) {
            comparator = comparator.reversed();
        }

        FileSizeEntry[] fileEntries = Arrays.stream(children)
                .sorted(comparator)
                .map(file -> new FileSizeEntry(file.path, file.getSize(), file.isFolder, self, file.getShortName()))
                .toArray(FileSizeEntry[]::new);

        return fileEntries;
    }

    public MyFile getChild(String childName) {
        for(MyFile child : children) {
            if(child.getShortName().equals(childName))
                return child;
        }

        return null;
    }

    public String getPath() {
        return path;
    }

    public String getShortName() {
        String shortName = thisFile.getName();

        return "".equals(shortName) ? path : shortName;
    }

    public MyFile getParent() {
        return parent;
    }

    public String getParentPath() {
        return thisFile.getParent();
    }

    public String getParentFolder() {
        return thisFile.getParent();
    }

    public boolean hasChild(String childName) {
        for(MyFile child : children) {
            if(child.getShortName().equals(childName))
                return true;
        }

        return false;
    }

    public static String getPrettyPrintedSize(long abstractSize) {
        if(abstractSize == -1L)
            return "";

        return new PrettyPrint("Bytes", 1024L).print(abstractSize, 2);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        MyFile myFile = (MyFile) o;

        if(isFolder != myFile.isFolder) return false;
        return path.equals(myFile.path);
    }

    @Override
    public int hashCode() {
        int result = (isFolder ? 1 : 0);
        result = 31 * result + path.hashCode();
        return result;
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