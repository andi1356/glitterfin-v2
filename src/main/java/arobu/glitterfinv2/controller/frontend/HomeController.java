package arobu.glitterfinv2.controller.frontend;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Glitterfin");
        model.addAttribute("heroTitle", "Finance intelligence for modern teams");
        model.addAttribute("heroSubtitle", "Track, understand, and optimise your spending with beautiful dashboards and actionable insights.");
        model.addAttribute("features", List.of(
                new FeatureCard("Expense visibility", "Gain a clear overview of your latest purchases with rich context and categorisation."),
                new FeatureCard("Collaboration ready", "Work with your team to flag shared costs and spot outliers before they surprise you."),
                new FeatureCard("Secure by default", "Enterprise-grade security keeps your financial data protected across the platform.")
        ));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user", authentication);
        return "index"; // returns templates/index.html
    }

    public record FeatureCard(String title, String description) { }
}