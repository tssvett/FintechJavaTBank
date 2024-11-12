package org.example.task10.service.crud.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task10.dto.EventCreateDto;
import org.example.task10.dto.EventReadDto;
import org.example.task10.dto.EventUpdateDto;
import org.example.task10.entity.Event;
import org.example.task10.exception.EntityDeleteException;
import org.example.task10.repository.EventRepository;
import org.example.task10.service.crud.CrudService;
import org.example.task10.specification.EventSpecification;
import org.example.task10.utils.Mapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCrudServiceImpl implements CrudService<Long, Event, EventCreateDto, EventReadDto, EventUpdateDto> {
    private final EventRepository eventRepository;
    private final PlaceCrudServiceImpl placeService;
    private final EventSpecification eventSpecification;

    @Override
    public Long create(EventCreateDto eventCreateDto) {
        return eventRepository.save(Mapper.toEvent(eventCreateDto, placeService.read(eventCreateDto.placeId())))
                .getId();
    }

    @Override
    public Event read(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<Event> readAll(EventReadDto eventReadDto) {
        return eventRepository.findAll(eventSpecification
                .filterEventsBySearchCriteria(Mapper.toSearchCriteria(eventReadDto)));
    }

    @Override
    public Event update(EventUpdateDto eventUpdateDto) {
        Event event = this.read(eventUpdateDto.eventIdToUpdate());

        event.setName(eventUpdateDto.name());
        event.setDate(eventUpdateDto.date());
        event.setPlace(placeService.read(eventUpdateDto.placeId()));

        return eventRepository.save(event);
    }

    @Override
    public void delete(Long id) {
        try {
            eventRepository.deleteById(id);
        } catch (Exception e) {
            throw new EntityDeleteException("Failed to delete event with id: " + id);
        }
    }
}
