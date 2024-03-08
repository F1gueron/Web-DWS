package es.board.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;


import es.board.webdws.service.AuthorSession;
import es.board.webdws.service.FileService;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.WriteupService;


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

    @GetMapping("/")
    public String landingPage() {

        return "../static/index";
    }

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

    //Show writeup "index"
    @GetMapping("/writeup")
    public String listWriteups(@RequestParam String category, Model model) {
        model.addAttribute("writeups", writeupService.findByCategory(category));
        return "writeup";
    }

    //Show writeup
    @GetMapping("/writeup/{id}")
    public String showWriteup(Model model, @PathVariable long id) {

        Writeup writeup = writeupService.findById(id);
        model.addAttribute("image", !writeup.getImageName().isEmpty());
        model.addAttribute("file", !writeup.getFileName().isEmpty());
        model.addAttribute("writeup", writeup);


        return "show_writeup";
    }

    //Delete Writeup
    @GetMapping("/writeup/{id}/delete")
    public String deleteWriteup(Writeup writeup, @PathVariable long id) throws IOException {

        writeupService.deleteById(id);

        imageService.deleteImage(POSTS_FOLDER, writeup.getImageName());
        fileService.deleteFile(POSTS_FOLDER, writeup.getFileName());

        return "deleted_writeup";
    }

    //Download File to user
    @GetMapping("/writeup/{id}/file")
    public ResponseEntity<Object> downloadFile(@PathVariable long id, @RequestParam(required = false) boolean download) throws MalformedURLException {
        Writeup writeup = writeupService.findById(id);
        return fileService.createResponseFromFile(POSTS_FOLDER, writeup.getFileName(), download);
    }

    @GetMapping("/writeup/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Writeup writeup = writeupService.findById(id);

        return imageService.createResponseFromImage(POSTS_FOLDER, writeup.getImageName());
    }

    //Save files
    private String uploadData(Model model, Writeup writeup, MultipartFile image, MultipartFile file) throws IOException {
        uploadHandler(file, image, writeup);
        authorSession.setAuthor(writeup.getAuthor());
        authorSession.incNumWriteups();

        model.addAttribute("numWriteup", authorSession.getNumWriteups());
        model.addAttribute("storageLocation", writeup.getCategory());

        return "saved_writeup";
    }

    private void uploadHandler(MultipartFile file, MultipartFile image, Writeup writeup) throws IOException {
        writeupService.save(writeup);
        String final_file = handleFile(file);
        String final_image = handleFile(image);

        if (final_file != null){
            fileService.saveFile(POSTS_FOLDER, writeup.getId(), file, final_file);
            writeup.setFileName(final_file);
        }
        if (final_image != null) {
            imageService.saveImage(POSTS_FOLDER, writeup.getId(), image, final_image);
            writeup.setImageName(final_image);
        }



    }

    private String handleFile(MultipartFile file){
        String new_filename = null;
        if (!file.isEmpty()){
            new_filename = file_to_UUID(file);
        }

        return new_filename;
    }

    private String file_to_UUID (MultipartFile file){
        String new_fileName = UUID.randomUUID().toString();
        String old_fileName = file.getOriginalFilename();
        String fileExtension = get_file_extension(old_fileName); // handleFile checks if it is empty so this is never null.

        return new_fileName + fileExtension;
    }

    private String get_file_extension(String filename){
        return filename.substring(filename.lastIndexOf("."));
    }

}
