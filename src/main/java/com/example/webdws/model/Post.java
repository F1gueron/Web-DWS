package com.example.webdws.model;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class Post {

    private Long id;
    private String author;
    private String title;
    private String text;
    private boolean isImage = false; // Variable to check if there is an image in the post.
    private boolean isFile = false; // IDEM

    public boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(boolean file) {
        this.isFile = file;
    }

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public Boolean getIsImage(){
        return isImage;
    }
    public void setIsImage(boolean image) {
        this.isImage = image;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Post(){}

    public Post(String author, String title, String text, LocalDate date) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Post(String author, String title, String text) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
