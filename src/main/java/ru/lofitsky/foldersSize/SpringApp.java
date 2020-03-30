package ru.lofitsky.foldersSize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringApp.class, args);

        String host = System.getenv("docker_machine_ip");
        System.out.println("Docker machine IP = " + host);

        if(host == null) {
            host = "localhost";
        }

        String shellCommand = getShellCommand();
        String url = "http://" + host + ":8080/";

        Runtime process = Runtime.getRuntime();
        if(process != null) {
            try {
                process.exec(shellCommand + " " + url);
            } catch (IOException e) {
                System.out.println(e.getMessage() + "\nOpen browser manually: " + url);
            }
        } else {
            System.out.println("Getting runtime error. Open browser manually: " + url);
        }
    }

    private static String getShellCommand() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("OS => " + os);

        if(os.indexOf("windows") >= 0) return "explorer";

        if(os.indexOf("nix") >= 0
                || os.indexOf("nux") >= 0
                || os.indexOf("aix") >= 0) return "xdg-open";

        if(os.indexOf("mac") >= 0) return "open";

        throw new Exception("Unsupported operating system");
    }
}