package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;

public class ExpenseEntryUpdateForm {

    private String description;
    private String category;
    private String merchant;
    private Double amount;

    public static ExpenseEntryUpdateForm fromExpenseEntry(ExpenseEntry expenseEntry) {
        ExpenseEntryUpdateForm form = new ExpenseEntryUpdateForm();
        form.setDescription(expenseEntry.getDescription());
        form.setCategory(expenseEntry.getCategory());
        form.setMerchant(expenseEntry.getMerchant());
        form.setAmount(expenseEntry.getAmount());
        return form;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
