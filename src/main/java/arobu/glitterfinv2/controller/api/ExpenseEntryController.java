package arobu.glitterfinv2.controller.api;

import arobu.glitterfinv2.model.dto.ExpenseEntryCrudDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseEntryController {

    Logger LOGGER = LogManager.getLogger(ExpenseEntryController.class);

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @GetMapping
    public List<ExpenseEntry> allExpenses() {
        return expenseEntryService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ExpenseEntryCrudDTO getExpense(@PathVariable Integer id) {
        return expenseEntryService.getExpense(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> insertExpense(@RequestBody final ExpenseEntryCrudDTO dto) {
        Map<String, String> response = new HashMap<>();
        ExpenseEntry savedEntity;
        try {
            savedEntity = expenseEntryService.saveExpense(dto);
        } catch (OwnerNotFoundException e) {
            LOGGER.error("Owner not found for expense {}", dto, e);
            response.put("status", "failure");
            response.put("reason", "Owner not found for expense " + dto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("status", "success");
        response.put("expense", savedEntity.toString());

        LOGGER.info(response.toString());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ExpenseEntry updateExpense(@PathVariable Integer id, @RequestBody ExpenseEntryCrudDTO dto) throws OwnerNotFoundException {
        dto.setId(id);
        return expenseEntryService.saveExpense(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer id) {
        expenseEntryService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
