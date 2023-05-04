package ru.practicum.ExploreWithMe.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;
import ru.practicum.ExploreWithMe.request.RequestRepository;
import ru.practicum.ExploreWithMe.statistics.StatisticService;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserMapper;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserDtoWithSubAbility;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubServiceImpl implements SubService {
    private final SubRepository subRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatisticService statisticService;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public SubDto add(Long subId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User with id=" + userId + "is not found."));
        if (!user.getSubscribe())
            throw new WrongConditionException("User has forbidden subscriptions");
        if (subRepository.existsBySubIdAndUserId(subId, userId))
            throw new DuplicateException("You have already been subscribed");
        return createResponse(subRepository.save(new Subscriber(null, userId, subId)).getSubId());
    }

    @Override
    @Transactional
    public SubDto delete(long subId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User with id=" + userId + "is not found."));
        if (!subRepository.existsBySubIdAndUserId(subId, userId))
            throw new WrongConditionException("You haven't subscribed on him");
        subRepository.deleteBySubIdAndUserId(subId, userId);
        return createResponse(subId);
    }

    @Override
    public SubDto get(long userId) {
        return createResponse(userId);
    }

    @Override
    public List<EventShortDto> getEvents(Long subId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NullPointerException("User with id=" + userId + "is not found."));
        if (!subRepository.existsBySubIdAndUserId(subId, userId))
            throw new WrongConditionException("You haven't subscribed on him");
        return eventRepository.findAllByInitiatorAndStateAndEventDateAfter(userId, State.PUBLISHED, LocalDateTime.now()).stream()
                .map(this::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDtoWithSubAbility changeAbility(Long userId, boolean ability) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getSubscribe() == ability)
            throw new WrongConditionException("Already " + ability);
        user.setSubscribe(ability);
        return UserMapper.toUserDtoWithSubAbility(userRepository.save(user));
    }

    public SubDto createResponse(Long subId) {
        SubDto sub = new SubDto();
        User user = userRepository.findById(subId).orElseThrow();
        sub.setUser(new UserShortDto(user.getId(), user.getName()));
        sub.setSubscriptions(subRepository.findAllBySubId(subId).stream()
                .map(Subscriber::getUserId)
                .collect(Collectors.toList()));
        if (user.getSubscribe()) {
            sub.setSubscribers(subRepository.countAllByUserId(subId));
        } else
            sub.setSubscribers(0);
        return sub;
    }

    private EventShortDto toEventShortDto(Event eventShort) {
        Integer confirmedReq = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventShort.getId()).size();
        CategoryDto category = CategoryMapper.toCategoryDto(categoryRepository.findById(eventShort.getCategory()).orElseThrow());
        User user = userRepository.findById(eventShort.getInitiator()).orElseThrow();
        return EventShortDto.builder()
                .annotation(eventShort.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedReq)
                .eventDate(eventShort.getEventDate())
                .id(eventShort.getId())
                .initiator(new UserShortDto(user.getId(), user.getName()))
                .paid(eventShort.getPaid())
                .title(eventShort.getTitle())
                .views(statisticService.getViews(eventShort.getId()).getHits())
                .build();
    }
}
