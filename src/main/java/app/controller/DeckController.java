package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deck")
public class DeckController {
    @GetMapping("/{id}")
    public List<String> getDeck(@PathVariable long id) {
        return List.of("term1", "term2", "term3");
    }
}
