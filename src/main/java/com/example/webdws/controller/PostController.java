package com.example.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.example.webdws.model.Post;
import com.example.webdws.service.ImageService;
import com.example.webdws.service.PostService;
import com.example.webdws.service.AuthorSession;
import com.example.webdws.service.FileService;



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
    public String landingPage(Model model, HttpSession session) {

        return "index";
    }

    @GetMapping("/writeups")
    public String showWriteups(Model model, HttpSession session) {

        //model.addAttribute("posts", postService.findAll());
        //model.addAttribute("welcome", session.isNew());

        return "../static/writeups";
    }


    /*
        Initial getmapping to show the choose type of post to create

     */

    @GetMapping("/post/new")
    public String choosePost(Model model) {

        return "create_new";
    }

    /*
        getmapping to show the new_post that we have used until today

     */
    @GetMapping("/post/newpost")
    public String newPostForm(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "new_post";
    }




    /*
        This function creates a post with (if exists) an Image and (if exists) a File.
        It checks if we passed a file or not to change the post parameter so it changes when resolving the image into the html (same with the file).

        Function : newPost
        param : Model, post, image, file
        returns : "saved_posts" route.


     */

    @GetMapping("/todo")
    public String todo(){
        return "todo";
    }


    @PostMapping("/post/newpost")
    public String newPost(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {

        postService.save(post);
        String old_imageName = image.getOriginalFilename();
        String old_fileName = file.getOriginalFilename();

        if ( !old_imageName.isEmpty() ){

            String new_imageName = UUID.randomUUID().toString();
            String fileExtension = old_imageName.substring(old_imageName.lastIndexOf("."));
            imageService.saveImage(POSTS_FOLDER, post.getId(), image, new_imageName + fileExtension);

            post.setImageName(new_imageName);
        }

        if ( !old_fileName.isEmpty() ){

            String new_fileName = UUID.randomUUID().toString();
            String fileExtension = old_fileName.substring(old_fileName.lastIndexOf("."));
            fileService.saveFile(POSTS_FOLDER, post.getId(), file, new_fileName + fileExtension);
            post.setFileName(new_fileName);
        }


        authorSession.setAuthor(post.getAuthor());
        authorSession.incNumPosts();

        model.addAttribute("numPosts", authorSession.getNumPosts());

        return "saved_posts";
    }

    @GetMapping("/post/{id}")
    public String showPost(Model model, @PathVariable long id) {

        Post post = postService.findById(id);
        model.addAttribute("image", !post.getImageName().isEmpty());
        model.addAttribute("file", !post.getFileName().isEmpty());
        model.addAttribute("post", post);

        return "show_post";
    }
    // TODO DOWNLOAD FILE AND IMG
    @GetMapping("/post/{id}/file")
    public String downloadFile(@PathVariable int id) throws MalformedURLException {

        // public ResponseEntity<Object> downloadFile(@PathVariable int id) throws MalformedURLException
        //return fileService.createResponseFromFile(POSTS_FOLDER, id);
        return "todo";
    }


    @GetMapping("/post/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable int id) throws MalformedURLException {

        return imageService.createResponseFromImage(POSTS_FOLDER, id);
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(Model model, @PathVariable long id) throws IOException {

        postService.deleteById(id);

        imageService.deleteImage(POSTS_FOLDER, id);

        return "deleted_post";
    }
}
