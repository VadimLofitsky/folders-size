package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lofitsky.foldersSize.service.FilesService;

@Controller
public class WebViewController {

    @Autowired
    private FilesService filesService;

    @GetMapping("/")
    String index(Model model, @RequestParam(required = false) String path) {

        path = filesService.validatePathArgument(path);

        model.addAttribute("path", path);
        model.addAttribute("filesList", filesService.getFilesList(path));
        return "index";
    }
}
