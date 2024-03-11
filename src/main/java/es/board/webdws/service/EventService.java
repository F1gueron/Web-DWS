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
        save(new Event("Event1", "Hackon 23", "Here we are going to upload some writeups and we are going to answer any question that you ask to solve any challenges", LocalDate.of(2023, 2, 17)));
        save(new Event("Event2", "Hackon 24", "This is gonna be a post dedicated to questions and solves of the 2024 Hackon CTF", LocalDate.of(2024, 2, 19)));
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
