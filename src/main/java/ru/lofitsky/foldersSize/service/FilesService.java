package ru.lofitsky.foldersSize.service;

import org.springframework.stereotype.Service;
import ru.lofitsky.foldersSize.util.FileSizeEntry;
import ru.lofitsky.foldersSize.util.MyFile;

import java.util.Arrays;
import java.util.List;

@Service
public class FilesService {

    public String validatePathArgument(String path) {
        return ((path == null) || path.equals("")) ? "/" : path;
    }

    public String getFilesHtmlText(String path) {
        path = validatePathArgument(path);

        MyFile root = new MyFile(path, null);

        final String lineSeparator = "<br>";

        StringBuilder files = new StringBuilder();
        files.append("<b>Path: ").append(path).append("</b>").append(lineSeparator);
        for (FileSizeEntry entry : root.getChildren()) {
            files.append(entry).append(lineSeparator);
        }

        return files.toString();
    }

    public FileSizeEntry[] getFilesArray(String path) {
        path = validatePathArgument(path);
        MyFile root = new MyFile(path, null);
        return root.getChildren();
    }

    public List<FileSizeEntry> getFilesList(String path) {
        path = validatePathArgument(path);
        MyFile root = new MyFile(path, null);
        return Arrays.asList(root.getChildren());
    }
}