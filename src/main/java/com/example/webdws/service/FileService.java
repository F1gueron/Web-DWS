package com.example.webdws.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "files");

    private Path createFilePath(long fileId, Path folder) {return folder.resolve("file-"+ fileId);}

    public void saveFile(String folderName, long fileId, MultipartFile file, String fileName) throws IOException{

        Path folder = FILES_FOLDER.resolve(folderName);

        Files.createDirectories(folder);
        String final_fileName = fileId + "_" + fileName; //
        Path newFile = folder.resolve(final_fileName);
        file.transferTo(newFile);
    }

    public void deleteFile(String folderName, long fileId) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path fileFile = createFilePath(fileId, folder);

        Files.deleteIfExists(fileFile);
    }

    public ResponseEntity<Object> createResponseFromFile(String folderName, long fileId) throws MalformedURLException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path filePath = createFilePath(fileId, folder);

        Resource file = new UrlResource(filePath.toUri());

        if(!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "file").body(file);
        }
    }
}

