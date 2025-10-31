package arobu.glitterfinv2.controller.api;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.service.ExpenseEntryService;
import arobu.glitterfinv2.service.exception.OwnerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseEntryController {

    Logger LOGGER = LogManager.getLogger(ExpenseEntryController.class);

    private final ExpenseEntryService expenseEntryService;

    public ExpenseEntryController(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> insertExpense(
            @AuthenticationPrincipal ExpenseOwner expenseOwner,
            @RequestBody final ExpenseEntryApiPostDTO dto) {

        Map<String, String> response = new HashMap<>();
        ExpenseEntry savedEntity;
        try {
            savedEntity = expenseEntryService.saveExpense(dto, expenseOwner);
        } catch (OwnerNotFoundException e) {
            LOGGER.error("Owner not found for expense {}",dto, e);
            response.put("status", "failure");
            response.put("reason", "Owner not found for expense " + dto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("status", "success");
        response.put("expense", savedEntity.toString());

        LOGGER.info("Successfully persisted entity with id: {}", savedEntity.getId());

        return ResponseEntity.ok(response);
    }
}
