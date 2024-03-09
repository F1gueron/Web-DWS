package es.board.webdws.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    private Long id;
    private String author;
    private String title;
    private String text;
    private String imageName = ""; // Name for embedded image
    private String category = null;

    private List<String> participants = new ArrayList<>();
    private LocalDate date;

    public Event(){}

    public Event(String author, String title, String text, LocalDate date) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
    }
    public Event(String author, String title, String text) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void addUser(String name) {
        this.participants.add(name);
    }

    public List<String> getParticipants(){
        return this.participants;
    }
}
