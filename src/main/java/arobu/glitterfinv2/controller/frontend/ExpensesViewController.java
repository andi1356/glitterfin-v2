package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.service.ExpenseEntryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExpensesViewController {

    private final ExpenseEntryService expenseEntryService;

    public ExpensesViewController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping("/expenses")
    public String showFinances(Model model) {
        List<ExpenseFrontendDTO> expenses = expenseEntryService.getExpensesForCurrentUser();

        model.addAttribute("expenses", expenses);
        model.addAttribute("appName", "Glitterfin v2");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        return "expenses";
    }
}
