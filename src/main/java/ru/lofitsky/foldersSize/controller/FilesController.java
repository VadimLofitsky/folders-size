package ru.lofitsky.foldersSize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.lofitsky.foldersSize.service.FilesService;

@RestController
public class FilesController {

    @Autowired
    private FilesService service;

    @RequestMapping("")
    String index() {
        return service.getFilesHtmlText("d:\\JavaPrjs\\git\\bookstoreexample");
    }
}
