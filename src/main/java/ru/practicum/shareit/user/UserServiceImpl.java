package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage storage;

    @Override
    public UserDto create(UserDto dto) {
        if (storage.emailExists(dto.getEmail())) {
            throw new ConflictException("Email already used: " + dto.getEmail());
        }
        User saved = storage.create(UserMapper.toUser(dto));
        return UserMapper.toUserDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User user = getEntity(id);
        if (dto.getEmail() != null && storage.emailExistsForOther(dto.getEmail(), id)) {
            throw new ConflictException("Email already used: " + dto.getEmail());
        }
        UserMapper.updateUser(user, dto);
        return UserMapper.toUserDto(storage.update(user));
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(getEntity(id));
    }

    @Override
    public List<UserDto> getAll() {
        return storage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        getEntity(id);
        storage.delete(id);
    }

    @Override
    public User getEntity(Long id) {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
}