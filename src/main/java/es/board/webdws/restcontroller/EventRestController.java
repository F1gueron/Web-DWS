package es.board.webdws.restcontroller;

import es.board.webdws.model.Event;

import es.board.webdws.service.EventService;
import es.board.webdws.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/event")
public class EventRestController {
    private static final String POSTS_FOLDER = "event";

    @Autowired
    private EventService eventService;

    @Autowired
    private ImageService imageService;


    //Show event "index"
    @GetMapping("")
    public Collection<Event> listEvents(Model model) {
        return eventService.findAll();
    }

    //Show Event
    @GetMapping("{id}")
    public ResponseEntity<Event> showPost( @PathVariable long id) {
        Event event = eventService.findById(id);

        if (event != null){
            return ResponseEntity.ok(event);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //Create
    @PostMapping("")
    public ResponseEntity<Event> newEvents(@RequestBody Event event) throws IOException {


        eventService.save(event);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(event.getId()).toUri();

        return ResponseEntity.created(location).body(event);
    }


    //Delete Event
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteWriteup(@PathVariable long id) throws IOException {

        Event event = eventService.findById(id);

        if (event != null) {
            eventService.deleteById(id);


            if(!event.getImageName().isEmpty()) {
                this.imageService.deleteImage(POSTS_FOLDER, event.getImageName());
            }

            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Update
    @PutMapping("{id}")
    public ResponseEntity<Event> replaceEvent(@PathVariable long id, @RequestBody Event newEvent) {

        Event event = eventService.findById(id);

        if (event != null) {

            newEvent.setId(id);
            eventService.edit(newEvent);

            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Event event = eventService.findById(id);

        return imageService.createResponseFromImage(POSTS_FOLDER, event.getImageName());
    }




}
