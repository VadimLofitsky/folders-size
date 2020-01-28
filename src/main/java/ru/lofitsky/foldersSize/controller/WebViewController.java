package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.lofitsky.foldersSize.service.FilesService;
import ru.lofitsky.foldersSize.util.ExecutionPerformance;
import ru.lofitsky.foldersSize.util.FileSizeEntry;
import ru.lofitsky.foldersSize.util.MyFile;
import ru.lofitsky.utils.PrettyPrint;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class WebViewController {

    @Autowired
    private FilesService filesService;

    private ExecutionPerformance timer = ExecutionPerformance.getInstance();

    @GetMapping("/")
    String index(Model model,
                 @RequestHeader(required = false, name = "folders-size-path") String path) throws UnsupportedEncodingException {

//        System.out.println("GET request with: " + path);
        if (path == null) {
            path = "";
        }
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        path = filesService.validatePathArgument(decodedPath);

        String timerName = "mainPerformance";
        timer.start(timerName);
        String prettyPrintedSize = MyFile.getPrettyPrintedSize(filesService.getRoot().getSizeCached());
        List<FileSizeEntry> filesList = filesService.getFilesList(path);
        timer.stop(timerName);

        PrettyPrint printer = new PrettyPrint("ns", 1000L, 6);
        String duration = printer.print(timer.duration(timerName));

        model.addAttribute("path", path);
        model.addAttribute("pathSize", prettyPrintedSize);
        model.addAttribute("filesList", filesList);
        model.addAttribute("duration", duration);

        return "index";
    }
}