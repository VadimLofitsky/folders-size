package ru.lofitsky.foldersSize.MyFile;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRootElement extends MyFile {
    private static FileSystemRootElement rootInstance;

    static {
        rootInstance = new FileSystemRootElement();
    }

    private FileSystemRootElement() {
        parent = null;
        size = -1L;
        isTopLevel = true;
        isFolder = true;

        String[] roots = retrieveRootDirs();

        if(MyFile.IS_WINDOWS_OS) {
            // Multiple root directories. Use pseudonym for the root element's path
            path = MyFile.FILE_SYSTEM_ROOT_ELEMENT_PATH_PSEUDONYM;
            thisNioFile = null;
            files = roots;
        } else {
            // Single root directory, like / in 'nix(nux) or single drive in Windows
            path = roots[0];
            thisNioFile = new File(path);
            files = getFiles();
        }
    }

    private String[] retrieveRootDirs() {
        List<String> rootDirectories = new ArrayList<>();
        Iterable<Path> roots = FileSystems.getDefault().getRootDirectories();
        for(Path p : roots) {
            String rootPath = p.toString() + (IS_WINDOWS_OS ? "\\" : "");
            rootDirectories.add(rootPath);
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
    public String toString() {
        return "FileSystemRootElement{}";
    }
}