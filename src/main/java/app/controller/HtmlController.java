package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class HtmlController {
    @GetMapping("/")
    void index(HttpServletResponse response) {
        response.setHeader("Location", "decks");
        response.setStatus(302);
    }

    @GetMapping("/decks")
    public String decks() {
        return "/static/html/decks.html";
    }
}
