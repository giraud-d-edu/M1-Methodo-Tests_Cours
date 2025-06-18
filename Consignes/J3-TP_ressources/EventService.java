package com.ynov.testing.service;

import com.ynov.testing.model.Event;
import com.ynov.testing.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Event entities
 *
 * Students must implement this service and achieve 100% test coverage.
 * Focus on business logic and proper exception handling.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieve all events from the database
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Find an event by its ID
     */
    public Optional<Event> getEventById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Event ID must be positive");
        }
        return eventRepository.findById(id);
    }

    /**
     * Create a new event
     */
    public Event createEvent(Event event) {
        validateEvent(event);

        if (eventRepository.existsByName(event.getName())) {
            throw new IllegalArgumentException("Event name already exists: " + event.getName());
        }

        return eventRepository.save(event);
    }

    /**
     * Update an existing event
     */
    public Event updateEvent(Long id, Event eventData) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));

        validateEvent(eventData);

        existingEvent.setName(eventData.getName());
        existingEvent.setDescription(eventData.getDescription());
        existingEvent.setEventDate(eventData.getEventDate());
        existingEvent.setActive(eventData.getActive());

        return eventRepository.save(existingEvent);
    }

    /**
     * Delete an event by ID
     */
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }

    /**
     * Get upcoming events (after current date)
     */
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now());
    }

    /**
     * Get past events (before current date)
     */
    public List<Event> getPastEvents() {
        return eventRepository.findByEventDateBefore(LocalDateTime.now());
    }

    /**
     * Get active events only
     */
    public List<Event> getActiveEvents() {
        return eventRepository.findByActiveTrue();
    }

    // Méthode privée de validation
    private void validateEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.getName() == null || event.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Event description is required");
        }
        if (event.getEventDate() == null) {
            throw new IllegalArgumentException("Event date is required");
        }
    }
}
