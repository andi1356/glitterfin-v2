package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.entity.TestEntity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Glitterfin v2");
        model.addAttribute("message", "Welcome to your Spring Boot app!");
        SecurityProperties.User user = new SecurityProperties.User();
        user.setName("Andrei");
        model.addAttribute("user", user);
        model.addAttribute("items", List.of(new TestEntity(1L,"Joke"), new TestEntity(2L, "Another Joke")));
        return "index"; // returns templates/index.html
    }
}