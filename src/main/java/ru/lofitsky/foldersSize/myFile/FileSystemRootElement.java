package ru.lofitsky.foldersSize.myFile;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRootElement extends MyFile {
    private static FileSystemRootElement instance;

    private FileSystemRootElement() {
        isTopLevel = true;
        parent = null;
        thisFile = null;
        size = -1;
        isFolder = true;
        path = MyFile.fileSystemRootElementPathPseudonym;

        updateChildren();
    }

    private void updateChildren() {
        List<MyFile> rootDirectories = new ArrayList<>();
        for(Path p : FileSystems.getDefault().getRootDirectories()) {
            rootDirectories.add(new MyFile(p.toString(), this));
            System.out.println(p.toString());
        }

        rootDirectories.add(new MyFile("D:\\JavaPrjs", new MyFile("D:\\", this)));
        children = rootDirectories.toArray(new MyFile[0]);
    }

    public static FileSystemRootElement getInstance() {
        return instance == null ? instance = new FileSystemRootElement() : instance;
    }
}