package arobu.glitterfinv2.controller.frontend;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryQuickShowDTO;
import arobu.glitterfinv2.model.dto.LocationData;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MVC controller exposing a simple web interface for managing expense entries.
 */
@Controller
@RequestMapping("/expenses")
public class ExpenseEntryViewController {

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryViewController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public String listExpenses(Model model, Authentication authentication) {
        List<ExpenseEntryQuickShowDTO> expenses =
                expenseEntryService.getExpensesForUser(authentication.getName());
        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new ExpenseEntryPostDTO());
        return "expenses";
    }

    @PostMapping
    public String createExpense(@ModelAttribute("expense") ExpenseEntryPostDTO dto,
                                Authentication authentication) throws OwnerNotFoundException {
        expenseEntryService.saveExpense(dto, authentication.getName());
        return "redirect:/expenses";
    }

    @GetMapping("/{id}")
    public String editExpense(@PathVariable Integer id, Model model) {
        ExpenseEntry entry = expenseEntryService.getExpense(id);
        if (entry == null) {
            return "redirect:/expenses";
        }
        ExpenseEntryPostDTO dto = new ExpenseEntryPostDTO()
                .setAmount(entry.getAmount())
                .setTimestamp(entry.getTimestamp().toString())
                .setSource(entry.getSource())
                .setMerchant(entry.getMerchant());
        if (entry.getLocation() != null) {
            LocationData ld = new LocationData()
                    .setLatitude(String.valueOf(entry.getLocation().getLatitude()))
                    .setLongitude(String.valueOf(entry.getLocation().getLongitude()));
            dto.setLocationData(ld);
        }
        model.addAttribute("expense", dto);
        model.addAttribute("expenseId", id);
        return "expenses-edit";
    }

    @PostMapping("/{id}")
    public String updateExpense(@PathVariable Integer id,
                                @ModelAttribute("expense") ExpenseEntryPostDTO dto,
                                Authentication authentication) throws OwnerNotFoundException {
        expenseEntryService.updateExpense(id, dto, authentication.getName());
        return "redirect:/expenses";
    }

    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable Integer id) {
        expenseEntryService.deleteExpense(id);
        return "redirect:/expenses";
    }
}