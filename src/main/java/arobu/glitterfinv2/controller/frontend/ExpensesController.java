package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryUpdateDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {

    private final ExpenseEntryService expenseEntryService;

    public ExpensesController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model) {
        List<ExpenseEntry> expenses = expenseEntryService.getExpensesForAuthenticatedUser();
        model.addAttribute("appName", "Glitterfin");
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpenses", expenses.size());
        double totalAmount = expenses.stream()
                .map(ExpenseEntry::getAmount)
                .filter(amount -> amount != null)
                .mapToDouble(Double::doubleValue)
                .sum();
        model.addAttribute("totalAmount", totalAmount);
        return "expenses/list";
    }

    @GetMapping("/{id}/edit")
    public String editExpense(@PathVariable Integer id, Model model) {
        ExpenseEntry expense = expenseEntryService.getExpenseForAuthenticatedUser(id);
        model.addAttribute("appName", "Glitterfin");
        model.addAttribute("expense", expense);
        model.addAttribute("expenseForm", ExpenseEntryUpdateDTO.fromEntity(expense));
        return "expenses/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateExpense(@PathVariable Integer id,
                                @ModelAttribute("expenseForm") ExpenseEntryUpdateDTO expenseForm,
                                RedirectAttributes redirectAttributes) {
        expenseEntryService.updateExpense(id, expenseForm);
        redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully");
        return "redirect:/expenses";
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        expenseEntryService.deleteExpense(id);
        redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully");
        return "redirect:/expenses";
    }
}
