package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.lofitsky.foldersSize.service.FilesService;
import ru.lofitsky.foldersSize.util.MyFile;

@Controller
public class WebViewController {

    @Autowired
    private FilesService filesService;

    @GetMapping("/")
    String index(Model model, @RequestHeader(required = false, name = "folders-size-path") String path) {

        path = filesService.validatePathArgument(path);
//        System.out.println("GET request with: " + path);

        model.addAttribute("path", path);
        model.addAttribute("pathSize", MyFile.getPrettyPrintedSize(filesService.getRoot().getSizeCached()));

        model.addAttribute("filesList", filesService.getFilesList(path));

        return "index";
    }
}
