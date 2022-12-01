package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created = LocalDateTime.now();
}
