package es.board.webdws.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AuthorSession {

    private String author;
    private int numPosts;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumPosts() {
        return this.numPosts;
    }

    public void incNumPosts() {
        this.numPosts++;
    }

}
