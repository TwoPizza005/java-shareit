package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto create(Long userId, ItemDto dto) {
        userService.getEntity(userId); // Проверка, что пользователь существует
        Item item = ItemMapper.toItem(dto);
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        userService.getEntity(userId); // Проверка, что пользователь существует
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        if (!item.getOwner().equals(userId)) {
            throw new ForbiddenException("Only owner can edit item");
        }
        ItemMapper.updateItem(item, dto);
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemStorage.findById(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
    }

    @Override
    public List<ItemDto> getAllByOwner(Long userId) {
        userService.getEntity(userId); // Проверка, что пользователь существует
        return itemStorage.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}