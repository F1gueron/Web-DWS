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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");
    private Path createFilePath(Path folder, String fileName) {
        return folder.resolve(fileName);
    }

    /*
        This function saves the image inside the server. Uses the UUID generated imageName and concatenates it with the imageId to avoid duplicates.

        Function : saveImage
        param : folderName, imageId, image, imageName
        returns : void



     */

    public void saveImage(String folderName, long imageId, MultipartFile image, String imageName) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Files.createDirectories(folder);
        String final_fileName = imageName; //
        Path newFile = folder.resolve(final_fileName);
        image.transferTo(newFile);
    }

    public void deleteImage(String folderName, String fileName) throws IOException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path imageFile = createFilePath(folder, fileName);

        Files.deleteIfExists(imageFile);
    }

    public ResponseEntity<Object> createResponseFromImage(String folderName, String fileName) throws MalformedURLException {

        Path folder = FILES_FOLDER.resolve(folderName);

        Path imagePath = createFilePath(folder, fileName);

        Resource file = new UrlResource(imagePath.toUri());

        if(!Files.exists(imagePath)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(file);
        }
    }


}
