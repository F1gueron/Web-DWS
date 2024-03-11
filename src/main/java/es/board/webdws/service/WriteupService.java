package es.board.webdws.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.board.webdws.model.Event;
import org.springframework.stereotype.Service;

import es.board.webdws.model.Writeup;


@Service
public class WriteupService {

    public WriteupService() {
        save(new Writeup("Figueron", "rev_windowsofopportunity", "If we debug it with Ghidra, we can find a password that its compared with the same password, but the character in the next position, so when we find the password is easy.\n Next, we can find the password in another function from the same file that it's HTB{... (we let you get this!!) ", LocalDate.of(2023, 2, 17), "reversing", "rev_windowsofopportunity.zip"));
        save(new Writeup("Lokete", " lokete", "In this challenge we have with a Python3 short script that encrypts a message with a random generated p and q. The output.txt files gives us the N used, as well as the exponent and the final cypher. The seed is taken from an environment variable from the system with a comment \"# La seed soy yo\". After some time researching about environment variables in Linux, we see that it is not possible to retrieve it in any way, so we have to get creative here. We try several seeds, but after some tries the one that works finally is \"lokete\" (surprise). Now we can just generate our prime numbers p and q and decrypt the message with RSA algorithm. FLAG - flag{r4p_s0lo}", LocalDate.of(2024, 2, 19), "crypto", "lokete.zip"));
    }
    private ConcurrentMap<Long, Writeup> writeup = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();


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

    public void edit(Writeup newWriteup) {
        long id = newWriteup.getId();
        Writeup existingWriteup = writeup.get(id);

        if (existingWriteup != null) {

            existingWriteup.setAuthor(newWriteup.getAuthor());
            existingWriteup.setTitle(newWriteup.getTitle());
            existingWriteup.setText(newWriteup.getText());
            existingWriteup.setImageName(newWriteup.getImageName());
            existingWriteup.setFileName(newWriteup.getFileName());
            existingWriteup.setCategory(newWriteup.getCategory());

            writeup.put(id, existingWriteup);
        }
    }

}
