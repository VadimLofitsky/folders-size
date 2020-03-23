package ru.lofitsky;

import java.io.IOException;

public class ConsoleApp {

    public static void main(String[] args) throws IOException {
        String path = "d:/javaprjs";
        MyFile root = new MyFile(path, null);

        for (FileSizeEntry entry : root.getChildren()) {
            System.out.println(entry);
        }
    }
}