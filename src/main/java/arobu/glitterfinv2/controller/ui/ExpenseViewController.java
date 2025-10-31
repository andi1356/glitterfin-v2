package arobu.glitterfinv2.controller.ui;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.form.ExpenseEntryForm;
import arobu.glitterfinv2.model.mapper.ExpenseEntryMapper;
import arobu.glitterfinv2.service.ExpenseEntryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.ZonedDateTime;
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
    public String listExpensesUI(@AuthenticationPrincipal Owner owner, Model model) {
        List<ExpenseEntry> expenses = expenseEntryService.getAllExpenses(owner);

        model.addAttribute("expenses", expenses);

        return "expenses";
    }

    @GetMapping("/new")
    public String newExpenseUI(Model model) {
        ExpenseEntryForm form = new ExpenseEntryForm();
        form.setTimezone(ZonedDateTime.now().getOffset().getId());

        model.addAttribute("expenseForm", form);
        model.addAttribute("isEdit", false);

        return "forms/expense-form";
    }

    @GetMapping("/{id}")
    public String viewExpenseUI(@PathVariable("id") Integer expenseId,
                                @AuthenticationPrincipal Owner owner,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        Optional<ExpenseEntry> expenseEntry = expenseEntryService.getExpense(expenseId, owner);

        if (expenseEntry.isEmpty()) {
            redirectAttributes.addFlashAttribute("expenseMessage", "Unable to locate the requested expense.");
            return "redirect:/expenses";
        }

        model.addAttribute("expense", expenseEntry.get());

        return "expense-detail";
    }

    @GetMapping("/{id}/edit")
    public String editExpenseUI(@PathVariable("id") Integer expenseId,
                                @AuthenticationPrincipal Owner owner,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        Optional<ExpenseEntry> expenseEntry = expenseEntryService.getExpense(expenseId, owner);

        if (expenseEntry.isEmpty()) {
            redirectAttributes.addFlashAttribute("expenseMessage", "Unable to locate the requested expense.");
            return "redirect:/expenses";
        }

        model.addAttribute("expense", expenseEntry.get());
        model.addAttribute("expenseForm", ExpenseEntryMapper.toExpenseEntryForm(expenseEntry.get()));
        model.addAttribute("isEdit", true);

        return "forms/expense-form";
    }

    @PostMapping
    public String createExpense(@ModelAttribute("expenseForm") ExpenseEntryForm expenseForm,
                                @AuthenticationPrincipal Owner owner,
                                RedirectAttributes redirectAttributes) {

        boolean created = expenseEntryService
                .createExpense(owner, expenseForm)
                .isPresent();

        String message = created
                ? "Expense added successfully."
                : "Unable to add the expense. Please verify the details and try again.";
        redirectAttributes.addFlashAttribute("expenseMessage", message);

        return "redirect:/expenses";
    }

    @PostMapping("/{id}/edit")
    public String updateExpense(@PathVariable("id") Integer expenseId,
                                @AuthenticationPrincipal Owner owner,
                                @ModelAttribute("expenseForm") ExpenseEntryForm expenseForm,
                                RedirectAttributes redirectAttributes) {

        boolean updated = expenseEntryService
                .updateExpense(expenseId, owner, expenseForm)
                .isPresent();

        String message = updated
                ? "Expense updated successfully."
                : "Unable to update the expense. Please try again.";
        redirectAttributes.addFlashAttribute("expenseMessage", message);

        return "redirect:/expenses/" + expenseId;
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable("id") Integer expenseId,
                                @AuthenticationPrincipal Owner owner,
                                RedirectAttributes redirectAttributes) {

        boolean deleted = expenseEntryService.deleteExpense(expenseId, owner);
        String message = deleted
                ? "Expense deleted successfully."
                : "Unable to delete the expense.";

        redirectAttributes.addFlashAttribute("expenseMessage", message);
        return "redirect:/expenses";
    }
}
