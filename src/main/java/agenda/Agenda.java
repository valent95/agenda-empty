package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private final List<Event> events = new ArrayList<>();

    public void addEvent(Event e) {
        events.add(e);
    }

    public List<Event> eventsInDay(LocalDate day) {
        List<Event> todayEvents = new ArrayList<>();
        for (Event e : events) {
            if (e.isInDay(day)) {
                todayEvents.add(e);
            }
        }
        return todayEvents;
    }

    public List<Event> findByTitle(String title) {
        List<Event> results = new ArrayList<>();
        for (Event e : events) {
            if (e.getTitle().equals(title)) {
                results.add(e);
            }
        }
        return results;
    }

    public boolean isFreeFor(Event e) {
        LocalDateTime eventStart = e.getStart();
        LocalDateTime eventEnd = eventStart.plus(e.getDuration());

        for (Event existing : events) {
            LocalDateTime existingStart = existing.getStart();
            LocalDateTime existingEnd = existingStart.plus(existing.getDuration());

            if (eventStart.isBefore(existingEnd) && eventEnd.isAfter(existingStart)) {
                return false;
            }
        }
        return true;
    }
}