package es.board.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import es.board.webdws.service.AuthorSession;
import es.board.webdws.service.FileService;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.WriteupService;


import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.board.webdws.model.Writeup;

@Controller
public class WriteupController {

    private static final String POSTS_FOLDER = "writeup";

    @Autowired
    private WriteupService writeupService;

    @Autowired
    private AuthorSession authorSession;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;

    // Create Writeup
    @GetMapping("/writeup/new")
    public String newWriteup(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_writeup";
    }

    @PostMapping("/writeup/new")
    public String newWriteup(@RequestParam("Category") String category, Model model, Writeup writeup, MultipartFile image, MultipartFile file) throws IOException {

        writeup.setCategory(category);

        return uploadData(model, writeup, image, file);
    }

    //Delete Writeup
    @GetMapping("/writeup/{id}/delete")
    public String deleteWriteup(Model model, @PathVariable long id) throws IOException {

        writeupService.deleteById(id);

        // imageService.deleteImage(POSTS_FOLDER, id);

        return "deleted_forum";
    }

    //Download File to user
    @GetMapping("/writeup/{id}/file")
    public ResponseEntity<Object> downloadFile(Writeup writeup,@PathVariable int id, @RequestParam(required = false) boolean download) throws MalformedURLException {
        String name = writeup.getFileName();
        return fileService.createResponseFromFile(POSTS_FOLDER, name, download);
    }

    @GetMapping("/writeup/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable String id, Writeup writeup) throws MalformedURLException {

        return imageService.createResponseFromImage(POSTS_FOLDER, writeup.getFileName());
    }

    //Show writeups
    @GetMapping("/writeup")
    public String showWriteups(@RequestParam String category, Model model) {
       if(category.equals("pwn")){
           model.addAttribute("pwn", category);
       }
        return "writeup";
    }

    //Save files
    private String file_to_UUID (MultipartFile file){
        String new_fileName = UUID.randomUUID().toString();
        String old_fileName = file.getOriginalFilename();
        String fileExtension = get_file_extension(old_fileName); // handleFile checks if it is empty so this is never null.

        return new_fileName + fileExtension;
    }

    private String uploadData(Model model, Writeup writeup, MultipartFile image, MultipartFile file) throws IOException {
        uploadHandler(file, image, writeup);
        authorSession.setAuthor(writeup.getAuthor());
        authorSession.incNumWriteups();

        model.addAttribute("numWriteup", authorSession.getNumWriteups());

        return "saved_writeup";
    }

    private String get_file_extension(String filename){
        return filename.substring(filename.lastIndexOf("."));
    }


    private String handleFile(MultipartFile file){
        String new_filename = null;
        if (!file.isEmpty()){
            new_filename = file_to_UUID(file);
        }

        return new_filename;
    }

    private void uploadHandler(MultipartFile file, MultipartFile image, Writeup writeup) throws IOException {
        writeupService.save(writeup);
        String final_file = handleFile(file);
        String final_image = handleFile(image);

        fileService.saveFile(POSTS_FOLDER, writeup.getId(), file, final_file);
        imageService.saveImage(POSTS_FOLDER, writeup.getId(), image, final_image);

        writeup.setFileName(final_file);
    }
}
