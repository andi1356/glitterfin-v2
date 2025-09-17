package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/expenses")
public class ExpenseDashboardController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseDashboardController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model) {
        Optional<String> username = currentUsername();
        List<ExpenseEntry> expenses = username
                .map(expenseEntryService::getExpensesForUser)
                .orElse(Collections.emptyList());

        populateBaseModel(model, expenses);
        model.addAttribute("expenseForm", new ExpenseEntryForm());
        return "expenses/list";
    }

    @GetMapping("/{expenseId}/edit")
    public String editExpense(@PathVariable Integer expenseId, Model model, RedirectAttributes redirectAttributes) {
        Optional<String> username = currentUsername();
        if (username.isEmpty()) {
            return "redirect:/login";
        }

        ExpenseEntry expense;
        try {
            expense = expenseEntryService.getExpenseForUser(username.get(), expenseId);
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "We could not find that expense.");
            return "redirect:/expenses";
        }

        List<ExpenseEntry> expenses = expenseEntryService.getExpensesForUser(username.get());
        populateBaseModel(model, expenses);
        model.addAttribute("expenseForm", ExpenseEntryForm.fromEntity(expense));
        model.addAttribute("editing", true);
        model.addAttribute("editExpenseId", expenseId);
        return "expenses/list";
    }

    @PostMapping("/{expenseId}/edit")
    public String updateExpense(@PathVariable Integer expenseId,
                                @Valid @ModelAttribute("expenseForm") ExpenseEntryForm expenseForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Optional<String> username = currentUsername();
        if (username.isEmpty()) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            List<ExpenseEntry> expenses = expenseEntryService.getExpensesForUser(username.get());
            populateBaseModel(model, expenses);
            model.addAttribute("editing", true);
            model.addAttribute("editExpenseId", expenseId);
            return "expenses/list";
        }

        try {
            expenseEntryService.updateExpenseForUser(username.get(), expenseId, expense -> {
                expense.setAmount(expenseForm.getAmount());
                expense.setCategory(expenseForm.getCategory());
                expense.setMerchant(expenseForm.getMerchant());
                expense.setDescription(expenseForm.getDescription());
                expense.setSource(expenseForm.getSource());
                expense.setShared(Boolean.TRUE.equals(expenseForm.getShared()));
                expense.setOutlier(Boolean.TRUE.equals(expenseForm.getOutlier()));
            });
            redirectAttributes.addFlashAttribute("successMessage", "Expense updated successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "We could not find that expense.");
        }

        return "redirect:/expenses";
    }

    @PostMapping("/{expenseId}/delete")
    public String deleteExpense(@PathVariable Integer expenseId, RedirectAttributes redirectAttributes) {
        Optional<String> username = currentUsername();
        if (username.isEmpty()) {
            return "redirect:/login";
        }

        try {
            expenseEntryService.deleteExpenseForUser(username.get(), expenseId);
            redirectAttributes.addFlashAttribute("successMessage", "Expense deleted successfully.");
        } catch (ExpenseNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "We could not find that expense.");
        }

        return "redirect:/expenses";
    }

    private void populateBaseModel(Model model, List<ExpenseEntry> expenses) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", authentication);
        model.addAttribute("appName", "Glitterfin");
        model.addAttribute("expenses", expenses);
        model.addAttribute("hasExpenses", !expenses.isEmpty());
    }

    private Optional<String> currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return Optional.ofNullable(userDetails.getUsername());
        }

        if (principal instanceof String principalName && !"anonymousUser".equalsIgnoreCase(principalName)) {
            return Optional.of(principalName);
        }

        return Optional.empty();
    }
}
