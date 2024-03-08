package es.board.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import es.board.webdws.model.Forum;
import es.board.webdws.service.AuthorSession;
import es.board.webdws.service.FileService;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.ForumService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ForumController {

    private static final String POSTS_FOLDER = "forum";

    @Autowired
    private ForumService forumService;

    @Autowired
    private AuthorSession authorSession;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;


    @GetMapping("/forum")
    public String showForums(Model model, HttpSession session) {

        model.addAttribute("forums", forumService.findAll());
        model.addAttribute("welcome", session.isNew());

        return "forum";
    }

    @GetMapping("/")
    public String landingPage() {

        return "../static/index";
    }



    /*
        Initial getmapping to show the choose type of post to create

     */

    @GetMapping("/new")
    public String choosePost() {

        return "create_new";
    }

    /*
        getmapping to show the new_post that we have used until today

     */

    //Create forum
    @GetMapping("/forum/new")
    public String newForum(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_forum";
    }

    @PostMapping("/forum/new")
    public String newForum(Model model, Forum forum, MultipartFile image, MultipartFile file) throws IOException {

        return uploadData(model, forum, image, file);
    }

    /*
        This function creates a post with (if exists) an Image and (if exists) a File.
        It checks if we passed a file or not to change the post parameter so, it changes when resolving the image into the html (same with the file).

        Function : newPost
        param : Model, post, image, file
        returns : "saved_posts" route.


     */

    @GetMapping("/todo")
    public String todo() {
        return "todo";
    }


    @GetMapping("/forum/{id}")
    public String showPost(Model model, @PathVariable long id) {

        Forum forum = forumService.findById(id);
        model.addAttribute("image", !forum.getImageName().isEmpty());
        model.addAttribute("file", !forum.getFileName().isEmpty());
        model.addAttribute("forum", forum);

        return "show_forum";
    }

    //Redirect to log in
    @GetMapping("/login")
    public String login(){
        return "../static/login";
    }



    private String uploadData(Model model, Forum forum, MultipartFile image, MultipartFile file) throws IOException {
        uploadHandler(file, image, forum);
        authorSession.setAuthor(forum.getAuthor());
        authorSession.incNumForums();
        String title = forum.getTitle();

        model.addAttribute("numForum", authorSession.getNumForums());
        model.addAttribute("title",title);

        return "saved_forum";
    }

    //Download File to user
    @GetMapping("/forum/{id}/file")
    public ResponseEntity<Object> downloadFile(@PathVariable long id, @RequestParam(required = false) boolean download) throws MalformedURLException {
        Forum forum = forumService.findById(id);
        return fileService.createResponseFromFile(POSTS_FOLDER, forum.getFileName(), download);
    }

    @GetMapping("/forum/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws MalformedURLException {

        Forum forum = forumService.findById(id);

        return imageService.createResponseFromImage(POSTS_FOLDER, forum.getFileName());
    }

    @GetMapping("/forum/{id}/delete")
    public String deleteForum(Forum forum, @PathVariable long id) throws IOException {

        forumService.deleteById(id);

        imageService.deleteImage(POSTS_FOLDER, forum.getImageName());
        fileService.deleteFile(POSTS_FOLDER, forum.getFileName());

        return "deleted_forum";
    }

    // Methods to handle images and files, probably remove image later, as it's a type of file

    private String file_to_UUID (MultipartFile file){
        String new_fileName = UUID.randomUUID().toString();
        String old_fileName = file.getOriginalFilename();
        String fileExtension = get_file_extension(old_fileName); // handleFile checks if it is empty so this is never null.

        return new_fileName + fileExtension;
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

    private void uploadHandler(MultipartFile file, MultipartFile image, Forum forum) throws IOException {
        forumService.save(forum);
        String final_file = handleFile(file);
        String final_image = handleFile(image);

        if (final_file != null){
            fileService.saveFile(POSTS_FOLDER, forum.getId(), file, final_file);
            forum.setFileName(final_file);
        }
        if (final_image != null) {
            imageService.saveImage(POSTS_FOLDER, forum.getId(), image, final_image);
            forum.setImageName(final_image);
        }

    }

}
