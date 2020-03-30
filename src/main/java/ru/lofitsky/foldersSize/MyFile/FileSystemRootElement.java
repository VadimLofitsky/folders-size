package ru.lofitsky.foldersSize.MyFile;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileSystemRootElement extends MyFile {
    private static FileSystemRootElement rootInstance;

    static {
        rootInstance=new FileSystemRootElement();
    }

    private FileSystemRootElement() {
        parent = null;
        size = -1L;
        isTopLevel = true;
        isFolder = true;

        String[] roots = retrieveRootDirs();

        if(roots.length == 1) {
            // Single root directory, like / in 'nix(nux) or single drive in Windows
            path = roots[0];
            thisNioFile = new File(path);
            files = getFiles();
        } else {
            // Multiple root directories. Use pseudonym for the root element's path
            path = MyFile.fileSystemRootElementPathPseudonym;
            thisNioFile = null;
            files = roots;
        }
    }

    private String[] retrieveRootDirs() {
        List<String> rootDirectories = new ArrayList<>();
        Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        for(Path p : roots) {
            rootDirectories.add(p.toString());
        }

        return rootDirectories.toArray(new String[0]);
    }

    public static FileSystemRootElement getRootInstance() {
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
            children = retrieveChildren(false);

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