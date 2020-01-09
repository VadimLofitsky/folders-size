package ru.lofitsky.foldersSize.service;

import org.springframework.stereotype.Service;
import ru.lofitsky.foldersSize.util.FileSizeEntry;
import ru.lofitsky.foldersSize.util.MyFile;

@Service
public class FilesService {

    public String getFiles(String path) {

        path = ((path == null) || path.equals("")) ? "d:\\JavaPrjs" : path;

        MyFile root_D = new MyFile(path, null);

        final String lineSeparator = "<br>";

        StringBuilder files = new StringBuilder();
        files.append("<b>Path: ").append(path).append("</b>").append(lineSeparator);
        for (FileSizeEntry entry : root_D.getChildren()) {
//            System.out.println(entry);
            files.append(entry).append(lineSeparator);
        }

        return files.toString();
    }
}
