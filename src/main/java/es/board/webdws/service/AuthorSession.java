package es.board.webdws.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AuthorSession {

    private String author;
    private int numPosts;
    private int numWriteup;
    private int numEvents;

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumForums() {
        return this.numPosts;
    }

    public void incNumForums() {
        this.numPosts++;
    }
    public int getNumWriteups() {
        return this.numWriteup;
    }

    public void incNumWriteups() {
        this.numWriteup++;
    }
    public int getNumEvents() {
        return this.numEvents;
    }

    public void incNumEvents() {
        this.numEvents++;
    }
}
