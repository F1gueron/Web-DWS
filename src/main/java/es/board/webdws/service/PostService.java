package es.board.webdws.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.board.webdws.model.Post;

@Service
public class PostService {

    private ConcurrentMap<Long, Post> posts = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public PostService() {
        save(new Post("Lokete", "Hackon 23", "Here we are going to upload some writeups and we are going to answer any question that you ask to solve any challenges",LocalDate.of(2023, 2, 17)));
        save(new Post("Figueron", "Hackon 24", "This is gonna be a post dedicated to questions and solves of the 2024 Hackon CTF", LocalDate.of(2024, 2, 19)));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(long id) {
        return posts.get(id);
    }

    public void save(Post post) {

        long id = nextId.getAndIncrement();

        post.setId(id);

        this.posts.put(id, post);
    }

    public void deleteById(long id) {
        this.posts.remove(id);
    }

}
