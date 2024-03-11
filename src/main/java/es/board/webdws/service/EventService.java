package es.board.webdws.service;

import es.board.webdws.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventService {
    private ConcurrentMap<Long, Event> event = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong();

    public EventService() {
        save(new Event("Figueron", "Hackon 25", "The CTF when we acted started as a team", LocalDate.of(2023, 2, 17), "HackOn Horizontal 2024 - dark.png"));
        save(new Event("Lokete", "Cyberapocalipsys", "This is a CTF from the platform HTB", LocalDate.of(2024, 2, 19), "maxresdefault.jpg"));
        save(new Event("Miguelin", "Rooted 25", "Rooted of 2025, event we have already participated 3 times already", LocalDate.of(2024, 2, 19), "logo-rootedcon.jpg"));
    }

    public Collection<Event> findAll() {
        return event.values();
    }

    public Event findById(long id) {
        return event.get(id);
    }

    public void save(Event event) {

        long id = nextId.getAndIncrement();

        event.setId(id);

        this.event.put(id, event);
    }


    public void deleteById(long id) {
        this.event.remove(id);
    }

    public void edit(Event newEvent) {
        long id = newEvent.getId();
        Event existingEvent = event.get(id);

        if (existingEvent != null) {

            existingEvent.setAuthor(newEvent.getAuthor());
            existingEvent.setTitle(newEvent.getTitle());
            existingEvent.setText(newEvent.getText());
            existingEvent.setImageName(newEvent.getImageName());
            existingEvent.setCategory(newEvent.getCategory());

            event.put(id, existingEvent);
        }
    }
}
