package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpenseEntryForm {

    private Integer id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;

    @Size(max = 60, message = "Category must be 60 characters or less")
    private String category;

    @Size(max = 120, message = "Merchant must be 120 characters or less")
    private String merchant;

    @Size(max = 255, message = "Description must be 255 characters or less")
    private String description;

    @Size(max = 60, message = "Source must be 60 characters or less")
    private String source;

    private Boolean shared = Boolean.FALSE;

    private Boolean outlier = Boolean.FALSE;

    public Integer getId() {
        return id;
    }

    public ExpenseEntryForm setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntryForm setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseEntryForm setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntryForm setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseEntryForm setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ExpenseEntryForm setSource(String source) {
        this.source = source;
        return this;
    }

    public Boolean getShared() {
        return shared;
    }

    public ExpenseEntryForm setShared(Boolean shared) {
        this.shared = shared;
        return this;
    }

    public Boolean getOutlier() {
        return outlier;
    }

    public ExpenseEntryForm setOutlier(Boolean outlier) {
        this.outlier = outlier;
        return this;
    }

    public static ExpenseEntryForm fromEntity(ExpenseEntry expenseEntry) {
        return new ExpenseEntryForm()
                .setId(expenseEntry.getId())
                .setAmount(expenseEntry.getAmount())
                .setCategory(expenseEntry.getCategory())
                .setMerchant(expenseEntry.getMerchant())
                .setDescription(expenseEntry.getDescription())
                .setSource(expenseEntry.getSource())
                .setShared(Boolean.TRUE.equals(expenseEntry.getShared()))
                .setOutlier(Boolean.TRUE.equals(expenseEntry.getOutlier()));
    }
}
