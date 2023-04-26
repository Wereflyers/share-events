package ru.practicum.ExploreWithMe.event;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.LocationDto;
import ru.practicum.ExploreWithMe.event.dto.NewEventDto;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.event.model.Location;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public static Event toNewEvent(Long userId, Long locationId, NewEventDto eventDto) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .category(eventDto.getCategory())
                .createdOn(LocalDateTime.now())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .initiator(userId)
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .state(State.PENDING)
                .title(eventDto.getTitle())
                .location(locationId)
                .build();
    }

    public static EventFullDto toEventFullDto(LocationDto locationDto, CategoryDto categoryDto, UserShortDto userShortDto, Event event, int confirmedRequests, int views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userShortDto)
                .location(locationDto)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .confirmedRequests(confirmedRequests)
                .views(views)
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
