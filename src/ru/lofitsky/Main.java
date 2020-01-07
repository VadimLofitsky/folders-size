package ru.lofitsky;

import ru.lofitsky.folderssize.FileSizeEntry;
import ru.lofitsky.folderssize.MyFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        String path_D = "d:\\Ленин в ссылке.jpg";
//        String path_D = "d:\\_Home Media Server";
        String path_D = "d:\\JavaPrjs";
        MyFile root_D = new MyFile(path_D, null);

        for (FileSizeEntry entry : root_D.getChildren()) {
            String path = entry.getPath();
            boolean isFolder = entry.isFolder();
            long size= entry.getSize();

            String output = (isFolder ? "[" : "") +
                    path +
                    (isFolder ? "]" : "") +
                    " = " +
                    size;

            System.out.println(output);
        }
    }
}