package es.board.webdws.restcontroller;


import es.board.webdws.model.Writeup;
import es.board.webdws.service.FileService;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.WriteupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api")
public class WriteupRestController {

    private static final String POSTS_FOLDER = "writeup";

    @Autowired
    private WriteupService writeupService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;

    //Show writeup "index"
    @GetMapping("/writeup")
    public Collection<Writeup> listWriteups(@RequestParam String category) {
        return writeupService.findByCategory(category);
    }

    //Show writeup
    @GetMapping("/writeup/{id}")
    public ResponseEntity<Writeup> showWriteup( @PathVariable long id) {
        Writeup writeup = writeupService.findById(id);

        if (writeup != null){
            return ResponseEntity.ok(writeup);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //Create
    @PostMapping("/writeup")
    public ResponseEntity<Writeup> post_new_Writeup(@RequestBody Writeup writeup) {

        writeupService.save(writeup);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(writeup.getId()).toUri();

        return ResponseEntity.created(location).body(writeup);
    }

    //Update
    @PutMapping("writeup/{id}")
    public ResponseEntity<Writeup> replaceWriteup(@PathVariable long id, @RequestBody Writeup newWriteup) {

        Writeup writeup = writeupService.findById(id);

        if (writeup != null) {

            newWriteup.setId(id);
            writeupService.edit(newWriteup);

            return ResponseEntity.ok(writeup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/writeup/{id}")
    public ResponseEntity<Writeup> deleteWriteup
            (@PathVariable long id) throws IOException {

        Writeup writeup = writeupService.findById(id);

        if (writeup != null) {
            writeupService.deleteById(id);

            if(!writeup.getImageName().equals("")) { //TODO
                this.imageService.deleteImage(POSTS_FOLDER, writeup.getImageName());
            }

            return ResponseEntity.ok(writeup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Download File to user
    @PostMapping("writeup/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        Writeup writeup = writeupService.findById(id);

        if (writeup != null) {

            URI location = fromCurrentRequest().build().toUri();

            writeup.setImageName(location.toString());
            writeupService.save(writeup);

            imageService.saveImage(POSTS_FOLDER,  imageFile, writeup.getImageName());

            return ResponseEntity.created(location).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("writeup/{id}/file")
    public ResponseEntity<Object> uploadFile(@PathVariable long id, @RequestParam MultipartFile file)
            throws IOException {

        Writeup writeup = writeupService.findById(id);

        if (writeup != null) {

            URI location = fromCurrentRequest().build().toUri();

            writeup.setFileName(location.toString());
            writeupService.save(writeup);

            fileService.saveFile(POSTS_FOLDER, file, writeup.getImageName());

            return ResponseEntity.created(location).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/writeup/{id}/file")
    public ResponseEntity<Object> downloadFile(@PathVariable long id, @RequestParam(required = false) boolean download) throws MalformedURLException {
        Writeup writeup = writeupService.findById(id);
        return fileService.createResponseFromFile(POSTS_FOLDER, writeup.getFileName(), download);
    }

    @GetMapping("writeup/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Writeup writeup = writeupService.findById(id);
        return imageService.createResponseFromImage(POSTS_FOLDER, writeup.getImageName());
    }
}
