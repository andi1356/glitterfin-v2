package arobu.glitterfinv2.service.exception;

public class ExpenseNotFoundException extends Exception {
    public ExpenseNotFoundException(Integer id) {
        super("Expense with id " + id + " not found");
    }
}
