package arobu.glitterfinv2.controller.frontend;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Glitterfin v2");
        model.addAttribute("message", "Welcome to Glitterfin!");
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user", user);
        return "index";
    }
}