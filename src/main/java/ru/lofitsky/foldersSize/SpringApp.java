package ru.lofitsky.foldersSize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApp {
    private static String dockerMachineIP = System.getenv("docker_machine_ip");

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringApp.class, args);

        System.out.println("Docker machine IP = " + dockerMachineIP);

//        String shellCommand = getShellCommand();
        String url = "http://" + dockerMachineIP + ":8080/";

//        Process process = Runtime.getRuntime().exec(shellCommand + " " + url);
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