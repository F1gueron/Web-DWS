package com.example.webdws.controller;

import java.io.IOException;
import java.net.MalformedURLException;

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


    @GetMapping("/")
    public String showPosts(Model model, HttpSession session) {

        model.addAttribute("posts", postService.findAll());
        model.addAttribute("welcome", session.isNew());

        return "index";
    }

    @GetMapping("/post/new")
    public String newPostForm(Model model) {

        model.addAttribute("author", authorSession.getAuthor());

        return "new_post";
    }

    @PostMapping("/post/new")
    public String newPost(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {

        postService.save(post);

        imageService.saveImage(POSTS_FOLDER, post.getId(), image);

        fileService.saveFile(POSTS_FOLDER, post.getId(), file);

        authorSession.setAuthor(post.getAuthor());
        authorSession.incNumPosts();

        model.addAttribute("numPosts", authorSession.getNumPosts());

        return "saved_posts";
    }

    @GetMapping("/post/{id}")
    public String showPost(Model model, @PathVariable long id) {

        Post post = postService.findById(id);

        model.addAttribute("post", post);

        return "show_post";
    }
    @GetMapping("/post/{id}/file")
    public ResponseEntity<Object> downloadFile(@PathVariable int id) throws MalformedURLException {

        return fileService.createResponseFromFile(POSTS_FOLDER, id);
    }

    @GetMapping("/post/{id}/delete")
    public String deletePost(Model model, @PathVariable long id) throws IOException {

        postService.deleteById(id);

        imageService.deleteImage(POSTS_FOLDER, id);

        return "deleted_post";
    }
}
