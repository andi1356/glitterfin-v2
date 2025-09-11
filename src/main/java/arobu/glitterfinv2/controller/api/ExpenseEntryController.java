package arobu.glitterfinv2.controller.api;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.ExpenseNotFoundException;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import arobu.glitterfinv2.service.mapper.ExpenseEntryMapper;
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
    public List<ExpenseFrontendDTO> getExpenses() {
        return expenseEntryService.getExpensesForCurrentUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseFrontendDTO> getExpense(@PathVariable Integer id) {
        try {
            ExpenseEntry expense = expenseEntryService.getExpenseById(id);
            return ResponseEntity.ok(ExpenseEntryMapper.toFrontend(expense));
        } catch (ExpenseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> insertExpense(@RequestBody final ExpenseEntryPostDTO dto) {
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
    public ResponseEntity<Map<String, String>> updateExpense(@PathVariable Integer id, @RequestBody ExpenseEntryPostDTO dto) {
        Map<String, String> response = new HashMap<>();
        try {
            ExpenseEntry updated = expenseEntryService.updateExpense(id, dto);
            response.put("status", "success");
            response.put("expense", updated.toString());
            return ResponseEntity.ok(response);
        } catch (OwnerNotFoundException e) {
            LOGGER.error("Owner not found for expense {}", dto, e);
            response.put("status", "failure");
            response.put("reason", "Owner not found for expense " + dto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (ExpenseNotFoundException e) {
            LOGGER.error("Expense not found: {}", id, e);
            response.put("status", "failure");
            response.put("reason", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            expenseEntryService.deleteExpense(id);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (ExpenseNotFoundException e) {
            LOGGER.error("Expense not found: {}", id, e);
            response.put("status", "failure");
            response.put("reason", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
