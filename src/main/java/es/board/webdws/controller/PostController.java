package es.board.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import es.board.webdws.service.AuthorSession;
import es.board.webdws.service.FileService;
import es.board.webdws.service.ImageService;
import es.board.webdws.service.PostService;
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

import es.board.webdws.model.Post;


@Controller
public class PostController {

    private static final String POSTS_FOLDER = "posts";

    @Autowired
    private PostService postService;

    @Autowired
    private AuthorSession authorSession;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;


    @GetMapping("/posts")
    public String showPosts(Model model, HttpSession session) {

        model.addAttribute("posts", postService.findAll());
        model.addAttribute("welcome", session.isNew());

        return "posts";
    }

    @GetMapping("/")
    public String landingPage() {

        return "index";
    }

    @GetMapping("/writeups")
    public String showWriteups() {

        return "../static/writeups";
    }


    /*
        Initial getmapping to show the choose type of post to create

     */

    @GetMapping("/post/new")
    public String choosePost() {

        return "create_new";
    }

    /*
        getmapping to show the new_post that we have used until today

     */


    @GetMapping("/post/newwriteup")
    public String newWriteup(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_writeup";
    }



    @GetMapping("/post/newforum")
    public String newForum(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "creation_pages/new_forum";
    }


    // One getmapping for each creator

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


    @GetMapping("/post/{id}")
    public String showPost(Model model, @PathVariable long id) {

        Post post = postService.findById(id);
        model.addAttribute("image", !post.getImageName().isEmpty());
        model.addAttribute("file", !post.getFileName().isEmpty());
        model.addAttribute("post", post);

        return "show_post";
    }

    @PostMapping("/post/newpost")
    public String newPost(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {

        return uploadData(model, post, image, file);

    }


    @PostMapping("/post/newforum")
    public String newForum(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {



        return uploadData(model, post, image, file);
    }

    private String uploadData(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {
        if (!image.isEmpty()){
            uploadHandler(image, post);
        }
        if (!file.isEmpty()) {
            uploadHandler(file, post);
        }
        authorSession.setAuthor(post.getAuthor());
        authorSession.incNumPosts();

        model.addAttribute("numPosts", authorSession.getNumPosts());
        if (post.getCategory() != null){
            model.addAttribute("storageLocation", post.getCategory());
        }

        return "saved_posts";
    }

    @PostMapping("/post/newwriteup")
    public String newWriteup(@RequestParam("Category") String category, Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {
        post.setCategory(category);
        return uploadData(model, post, image, file);
    }

    // TODO DOWNLOAD FILE AND IMG
    @GetMapping("/post/{id}/file")
    public ResponseEntity<Object> downloadFile(@PathVariable int id, @RequestParam(required = false) boolean download) throws MalformedURLException {
        String a = "a";
        return fileService.createResponseFromFile(POSTS_FOLDER, a, download);
    }

    @GetMapping("/post/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable String id) throws MalformedURLException {
        String a = "a";
        return imageService.createResponseFromImage(POSTS_FOLDER, a);
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(Model model, @PathVariable long id) throws IOException {

        postService.deleteById(id);

        // imageService.deleteImage(POSTS_FOLDER, id);

        return "deleted_post";
    }

    // Methods to handle images and files, probably remove image later, as it's a type of file


    private void uploadHandler(MultipartFile file, Post post) throws IOException {
        postService.save(post);
        String old_fileName = file.getOriginalFilename();
        String content_type = file.getContentType();
        String new_fileName = UUID.randomUUID().toString();
        String fileExtension = old_fileName.substring(old_fileName.lastIndexOf("."));
        if (content_type.startsWith("image/")) {
            imageService.saveImage(POSTS_FOLDER, post.getId(), file, new_fileName + fileExtension);
        } else {
            fileService.saveFile(POSTS_FOLDER, post.getId(), file, new_fileName + fileExtension);
        }
        post.setFileName(new_fileName);
    }

}
