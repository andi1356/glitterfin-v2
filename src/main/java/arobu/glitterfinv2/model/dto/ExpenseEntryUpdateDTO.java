package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;

public class ExpenseEntryUpdateDTO {
    private Integer id;
    private Double amount;
    private String category;
    private String merchant;
    private String source;
    private String description;
    private String details;
    private Boolean shared;
    private Boolean outlier;

    public static ExpenseEntryUpdateDTO fromEntity(ExpenseEntry expense) {
        ExpenseEntryUpdateDTO dto = new ExpenseEntryUpdateDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setMerchant(expense.getMerchant());
        dto.setSource(expense.getSource());
        dto.setDescription(expense.getDescription());
        dto.setDetails(expense.getDetails());
        dto.setShared(expense.getShared());
        dto.setOutlier(expense.getOutlier());
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getOutlier() {
        return outlier;
    }

    public void setOutlier(Boolean outlier) {
        this.outlier = outlier;
    }
}
