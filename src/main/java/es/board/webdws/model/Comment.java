package es.board.webdws.model;

import java.time.LocalDate;

public class Comment {

    private Long id;
    private String author;
    private String text;
    private LocalDate date;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Comment(){}

    public Comment(String author, String text, LocalDate date) {
        super();
        this.author = author;
        this.text = text;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + this.id +
                ", author='" + this.author + '\'' +
                ", text='" + this.text + '\'' +
                ", date=" + this.date +
                '}';
    }
}
