package app.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {
    @GetMapping("/")
    void indexPage(HttpServletResponse response) {
        response.setHeader("Location", "dogs");
        response.setStatus(302);
    }

    @GetMapping("/dogs")
    public String dogsPage() {
        return "/static/html/dogs.html";
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
