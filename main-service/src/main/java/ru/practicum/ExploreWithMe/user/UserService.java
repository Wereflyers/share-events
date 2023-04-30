package ru.practicum.ExploreWithMe.user;


import ru.practicum.ExploreWithMe.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    List<UserDto> getIds(Collection<Long> ids, int from, int size);

    List<UserDto> getAll(int from, int size);

    UserDto delete(long id);

    UserDto add(UserDto userDto);
}