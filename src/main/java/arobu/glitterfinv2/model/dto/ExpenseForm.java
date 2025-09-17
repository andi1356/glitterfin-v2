package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpenseForm {

    private Integer id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid monetary value")
    private Double amount;

    @Size(max = 120, message = "Merchant name is too long")
    private String merchant;

    @Size(max = 120, message = "Source description is too long")
    private String source;

    @Size(max = 80, message = "Category name is too long")
    private String category;

    @Size(max = 500, message = "Description is too long")
    private String description;

    private Boolean shared;
    private Boolean outlier;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static ExpenseForm fromEntity(ExpenseEntry entry) {
        ExpenseForm form = new ExpenseForm();
        form.setId(entry.getId());
        form.setAmount(entry.getAmount());
        form.setMerchant(entry.getMerchant());
        form.setSource(entry.getSource());
        form.setCategory(entry.getCategory());
        form.setDescription(entry.getDescription());
        form.setShared(Boolean.TRUE.equals(entry.getShared()));
        form.setOutlier(Boolean.TRUE.equals(entry.getOutlier()));
        return form;
    }
}

