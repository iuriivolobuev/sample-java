package app.dto;

import java.util.List;

public class DeckDto {
    private Long id;
    private final List<DeckItemDto> items;

    public DeckDto(Long id, List<DeckItemDto> items) {
        this.id = id;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public List<DeckItemDto> getItems() {
        return items;
    }
}
