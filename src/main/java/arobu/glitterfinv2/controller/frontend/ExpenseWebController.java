package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseWebController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseWebController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model) {
        List<ExpenseEntry> expenses = expenseEntryService.getExpensesForCurrentUser();
        double totalAmount = expenses.stream()
                .filter(expense -> expense.getAmount() != null)
                .mapToDouble(ExpenseEntry::getAmount)
                .sum();

        double averageAmount = expenses.isEmpty() ? 0 : totalAmount / expenses.size();

        model.addAttribute("expenses", expenses);
        model.addAttribute("pageTitle", "Expenses");
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("expenseCount", expenses.size());
        model.addAttribute("averageAmount", averageAmount);

        return "expenses/list";
    }

    @GetMapping("/{expenseId}/edit")
    public String editExpense(@PathVariable Integer expenseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            ExpenseEntry expense = expenseEntryService.getExpenseForCurrentUser(expenseId);
            model.addAttribute("expense", expense);
            model.addAttribute("expenseForm", ExpenseForm.fromEntity(expense));
            model.addAttribute("pageTitle", "Edit Expense");
            return "expenses/edit";
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
            return "redirect:/expenses";
        }
    }

    @PostMapping("/{expenseId}/edit")
    public String updateExpense(@PathVariable Integer expenseId,
                                @Valid @ModelAttribute("expenseForm") ExpenseForm form,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            ExpenseEntry expense = expenseEntryService.getExpenseForCurrentUser(expenseId);
            if (bindingResult.hasErrors()) {
                model.addAttribute("expense", expense);
                model.addAttribute("pageTitle", "Edit Expense");
                return "expenses/edit";
            }

            expenseEntryService.updateExpense(expenseId, form);
            redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
        }
        return "redirect:/expenses";
    }

    @PostMapping("/{expenseId}/delete")
    public String deleteExpense(@PathVariable Integer expenseId, RedirectAttributes redirectAttributes) {
        try {
            expenseEntryService.deleteExpense(expenseId);
            redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
        }
        return "redirect:/expenses";
    }
}

