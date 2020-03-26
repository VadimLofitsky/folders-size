package ru.lofitsky.foldersSize.MyFile;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileSystemRootElement extends MyFile {
    private static FileSystemRootElement rootInstance;

    private FileSystemRootElement() {
        isTopLevel = true;
        parent = null;
        thisFile = null;
        size = -1;
        isFolder = true;
        path = MyFile.fileSystemRootElementPathPseudonym;
    }

    private void updateChildren() {
        List<MyFile> rootDirectories = new ArrayList<>();
        for(Path p : FileSystems.getDefault().getRootDirectories()) {
            rootDirectories.add(new MyFile(p.toString(), this));
        }

        children = rootDirectories.toArray(new MyFile[0]);
    }

    public static FileSystemRootElement getRootInstance() {
        if(rootInstance == null) {
            rootInstance = new FileSystemRootElement();
            rootInstance.updateChildren();
        }

        return rootInstance;
    }

    @Override
    public MyFile getParent() {
        return rootInstance;
    }

    @Override
    public String getParentPath() {
        return path;
    }

    @Override
    public FileSizeEntry[] getChildren() {
        return getChildren(sortOrder);
    }

    @Override
    public FileSizeEntry[] getChildren(int order) {
        MyFile self = this;
        setSortOrder(order);

        if(children == null)
            updateChildren();

        if(children.length == 0) {
            FileSizeEntry[] emptyFileSizeEntry = {new FileSizeEntry(path, getSize(), isFolder, self, getShortName())};
            return emptyFileSizeEntry;
        }

        Comparator<MyFile> comparator = Comparator.comparingLong(MyFile::getSize);
        if(sortOrder == SortOrder.REVERSED) {
            comparator = comparator.reversed();
        }

        FileSizeEntry[] fileEntries = Arrays.stream(children)
                .sorted(comparator)
                .map(file -> new FileSizeEntry(file.path, file.getSize(), file.isFolder, self, file.getShortName()))
                .toArray(FileSizeEntry[]::new);

        return fileEntries;
    }

    @Override
    public String toString() {
        return "FileSystemRootElement{}";
    }
}