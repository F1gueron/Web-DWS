package es.board.webdws.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.board.webdws.model.Forum;
import es.board.webdws.model.Writeup;
import org.springframework.stereotype.Service;


@Service
public class ForumService {

    private ConcurrentMap<Long, Forum> forum = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public ForumService() {
        save(new Forum("Lokete", "HackOn 2023 - URJC Cybersecurity Event - Discussion Forum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",LocalDate.of(2023, 2, 17)));
        save(new Forum("Figueron", "HackOn 2024 - CTF Q/A", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", LocalDate.of(2024, 2, 19)));
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
