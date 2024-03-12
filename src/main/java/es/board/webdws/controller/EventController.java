package es.board.webdws.controller;


import es.board.webdws.model.Event;
import es.board.webdws.service.AuthorSession;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@Controller
public class EventController {
    private static final String POSTS_FOLDER = "event";

    @Autowired
    private EventService eventService;

    @Autowired
    private AuthorSession authorSession;

    @Autowired
    private ImageService imageService;

    //Show event "index"
    @GetMapping("/event")
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "event";
    }

    //Show Event
    @GetMapping("/event/{id}")
    public String showEvent(Model model, @PathVariable long id) {

        Event event = eventService.findById(id);
        model.addAttribute("image", !event.getImageName().isEmpty());
        model.addAttribute("participants", event.getParticipants());
        model.addAttribute("event", event);

        return "show_event";
    }

    @GetMapping("/event/new")
    public String newEvent(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_event";
    }

    @PostMapping("/event/new")
    public String newEvents( Model model, Event event, MultipartFile image) throws IOException {


        return uploadData(model, event, image);
    }

    //Delete Event
    @GetMapping("/event/{id}/delete")
    public String deleteEvent( @PathVariable long id) throws IOException {

        Event event = eventService.findById(id);
        eventService.deleteById(id);

        if (!event.getImageName().isEmpty()){
            imageService.deleteImage(POSTS_FOLDER, event.getImageName()); //TODO
        }


        return "deleted_event";
    }

    @GetMapping("/event/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Event event = eventService.findById(id);
        System.out.println(event.getImageName());
        return imageService.createResponseFromImage(POSTS_FOLDER, event.getImageName());
    }


    //Save files
    private String uploadData(Model model,Event event, MultipartFile image) throws IOException {
        uploadHandler( image, event);
        authorSession.setAuthor(event.getAuthor());
        authorSession.incNumEvents();

        model.addAttribute("numEvent", authorSession.getNumEvents());
        model.addAttribute("storageLocation", event.getCategory());
        model.addAttribute("title", event.getTitle());

        return "saved_event";
    }

    private void uploadHandler(MultipartFile image, Event event) throws IOException {
        eventService.save(event);
        String final_image = handleFile(image);

        if (final_image != null) {
            imageService.saveImage(POSTS_FOLDER, image, final_image);
            event.setImageName(final_image);
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
