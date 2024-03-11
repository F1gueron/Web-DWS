package es.board.webdws.restcontroller;


import es.board.webdws.model.Forum;
import es.board.webdws.model.Comment;
import es.board.webdws.service.ForumService;
import es.board.webdws.service.ImageService;
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
public class ForumRestController {

    private static final String POSTS_FOLDER = "forum";

    @Autowired
    private ForumService forumService;

    @Autowired
    private ImageService imageService;

    //Show "Index"
    @GetMapping("/forum")
    public Collection<Forum> showForums() {
        return forumService.findAll();
    }

    //Show Forum
    @PostMapping("/forum")
    public ResponseEntity<Forum> post_new_forum(@RequestBody Forum forum) {

        forumService.save(forum);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(forum.getId()).toUri();

        return ResponseEntity.created(location).body(forum);
    }

    //Create
    @GetMapping("/forum/{id}")
    public ResponseEntity<Forum> showPost( @PathVariable long id) {
        Forum forum = forumService.findById(id);

        if (forum != null){
            return ResponseEntity.ok(forum);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    // Update
    @PutMapping("forum/{id}")
    public ResponseEntity<Forum> replacePost(@PathVariable long id, @RequestBody Forum newForum) {

        Forum forum = forumService.findById(id);

        if (forum != null) {

            newForum.setId(id);
            forumService.edit(newForum);

            return ResponseEntity.ok(forum);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/forum/{id}")
    public ResponseEntity<Forum> deleteForum(@PathVariable long id) throws IOException {

        Forum forum = forumService.findById(id);

        if (forum != null) {
            forumService.deleteById(id);

            if(!forum.getImageName().isEmpty()) {
                this.imageService.deleteImage(POSTS_FOLDER, forum.getImageName());
            }

            return ResponseEntity.ok(forum);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //Add comment

    @PostMapping("/forum/{id}/comments")
    public ResponseEntity<Forum> create_comment(@RequestBody Comment comment, @PathVariable long id){

        Forum forum = forumService.findById(id);

        forum.addComment(comment);
        forumService.save(forum);

        return ResponseEntity.ok(forum);
    }
    //Download File to user
    @PostMapping("forum/{id}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        Forum forum = forumService.findById(id);

        if (forum != null) {

            URI location = fromCurrentRequest().build().toUri();

            forum.setImageName(location.toString());
            forumService.save(forum);

            imageService.saveImage(POSTS_FOLDER, imageFile, forum.getImageName());

            return ResponseEntity.created(location).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("forum/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Forum forum = forumService.findById(id);
        return imageService.createResponseFromImage(POSTS_FOLDER, forum.getImageName());
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long id) throws IOException {

        Forum forum = forumService.findById(id);

        if(forum != null) {

            forum.setImageName(null);
            forumService.save(forum);

            imageService.deleteImage(POSTS_FOLDER, forum.getImageName());

            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
