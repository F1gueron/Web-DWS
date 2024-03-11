package es.board.webdws.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.board.webdws.model.Comment;
import es.board.webdws.model.Forum;
import es.board.webdws.model.Writeup;
import org.springframework.stereotype.Service;


@Service
public class ForumService {

    private ConcurrentMap<Long, Forum> forum = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();



    public ForumService() {
        /*List<Comment> comments1 = new ArrayList<>();
        comments1.add(new Comment("Luis", "Are you from the humanities? CMD comes pre-installed", LocalDate.of(2024, 1, 4)));
        comments1.add(new Comment("Rubio", "Here we have the IT person of the day", LocalDate.of(2024, 1, 6)));
        List<Comment> comments2 = new ArrayList<>();
        comments2.add(new Comment("The Loving One", "Don't be resentful, make love not war.", LocalDate.of(2023, 11, 4)));
        comments2.add(new Comment("Luis", "Are you from the humanities? CMD comes pre-installed", LocalDate.of(2024, 1, 4)));
        List<Comment> comments3 = new ArrayList<>();
        comments3.add(new Comment("Luis", "Are you from the humanities? CMD comes pre-installed", LocalDate.of(2024, 1, 4)));
        */
        save(new Forum("Miguelin", "How to install CMD on Windows", "If someone could help me install this element on my PC, it would be great, I need it for a legal work.", LocalDate.of(2023, 2, 17) ));
        save(new Forum("Figueron", "Seeking help to program a virus", "I want to mess up my boss who has been exploiting me for 10 years, but I don't have enough knowledge of IT", LocalDate.of(2024, 2, 19)));
        save(new Forum("Lokete", "What programming languages do you recommend for starting to program", "I would like to know which languages could help me have a better future as a programmer", LocalDate.of(2023, 7, 12)));
    }


    public Collection<Forum> findAll() {
        return forum.values();
    }

    public Forum findById(long id) {
        return forum.get(id);
    }

    public void save(Forum forum) {

        long id = nextId.getAndIncrement();

        forum.setId(id);

        this.forum.put(id, forum);
    }


    public void deleteById(long id) {
        this.forum.remove(id);
    }

    public void edit(Forum newForum) {
        long id = newForum.getId();
        Forum existingForum = forum.get(id);

        if (existingForum != null) {

            existingForum.setAuthor(newForum.getAuthor());
            existingForum.setTitle(newForum.getTitle());
            existingForum.setText(newForum.getText());
            existingForum.setImageName(newForum.getImageName());
            existingForum.setFileName(newForum.getFileName());
            existingForum.setCategory(newForum.getCategory());
            existingForum.setComments(newForum.getComments());

            forum.put(id, existingForum);
        }
    }
}
