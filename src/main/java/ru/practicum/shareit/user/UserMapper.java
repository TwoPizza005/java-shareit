package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User toUser(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public static void updateUser(User target, UserDto dto) {
        if (dto.getName() != null) {
            target.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            target.setEmail(dto.getEmail());
        }
    }
}