package arobu.glitterfinv2.service.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Integer expenseId) {
        super("Expense with id " + expenseId + " was not found for the current user.");
    }
}

