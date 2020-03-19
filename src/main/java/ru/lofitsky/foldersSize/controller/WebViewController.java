package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.lofitsky.foldersSize.myFile.FileSizeEntry;
import ru.lofitsky.foldersSize.myFile.MyFile;
import ru.lofitsky.foldersSize.service.filesService.FilesService;
import ru.lofitsky.foldersSize.util.ExecutionPerformance;
import ru.lofitsky.foldersSize.util.PrettyPrint;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WebViewController {

    @Autowired
    private FilesService filesService;

    private ExecutionPerformance timer = ExecutionPerformance.getInstance();

    @GetMapping("/")
    String index(Model model,
                 @RequestHeader(required = false, defaultValue = "/", name = "folders-size-path") String path,
                 @RequestHeader(required = false, name = "folders-size-calculate-size") boolean calculate
    ) throws UnsupportedEncodingException {

        path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());

        String timerName = "mainPerformance";
        timer.start(timerName);
        MyFile file = filesService.getFile(path, calculate);
        String pathPrettyPrintedSize = MyFile.getPrettyPrintedSize(file.getSize());
        FileSizeEntry[] filesList = file.getChildren();
        timer.stop(timerName);

        PrettyPrint printer = new PrettyPrint("ns", 1000L, 6);
        String duration = printer.print(timer.duration(timerName));

        model.addAttribute("path", path);
        model.addAttribute("pathSize", pathPrettyPrintedSize);
        model.addAttribute("duration", duration);
        model.addAttribute("filesList", filesList);

        return "index";
    }
}