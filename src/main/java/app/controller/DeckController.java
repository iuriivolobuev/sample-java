package app.controller;

import app.dto.DeckDto;
import app.dto.DeckItemDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/deck")
public class DeckController {
    @GetMapping("/{id}")
    public DeckDto getDeck(@PathVariable long id) {
        return new DeckDto(id, List.of(
                new DeckItemDto("der Stift", "pen"),
                new DeckItemDto("die Stadt", "city"),
                new DeckItemDto("das Buch" , "book")
        ));
    }
}
