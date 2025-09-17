package arobu.glitterfinv2.service.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Integer id) {
        super("Expense not found for id: " + id);
    }
}
