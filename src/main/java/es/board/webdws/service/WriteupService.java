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

    public WriteupService() {}

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
