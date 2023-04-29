package ru.practicum.ExploreWithMe.user;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.user.dto.UserDto;
import ru.practicum.ExploreWithMe.user.dto.UserDtoWithSubAbility;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUserWithoutId(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail(), true);
    }


    public UserDtoWithSubAbility toUserDtoWithSubAbility(User user) {
        return new UserDtoWithSubAbility(user.getId(), user.getName(), user.getEmail(), user.getSubscribe());
    }
}
