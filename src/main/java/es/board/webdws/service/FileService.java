package es.board.webdws.service;

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

    private Path createFilePath(Path folder, String fileName) {
        return folder.resolve(fileName);
    }

    public void saveFile(String folderName, long fileId, MultipartFile file, String fileName) throws IOException{

        Path folder = FILES_FOLDER.resolve(folderName);

        Files.createDirectories(folder);
        String final_fileName = fileId + "_" + fileName; //
        Path newFile = folder.resolve(final_fileName);
        file.transferTo(newFile);
    }

    // not used, should we delete it?
    public void deleteFile(String folderName, String fileName) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path fileFile = createFilePath(folder, fileName);

        Files.deleteIfExists(fileFile);
    }

    public ResponseEntity<Object> createResponseFromFile(String folderName, String fileName, boolean download) throws MalformedURLException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path filePath = createFilePath(folder, fileName);

        Resource file = new UrlResource(filePath.toUri());
        if(!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        } else {
            if(download){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
            }else{
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(file);
            }
        }
    }
}

