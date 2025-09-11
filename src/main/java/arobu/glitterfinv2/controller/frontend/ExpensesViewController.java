package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import arobu.glitterfinv2.service.mapper.ExpenseEntryMapper;
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
    public String showCreateForm(Model model) {
        model.addAttribute("expense", new ExpenseEntryPostDTO());
        model.addAttribute("appName", "Glitterfin v2");
        return "expense-form";
    }

    @PostMapping("/expenses")
    public String createExpense(@ModelAttribute ExpenseEntryPostDTO expenseEntryPostDTO) throws OwnerNotFoundException {
        expenseEntryService.saveExpense(expenseEntryPostDTO);
        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) throws ExpenseNotFoundException {
        ExpenseEntry expense = expenseEntryService.getExpenseById(id);
        ExpenseEntryPostDTO dto = ExpenseEntryMapper.toPostDto(expense);
        model.addAttribute("expense", dto);
        model.addAttribute("expenseId", id);
        model.addAttribute("appName", "Glitterfin v2");
        return "expense-form";
    }

    @PostMapping("/expenses/{id}")
    public String updateExpense(@PathVariable Integer id, @ModelAttribute ExpenseEntryPostDTO expenseEntryPostDTO) throws OwnerNotFoundException, ExpenseNotFoundException {
        expenseEntryService.updateExpense(id, expenseEntryPostDTO);
        return "redirect:/expenses";
    }

    @PostMapping("/expenses/{id}/delete")
    public String deleteExpense(@PathVariable Integer id) throws ExpenseNotFoundException {
        expenseEntryService.deleteExpense(id);
        return "redirect:/expenses";
    }
}
