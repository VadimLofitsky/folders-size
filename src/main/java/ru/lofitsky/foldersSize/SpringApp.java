package ru.lofitsky.foldersSize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.lofitsky.foldersSize.service.OSType;

import java.io.IOException;

@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringApp.class, args);

        String host = System.getenv("DOCKER_MACHINE_IP");
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
                System.out.println("!! " + e.getMessage() + "\nOpen browser manually: " + url);
            }
        } else {
            System.out.println("!! Got runtime error. Open in browser manually: " + url);
        }
    }

    private static String getShellCommand() throws Exception {
        OSType os = OSType.getOS();
        System.out.println("OS => " + os);

        switch(os) {
            case WINDOWS:
                return "explorer";
            case NIX:
            case NUX:
            case AIX:
                return "xdg-open";
            case MAC:
                return "open";
            case UNKNOWN: throw new Exception("Unsupported operating system: " + os);
            default: return null;
        }
    }
}