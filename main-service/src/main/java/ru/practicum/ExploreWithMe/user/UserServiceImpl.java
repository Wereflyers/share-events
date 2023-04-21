package ru.practicum.ExploreWithMe.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getIds(Collection<Long> ids, int from, int size) {
        return userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAll(int from, int size) {
        return userRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.toUserWithoutId(userDto)));
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDto delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NullPointerException("User with id=" + id + "is not found."));
        userRepository.deleteById(id);
        return UserMapper.toUserDto(user);
    }
}
