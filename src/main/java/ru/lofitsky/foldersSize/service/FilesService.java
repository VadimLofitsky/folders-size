package ru.lofitsky.foldersSize.service;

import org.springframework.stereotype.Service;
import ru.lofitsky.foldersSize.MyFile.FileSystemRootElement;
import ru.lofitsky.foldersSize.MyFile.MyFile;

import java.nio.file.FileSystems;

@Service
public class FilesService {
    private static final MyFile root = FileSystemRootElement.getRootInstance();
    private final RequestsCache cache = RequestsCache.getInstance();
    public static final String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();

    public MyFile getRoot() {
        return root;
    }

    public MyFile getMyFile(String path, boolean calculateSize) {
        if((path == null) || "".equals(path) || root.getPath().equals(path)) {
            if(calculateSize) {
                root.calculateSize();
            }

            return root;
        }

        MyFile myFile = getMyFileFromCache(path, calculateSize);
        if(myFile == null) {
            myFile = new MyFile(path);
            if(calculateSize) {
                myFile.calculateSize();
            }
        }

        return myFile;
    }

    private MyFile getMyFileFromCache(String path, boolean calculateSize) {
        return cache.get(path, calculateSize);
    }

    public String[] getChildren(String path) {
        return getMyFile(path, false).getFiles();
    }
}