package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseFormData;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseViewController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseViewController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model, Authentication authentication) {
        String username = resolveUsername(authentication);
        List<ExpenseEntry> expenses = expenseEntryService.getExpensesForOwner(username);

        DoubleSummaryStatistics statistics = expenses.stream()
                .filter(expense -> expense.getAmount() != null)
                .mapToDouble(ExpenseEntry::getAmount)
                .summaryStatistics();

        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseCount", expenses.size());
        model.addAttribute("totalAmount", statistics.getSum());
        model.addAttribute("averageAmount", statistics.getCount() > 0 ? statistics.getAverage() : 0);
        model.addAttribute("username", username);

        return "expenses/list";
    }

    @GetMapping("/{id}/edit")
    public String editExpense(@PathVariable Integer id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = resolveUsername(authentication);
        try {
            ExpenseEntry expenseEntry = expenseEntryService.getExpenseForOwner(id, username);
            model.addAttribute("expense", expenseEntry);
            model.addAttribute("expenseForm", ExpenseFormData.fromEntity(expenseEntry));
            return "expenses/edit";
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
            return "redirect:/expenses";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateExpense(@PathVariable Integer id,
                                @Valid @ModelAttribute("expenseForm") ExpenseFormData expenseForm,
                                BindingResult bindingResult,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        String username = resolveUsername(authentication);

        if (expenseForm.getAmount() == null) {
            bindingResult.rejectValue("amount", "amount.required", "Please provide an amount.");
        }

        ExpenseEntry expenseEntry;
        try {
            expenseEntry = expenseEntryService.getExpenseForOwner(id, username);
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
            return "redirect:/expenses";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("expense", expenseEntry);
            return "expenses/edit";
        }

        try {
            expenseEntryService.updateExpense(id, expenseForm, username);
            redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
        }
        return "redirect:/expenses";
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable Integer id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = resolveUsername(authentication);
        try {
            expenseEntryService.deleteExpense(id, username);
            redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The requested expense could not be found.");
        }
        return "redirect:/expenses";
    }

    private String resolveUsername(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("No authenticated user available");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return String.valueOf(principal);
    }
}
