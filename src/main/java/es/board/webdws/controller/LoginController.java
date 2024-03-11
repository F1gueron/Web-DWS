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
public class LoginController {

    @GetMapping("/login")
    public String show_login(){
        return "../static/login";
    }


    @PostMapping("/login")
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", required = false) boolean remember){

        if("admin".equals(username) && "admin".equals(password)){
            return "redirect:/";
        }
        else {
            model.addAttribute("login-failed", true);
            return "redirect:/login";
        }


    }


}
