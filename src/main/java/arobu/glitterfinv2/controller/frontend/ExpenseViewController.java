package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryUpdateForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/expenses")
public class ExpenseViewController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseViewController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String expenses(Model model, Authentication authentication) {
        boolean isAuthenticated = isUserAuthenticated(authentication);

        List<ExpenseEntry> expenses = isAuthenticated
                ? expenseEntryService.getExpensesForUser(authentication.getName())
                : Collections.emptyList();

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("expenses", expenses);

        return "expenses";
    }

    @GetMapping("/{id}")
    public String viewExpense(@PathVariable("id") Integer expenseId,
                              Model model,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (!isUserAuthenticated(authentication)) {
            return "redirect:/login";
        }

        Optional<ExpenseEntry> expenseEntry = expenseEntryService.getExpenseForUser(expenseId, authentication.getName());

        if (expenseEntry.isEmpty()) {
            redirectAttributes.addFlashAttribute("expenseMessage", "Unable to locate the requested expense.");
            return "redirect:/expenses";
        }

        model.addAttribute("expense", expenseEntry.get());

        return "expense-detail";
    }

    @GetMapping("/{id}/edit")
    public String editExpense(@PathVariable("id") Integer expenseId,
                              Model model,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        if (!isUserAuthenticated(authentication)) {
            return "redirect:/login";
        }

        Optional<ExpenseEntry> expenseEntry = expenseEntryService.getExpenseForUser(expenseId, authentication.getName());

        if (expenseEntry.isEmpty()) {
            redirectAttributes.addFlashAttribute("expenseMessage", "Unable to locate the requested expense.");
            return "redirect:/expenses";
        }

        model.addAttribute("expense", expenseEntry.get());
        model.addAttribute("expenseForm", ExpenseEntryUpdateForm.fromExpenseEntry(expenseEntry.get()));

        return "expense-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateExpense(@PathVariable("id") Integer expenseId,
                                @ModelAttribute("expenseForm") ExpenseEntryUpdateForm expenseForm,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        if (!isUserAuthenticated(authentication)) {
            return "redirect:/login";
        }

        boolean updated = expenseEntryService
                .updateExpenseForUser(expenseId, authentication.getName(), expenseForm)
                .isPresent();

        String message = updated
                ? "Expense updated successfully."
                : "Unable to update the expense. Please try again.";
        redirectAttributes.addFlashAttribute("expenseMessage", message);

        return "redirect:/expenses";
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable("id") Integer expenseId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        if (!isUserAuthenticated(authentication)) {
            return "redirect:/login";
        }

        boolean deleted = expenseEntryService.deleteExpenseForUser(expenseId, authentication.getName());
        String message = deleted
                ? "Expense deleted successfully."
                : "Unable to delete the expense. It may have already been removed.";

        redirectAttributes.addFlashAttribute("expenseMessage", message);
        return "redirect:/expenses";
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
