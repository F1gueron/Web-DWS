package es.board.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import es.board.webdws.model.Comment;
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
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/new")
    public String choosePost() {

        return "create_new";
    }

    @GetMapping("/todo")
    public String todo() {
        return "todo";
    }

    //Redirect to log in
    @GetMapping("/login")
    public String login(){
        return "../static/login";
    }

    @GetMapping("/forum")
    public String showForums(Model model, HttpSession session) {

        model.addAttribute("forums", forumService.findAll());
        model.addAttribute("welcome", session.isNew());

        return "forum";
    }

    /*
        This function creates a post with (if exists) an Image and (if exists) a File.
        It checks if we passed a file or not to change the post parameter so, it changes when resolving the image into the html (same with the file).

        Function : newPost
        param : Model, post, image, file
        returns : "saved_posts" route.


     */

    //Create forum

    @GetMapping("/forum/new")
    public String get_new_forum(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_forum";
    }

    @PostMapping("/forum/new")
    public String post_new_forum(Model model, Forum forum, MultipartFile image, MultipartFile file) throws IOException {

        return uploadData(model, forum, image, file);
    }

    @GetMapping("/forum/{id}")
    public String showPost(Model model, @PathVariable long id) {

        Forum forum = forumService.findById(id);
        model.addAttribute("image", !forum.getImageName().isEmpty());
        model.addAttribute("file", !forum.getFileName().isEmpty());
        model.addAttribute("forum", forum);

        if(forum.getComments() != null){
            model.addAttribute("comments", forum.getComments());
        }

        return "show_forum";
    }

    @PostMapping("/forum/{id}/comments")
    public String create_comment(@ModelAttribute Comment comment, @PathVariable long id){
        Forum forum = forumService.findById(id);

        forum.addComment(comment);

        return "redirect:/forum/{id}";
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

    // Methods to handle images and files
    private String uploadData(Model model, Forum forum, MultipartFile image, MultipartFile file) throws IOException {
        uploadHandler(file, image, forum);
        authorSession.setAuthor(forum.getAuthor());
        authorSession.incNumForums();
        String title = forum.getTitle();

        model.addAttribute("numForum", authorSession.getNumForums());
        model.addAttribute("title",title);

        return "saved_forum";
    }

    private void uploadHandler(MultipartFile file, MultipartFile image, Forum forum) throws IOException {
        forumService.save(forum);
        String final_file = handleFile(file);
        String final_image = handleFile(image);

        if (final_file != null){
            fileService.saveFile(POSTS_FOLDER, file, final_file);
            forum.setFileName(final_file);
        }
        if (final_image != null) {
            imageService.saveImage(POSTS_FOLDER, image, final_image);
            forum.setImageName(final_image);
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
