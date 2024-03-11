package es.board.webdws.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public Collection<Writeup> findByCategory(String category) {
        List <Writeup> res = new ArrayList<>();
        for (Map.Entry<Long, Writeup> entry : writeup.entrySet()) {
            if (entry.getValue().getCategory().equals(category)){
                res.add(entry.getValue());
            }
        }
        return res;
    }

    public Writeup findById(long id) {
        return this.writeup.get(id);
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
