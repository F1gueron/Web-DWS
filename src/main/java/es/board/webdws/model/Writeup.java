package es.board.webdws.model;

import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.util.List;

public class Writeup {
    private Long id;
    private String author;
    private String title;
    private String text;
    private String imageName = ""; // Name for embedded image
    private String fileName = "";  // Name for embedded file

    private String category = null;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Writeup(){}

    public Writeup(String author, String title, String text, LocalDate date, String category, String fileName) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
        this.category = category;
        this.fileName = fileName;
    }
    public Writeup(String author, String title, String text, LocalDate date) {
        super();
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Writeup(String author, String title, String text) {
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
