package ru.lofitsky.foldersSize.service.filesService;

import org.springframework.stereotype.Service;
import ru.lofitsky.foldersSize.myFile.FileSizeEntry;
import ru.lofitsky.foldersSize.myFile.FileSystemRootElement;
import ru.lofitsky.foldersSize.myFile.MyFile;

import java.io.File;
import java.nio.file.FileSystems;

@Service
public class FilesService {

    private static final MyFile root = FileSystemRootElement.getInstance();

    private final RequestsCache cache = RequestsCache.getInstance();

    public static final String pathSeparator = FileSystems.getDefault().getSeparator();

//    private MyFile currentFile;

    public MyFile getRoot() {
        return root;
    }

    public String getFilesHtmlText(String path) {

        final String lineSeparator = "<br>";

        StringBuilder files = new StringBuilder();
        files.append("<b>Path: ").append(path).append("</b>").append(lineSeparator);
        for(FileSizeEntry entry : root.getChildren()) {
            files.append(entry).append(lineSeparator);
        }

        return files.toString();
    }

    public MyFile getFile(String path, boolean calculateSize) {
        if(calculateSize)
            return getCalculatedFile(path);

        if((path == null) || "".equals(path) || root.getPath().equals(path)) {
            return root;
        }

        if(path.contains("..")) {
            path = new File(path.replace("..", "")).getParent();
        }

        return new MyFile(path);
    }

    private MyFile getCalculatedFile(String path) {
        return cache.get(path);
    }
}