package ru.netology.backend.servise;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.backend.dto.UserFile;
import ru.netology.backend.exception.BadRequest;
import ru.netology.backend.exception.InternalServerError;
import ru.netology.backend.model.FilesModel;
import ru.netology.backend.model.Users;
import ru.netology.backend.repository.FileRepository;
import ru.netology.backend.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileServise {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileServise(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<Void> addFile(String fileName, MultipartFile file, HttpServletRequest req) {
        try {
            if (!fileName.isEmpty()) {
                var username = (String) req.getSession().getAttribute("username");
                Optional<Users> user = userRepository.findByLogin(username);
                byte[] photoBytes = file.getBytes();
                long fileLength = file.getSize();
                FilesModel build = FilesModel.builder().name(fileName).users(user.get()).content(photoBytes).size(fileLength).build();
                fileRepository.save(build);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new BadRequest("Error input data");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("Error input data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Void> deleteFile(String filename, HttpServletRequest req) {
        try {
            if (!filename.isEmpty()) {
                var userId = (long) req.getSession().getAttribute("user-id");
                fileRepository.deleteFile(filename, userId);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new BadRequest("Error input data");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("Error input data");
        }
    }

    public ResponseEntity<Resource> downloadFile(String filename, HttpServletRequest req) throws IOException {
        try {
            if (!filename.isEmpty()) {
                final String mimeType;
                var userId = (long) req.getSession().getAttribute("user-id");
                var fileModal = fileRepository.downloadFile(filename, userId);
                File file = new File(filename);
                FileOutputStream os = new FileOutputStream(file);
                os.write(fileModal.getContent());
                os.close();
                mimeType = Files.probeContentType(file.toPath());
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName()).contentType(MediaType.valueOf(mimeType)).contentLength(file.length()) //
                        .body(resource);
            } else {
                throw new BadRequest("Error input data");
            }
        } catch (IOException e) {
            throw new InternalServerError("Error upload file");
        }
    }

    public ResponseEntity<Void> editName(String filename, String ediFilename, HttpServletRequest req) {
        try {
            if (!filename.isEmpty() || !ediFilename.isEmpty()) {
                var userId = (long) req.getSession().getAttribute("user-id");
                fileRepository.editName(filename, ediFilename, userId);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new BadRequest("Error input data");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("Error input data");
        }
    }

    public ResponseEntity<List<UserFile>> getListUserFiles(int limit, HttpServletRequest req) {
        try {
            if (limit > 0) {
                List<UserFile> arrFiles = new ArrayList<>();
                var userId = (long) req.getSession().getAttribute("user-id");
                var files = fileRepository.getList(userId, limit);
                for (FilesModel item : files) {
                    arrFiles.add(new UserFile(item.getName(), item.getSize()));
                }
                return new ResponseEntity<>(arrFiles, HttpStatus.OK);
            } else {
                throw new BadRequest("Error input data");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("Error input data");
        }
    }
}
