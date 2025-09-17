package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
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
public class ExpenseViewController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseViewController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model) {
        List<ExpenseEntry> expenses = expenseEntryService.getExpensesForCurrentUser();
        model.addAttribute("expenses", expenses);
        return "expenses/list";
    }

    @GetMapping("/{expenseId}/edit")
    public String editExpense(@PathVariable Integer expenseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            ExpenseEntry expenseEntry = expenseEntryService.getExpenseForCurrentUser(expenseId);
            model.addAttribute("expense", expenseEntry);
            model.addAttribute("expenseForm", ExpenseEntryForm.fromEntity(expenseEntry));
            return "expenses/edit";
        } catch (ExpenseNotFoundException exception) {
            redirectAttributes.addFlashAttribute("feedbackType", "error");
            redirectAttributes.addFlashAttribute("feedbackMessage", "We couldn't find the requested expense.");
            return "redirect:/expenses";
        }
    }

    @PostMapping("/{expenseId}/edit")
    public String updateExpense(@PathVariable Integer expenseId,
                                @ModelAttribute("expenseForm") ExpenseEntryForm expenseForm,
                                RedirectAttributes redirectAttributes) {
        try {
            expenseEntryService.updateExpense(expenseId, expenseForm);
            redirectAttributes.addFlashAttribute("feedbackType", "success");
            redirectAttributes.addFlashAttribute("feedbackMessage", "Expense updated successfully.");
        } catch (ExpenseNotFoundException exception) {
            redirectAttributes.addFlashAttribute("feedbackType", "error");
            redirectAttributes.addFlashAttribute("feedbackMessage", "We couldn't find the requested expense.");
        }
        return "redirect:/expenses";
    }

    @PostMapping("/{expenseId}/delete")
    public String deleteExpense(@PathVariable Integer expenseId, RedirectAttributes redirectAttributes) {
        try {
            expenseEntryService.deleteExpense(expenseId);
            redirectAttributes.addFlashAttribute("feedbackType", "success");
            redirectAttributes.addFlashAttribute("feedbackMessage", "Expense deleted successfully.");
        } catch (ExpenseNotFoundException exception) {
            redirectAttributes.addFlashAttribute("feedbackType", "error");
            redirectAttributes.addFlashAttribute("feedbackMessage", "We couldn't find the requested expense.");
        }
        return "redirect:/expenses";
    }
}
