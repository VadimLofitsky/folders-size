package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.lofitsky.foldersSize.MyFile.FileSizeEntry;
import ru.lofitsky.foldersSize.MyFile.MyFile;
import ru.lofitsky.foldersSize.service.FilesService;
import ru.lofitsky.foldersSize.util.ExecutionPerformance;
import ru.lofitsky.foldersSize.util.PrettyPrint;

import javax.servlet.http.HttpServletResponse;
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
                 @RequestHeader(required = false, name = "folders-size-calculate-size") boolean calculate,
                 HttpServletResponse response) throws UnsupportedEncodingException {

        path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());

        String timerName = "mainPerformance";
        timer.start(timerName);
        MyFile file = filesService.getMyFile(path, calculate);
        String pathPrettyPrintedSize = calculate ? MyFile.getPrettyPrintedSize(file.getSize()) : "";
        FileSizeEntry[] filesList = file.getChildren(calculate);
        timer.stop(timerName);

        String parentPath = file.getParent().getPath();

//        PrettyPrint printer = new PrettyPrint("ns", 1000L, 6);        // in nanoseconds
        PrettyPrint printerNanoseconds = new PrettyPrint("ns", 3);      // in nanoseconds
        PrettyPrint printerSeconds = new PrettyPrint("s", 3);      // in nanoseconds
        String duration = printerNanoseconds.print(timer.duration(timerName));      // in nanoseconds
        duration += " (" + printerSeconds.printExp(timer.duration(timerName)/1e9, 3) + ")";      // in seconds

        String isCalculated = String.valueOf(file.getSize() != -1);

        model.addAttribute("isCalculated", isCalculated);
        model.addAttribute("path", path);
        model.addAttribute("pathSeparator", filesService.pathSeparator);
        model.addAttribute("parentPath", parentPath);
        model.addAttribute("filesCount", filesList.length);
        model.addAttribute("pathSize", pathPrettyPrintedSize);
        model.addAttribute("duration", duration);
        model.addAttribute("filesList", filesList);

        response.setHeader("Pragma", "no-cache");
        response.setHeader("folders-size-isCalculated", isCalculated);
        return "index";
    }

    @PostMapping("/exit")
    void stopApp() {
        System.exit(0);
    }
}