package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;

public class ExpenseEntryForm {

    private Integer id;
    private Double amount;
    private String source;
    private String merchant;
    private String category;
    private String description;
    private String details;
    private Boolean shared;
    private Boolean outlier;

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

    public String getSource() {
        return source;
    }

    public ExpenseEntryForm setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntryForm setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseEntryForm setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseEntryForm setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public ExpenseEntryForm setDetails(String details) {
        this.details = details;
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
                .setSource(expenseEntry.getSource())
                .setMerchant(expenseEntry.getMerchant())
                .setCategory(expenseEntry.getCategory())
                .setDescription(expenseEntry.getDescription())
                .setDetails(expenseEntry.getDetails())
                .setShared(expenseEntry.getShared())
                .setOutlier(expenseEntry.getOutlier());
    }
}
