import es.board.webdws.model.Post;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@GetMapping("/post/newpost")
public String newPostForm(Model model) {

    model.addAttribute("author", authorSession.getAuthor());

    return "creation_pages/new_post";
}

@GetMapping("/post/newctf")
public String newCTF(Model model) {

    model.addAttribute("author", authorSession.getAuthor());

    return "creation_pages/new_ctf";
}

@PostMapping("/post/newctf")
public String newCTF(Model model, Post post, MultipartFile image, MultipartFile file) throws IOException {

    return uploadData(model, post, image, file);

}
