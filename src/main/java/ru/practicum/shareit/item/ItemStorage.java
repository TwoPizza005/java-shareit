package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Item create(Item item) {
        item.setId(idGenerator.getAndIncrement());
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<Item> findAllByOwner(Long ownerId) {
        return items.values().stream()
                .filter(i -> Objects.equals(i.getOwner(), ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        String lower = text.toLowerCase();
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (!Boolean.TRUE.equals(item.getAvailable())) {
                continue;
            }
            String name = item.getName() == null ? "" : item.getName().toLowerCase();
            String description = item.getDescription() == null ? "" : item.getDescription().toLowerCase();
            if (name.contains(lower) || description.contains(lower)) {
                result.add(item);
            }
        }
        return result;
    }
}