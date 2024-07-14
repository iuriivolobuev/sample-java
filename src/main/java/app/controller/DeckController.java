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
                new DeckItemDto("letter", "der Brief", "die Briefe", "blue"),
                new DeckItemDto("postal card", "die Postkarte", "die Postkarten", "red"),
                new DeckItemDto("parcel, package", "das Paket", "die Pakete", "green"),
                new DeckItemDto("small package, sachet", "das Päckchen", "die Päckchen", "green"),
                new DeckItemDto("note", "der Zettel", "die Zettel", "blue")
        ));
    }
}
