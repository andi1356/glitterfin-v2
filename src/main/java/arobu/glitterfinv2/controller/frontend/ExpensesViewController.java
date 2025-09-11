package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryCrudDTO;
import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/expenses/new")
    public String newExpense(Model model) {
        model.addAttribute("expense", new ExpenseEntryCrudDTO());
        return "expense-form";
    }


    @GetMapping("/expenses/{id}/edit")
    public String editExpense(@PathVariable Integer id, Model model) {
        model.addAttribute("expense", expenseEntryService.getExpense(id));
        return "expense-form";
    }

    @PostMapping("/expenses")
    public String createExpense(@ModelAttribute ExpenseEntryCrudDTO expense) throws OwnerNotFoundException {
        expenseEntryService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/{id}")
    public String updateExpense(@PathVariable Integer id, @ModelAttribute ExpenseEntryCrudDTO expense) throws OwnerNotFoundException {
        expense.setId(id);
        expenseEntryService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/{id}/delete")
    public String deleteExpense(@PathVariable Integer id) {
        expenseEntryService.deleteExpense(id);
        return "redirect:/expenses";
    }
}
