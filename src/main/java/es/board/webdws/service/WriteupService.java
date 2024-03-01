package es.board.webdws.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.board.webdws.model.Writeup;


@Service
public class WriteupService {

    private ConcurrentMap<Long, Writeup> writeup = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public WriteupService() {
        save(new Writeup("Lokete", "Hackon 23", "Here we are going to upload some writeups and we are going to answer any question that you ask to solve any challenges", LocalDate.of(2023, 2, 17)));
        //save(new Post("Figueron", "Hackon 24", "This is gonna be a post dedicated to questions and solves of the 2024 Hackon CTF", LocalDate.of(2024, 2, 19)));
    }

    public Collection<Writeup> findAll() {
        return writeup.values();
    }

    public Writeup findById(long id) {
        return writeup.get(id);
    }

    public void save(Writeup writeup) {

        long id = nextId.getAndIncrement();

        writeup.setId(id);

        this.writeup.put(id, writeup);
    }

    public void deleteById(long id) {
        this.writeup.remove(id);
    }

}
