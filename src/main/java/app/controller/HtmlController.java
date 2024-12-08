package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {
    @GetMapping("/")
    public String indexPage() {
        return "/static/html/index.html";
    }

    @GetMapping("/decks")
    public String decksPage() {
        return "/static/html/decks.html";
    }

    @GetMapping("/deck/{id}")
    public String deckPage() {
        return "/static/html/deck.html";
    }
}
