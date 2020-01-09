package ru.lofitsky.foldersSize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.lofitsky.foldersSize.util.FileSizeEntry;
import ru.lofitsky.foldersSize.util.MyFile;

import java.io.IOException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
//        String path_D = "d:\\Ленин в ссылке.jpg";
//        String path_D = "d:\\_Home Media Server";
        String path_D = "d:\\JavaPrjs";
        MyFile root_D = new MyFile(path_D, null);

        for (FileSizeEntry entry : root_D.getChildren()) {
            System.out.println(entry);
        }
    }
}