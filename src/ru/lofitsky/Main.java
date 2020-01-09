package ru.lofitsky;

import ru.lofitsky.foldersSize.FileSizeEntry;
import ru.lofitsky.foldersSize.MyFile;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = "/";
        MyFile root = new MyFile(path, null);

        for (FileSizeEntry entry : root.getChildren()) {
            System.out.println(entry);
        }
    }
}