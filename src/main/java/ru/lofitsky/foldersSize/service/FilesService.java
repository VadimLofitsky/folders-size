package ru.lofitsky.foldersSize.service;

import org.springframework.stereotype.Service;
import ru.lofitsky.foldersSize.util.FileSizeEntry;
import ru.lofitsky.foldersSize.util.MyFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class FilesService {

    private MyFile root;

    public String validatePathArgument(String path) {
        if ((path == null) || path.equals("")) {
            path = "d:\\Javaprjs\\git";
        } else {
            path = path.replaceAll("/\\/g", "\\\\");

            if (path.indexOf("..") != -1) {
                path = new File(path.replace("..", "")).getParent();
            }
        }

        root = new MyFile(path, null);
        return path;
    }

    public String getFilesHtmlText(String path) {

        final String lineSeparator = "<br>";

        StringBuilder files = new StringBuilder();
        files.append("<b>Path: ").append(path).append("</b>").append(lineSeparator);
        for (FileSizeEntry entry : root.getChildren()) {
            files.append(entry).append(lineSeparator);
        }

        return files.toString();
    }

    public FileSizeEntry[] getFilesArray(String path) {
        return root.getChildren();
    }

    public List<FileSizeEntry> getFilesList(String path) {
        List<FileSizeEntry> list = Arrays.asList(root.getChildren());
        System.out.println(list);
        return list;
    }

    public MyFile getRoot() {
        return root;
    }

    public boolean isRootTopLevel() {
        return root.isTopLevel();
    }
}