package ru.lofitsky.foldersSize.MyFile;

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
    File thisNioFile;
    long size = -1;
    boolean isFolder;
    String path;
    MyFile[] children;
    String[] files;

    MyFile() {}

    public MyFile(String fullPath) {
        if((fullPath == null) || "".equals(fullPath)) {
            throw new NullPointerException("Trying to create MyFile object for empty path string.");
        }

        thisNioFile = new File(fullPath);

        path = fullPath;

        if(thisNioFile.getParentFile() == null) {
            parent = FileSystemRootElement.getRootInstance();
            if(fullPath.endsWith(pathSeparator) && fullPath.length()>pathSeparator.length()) {
                path = fullPath.substring(0, fullPath.length() - pathSeparator.length());
            }
        } else {
            parent = new MyFile(thisNioFile.getParent());
        }


        isFolder = thisNioFile.isDirectory();
        isTopLevel = thisNioFile.getParent() == null;

        files = getFiles();
    }
    public MyFile(String fullPath, MyFile parent) {
        this(fullPath);
        this.parent = parent;
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
                children = retrieveChildren(true);
            }

            for(MyFile child : children) {
                size += child.calculateSize();
            }
        } else {
            size = thisNioFile.length();
        }

        return size;
    }

    protected MyFile[] retrieveChildren(boolean performSizeCalculation) {
        if(!isFolder)
            return new MyFile[0];

        if(files.length == 0) {
            return new MyFile[0];
        } else {
            String thisPath = getPath() + pathSeparator;
            return Arrays.stream(files)
                    .parallel()
                    .map(path -> new MyFile(thisPath + path, this))
                    .peek(myFile -> {
                        if(performSizeCalculation)
                            myFile.calculateSize();
                    })
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
            children = retrieveChildren(false);
        }

        if(children.length == 0) {
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
        if(children == null) {
            children = retrieveChildren(false);
        }

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
        String shortName = thisNioFile.getName();

        return "".equals(shortName) ? path : shortName;
    }

    public MyFile getParent() {
        return parent;
    }

    public String getParentPath() {
        return parent.path;
    }

    public String getParentFolder() {
        return thisNioFile.getParent();
    }

    public boolean hasChild(String childName) {
        if(children == null) {
            children = retrieveChildren(false);
        }

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

    // Returns array of child files' names.
    public String[] getFiles() {
        if(!isFolder)
            return new String[0];

        String[] childFiles;
        try {
            childFiles = thisNioFile.list();
        } catch (SecurityException e) {
            System.out.println("Read access denied for " + path);
            return new String[0];
        }

        if(childFiles == null || childFiles.length == 0) {
            return new String[0];
        } else {
            return childFiles;
        }
    }

    public File getFileObject() {
        return thisNioFile;
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
                "path='" + thisNioFile.getParent() + '\'' +
                ", name='" + getShortName() + '\'' +
                (isFolder ? "]" : "") +
                ", size=" + size +
                '}';
    }
}