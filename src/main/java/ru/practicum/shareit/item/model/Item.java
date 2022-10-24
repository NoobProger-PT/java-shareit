package ru.practicum.shareit.item.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Item {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, description, available);
    }
}
