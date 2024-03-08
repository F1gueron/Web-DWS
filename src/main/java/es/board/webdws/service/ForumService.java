package es.board.webdws.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.board.webdws.model.Forum;
import org.springframework.stereotype.Service;


@Service
public class ForumService {

    private ConcurrentMap<Long, Forum> forum = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public ForumService() {
        save(new Forum("Lokete", "Hackon 23", "Here we are going to upload some writeups and we are going to answer any question that you ask to solve any challenges",LocalDate.of(2023, 2, 17)));
        save(new Forum("Figueron", "Hackon 24", "This is gonna be a post dedicated to questions and solves of the 2024 Hackon CTF", LocalDate.of(2024, 2, 19)));
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

}
