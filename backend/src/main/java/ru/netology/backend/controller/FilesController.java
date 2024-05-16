package ru.netology.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.backend.dto.EditFileNameDto;
import ru.netology.backend.dto.UserFile;
import ru.netology.backend.servise.FileServise;

import java.io.IOException;
import java.util.List;


@RestController
public class FilesController {
    private final FileServise fileServise;

    public FilesController(FileServise fileServise) {
        this.fileServise = fileServise;
    }

    @PostMapping("/file")
    @ResponseBody
    public ResponseEntity<Void> uploadFile(@RequestParam String filename, @RequestPart MultipartFile file, HttpServletRequest req) {
        return fileServise.addFile(filename, file, req);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam String filename, HttpServletRequest req) {
        return fileServise.deleteFile(filename, req);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename, HttpServletRequest req) throws IOException {
        return fileServise.downloadFile(filename, req);
    }

    @PutMapping("/file")
    @ResponseBody
    public ResponseEntity<Void> editFile(@RequestParam String filename, @RequestBody EditFileNameDto body, HttpServletRequest req) {
        return fileServise.editName(filename, body.getFilename(), req);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserFile>> getFiles(@RequestParam int limit, HttpServletRequest req) {
        return fileServise.getListUserFiles(limit, req);
    }
}

