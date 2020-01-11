package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.lofitsky.foldersSize.service.FilesService;
import ru.lofitsky.foldersSize.util.MyFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WebViewController {

    @Autowired
    private FilesService filesService;

    @GetMapping("/")
    String index(Model model, @RequestHeader(required = false, name = "folders-size-path") String path) throws UnsupportedEncodingException {

//        System.out.println("GET request with: " + path);
        if (path == null) {
            path = "";
        }
        String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        path = filesService.validatePathArgument(decodedPath);

        model.addAttribute("path", path);
        model.addAttribute("pathSize", MyFile.getPrettyPrintedSize(filesService.getRoot().getSizeCached()));

        model.addAttribute("filesList", filesService.getFilesList(path));

        return "index";
    }
}
