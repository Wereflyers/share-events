package ru.practicum.ExploreWithMe.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;
import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ExploreWithMe.request.dto.ParticipationRequestDto;
import ru.practicum.ExploreWithMe.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ParticipationRequestDto add(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("Event with id=" + eventId + " was not found."));
        if (Objects.equals(event.getInitiator(), userId) || event.getState() != State.PUBLISHED) {
            throw new WrongConditionException("Wrong state");
        }
        if (requestRepository.findByRequesterAndEvent(userId, eventId) != null)
            throw new DuplicateException("Repeated request");
        List<Request> requestList = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventId);
        if (requestList.size() == event.getParticipantLimit()) throw new WrongConditionException("Participant limit has reached");
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(userId)
                .event(eventId)
                .build();
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else
            request.setStatus(RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAll(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User with id=" + userId + " was not found."));
        return requestRepository.findAllByRequester(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NullPointerException("Request with id=" + requestId + " was not found"));
        if (!Objects.equals(request.getRequester(), userId)) throw new WrongConditionException("No rights for this");
        if (request.getStatus() == RequestStatus.CONFIRMED) throw new WrongConditionException("Already confirmed");
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User with id=" + userId + "is not found."));
        eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("Event with id=" + eventId + "is not found."));
        return requestRepository.findAllByEvent(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult update(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("Event with id=" + eventId + "was not found."));
        if ((!event.getRequestModeration() || event.getParticipantLimit() == 0) && updateRequest.getStatus() == RequestStatus.CONFIRMED) {
            return new EventRequestStatusUpdateResult(Stream.of(requestRepository.findByRequesterAndEvent(userId, eventId))
                    .map(RequestMapper::toRequestDto)
                    .collect(Collectors.toList()), null);
        }
        List<ParticipationRequestDto> confirmedRequestsBeforeUpdate = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        if (confirmedRequestsBeforeUpdate.size() == event.getParticipantLimit()) throw new WrongConditionException("The participant limit has been reached");
        List<ParticipationRequestDto> pendingRequestsBeforeUpdate = requestRepository.findAllByStatusAndEvent(RequestStatus.PENDING, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        if (updateRequest.getStatus() == RequestStatus.CONFIRMED && (pendingRequestsBeforeUpdate.size() + confirmedRequestsBeforeUpdate.size()
        + updateRequest.getRequestIds().size()) > event.getParticipantLimit()) {
            List<Long> idsOfPendingRequests = pendingRequestsBeforeUpdate.stream()
                            .map(ParticipationRequestDto::getId)
                                    .collect(Collectors.toList());
            update(userId, eventId, new EventRequestStatusUpdateRequest(idsOfPendingRequests, RequestStatus.CANCELED));
        }
        for (Long id: updateRequest.getRequestIds()) {
            Request oldRequest = requestRepository.findById(id).orElseThrow(() -> new NullPointerException("User with id=" + userId + "was not found."));
            if (oldRequest.getStatus() == RequestStatus.CONFIRMED) {
                throw new WrongConditionException("Cannot update request because it's not in the right state: " + oldRequest.getStatus());
            }
            oldRequest.setStatus(updateRequest.getStatus());
            requestRepository.save(oldRequest);
        }
        List<ParticipationRequestDto> confirmedRequests = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = requestRepository.findAllByStatusAndEvent(RequestStatus.REJECTED, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
