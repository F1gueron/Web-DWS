package es.board.webdws.controller;

import es.board.webdws.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "login_failed", required = false) boolean loginFailed, Model model) {
        if (loginFailed) {
            model.addAttribute("login_failed", true);
        }
        return "../static/login";
    }


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", required = false) boolean remember, HttpSession session){

        if(userService.validateUser(username, password)){
            session.setAttribute("USER", username);
            return "redirect:/new";
        }
        else {
            return "redirect:/login?login_failed=true";
        }


    }


}
