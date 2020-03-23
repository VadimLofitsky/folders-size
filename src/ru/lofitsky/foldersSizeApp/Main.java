package ru.lofitsky.foldersSizeApp;

import ru.lofitsky.foldersSize.FileSizeEntry;
import ru.lofitsky.foldersSize.MyFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        String path_D = "d:\\Ленин в ссылке.jpg";
//        String path_D = "d:\\_Home Media Server";
        String path_D = "d:\\JavaPrjs";
        MyFile root_D = new MyFile(path_D, null);

        for (FileSizeEntry entry : root_D.getChildren()) {
            System.out.println(entry);
        }
    }
}