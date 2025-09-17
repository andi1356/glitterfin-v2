package arobu.glitterfinv2.controller.frontend;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Glitterfin");
        model.addAttribute("message", "Finance operations designed for modern teams");
        model.addAttribute("pageTitle", "Welcome");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user", authentication);
        return "index"; // returns templates/index.html
    }
}