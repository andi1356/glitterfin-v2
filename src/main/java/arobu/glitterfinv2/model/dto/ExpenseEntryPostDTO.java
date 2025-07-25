package arobu.glitterfinv2.model.dto;

import java.time.ZonedDateTime;

public class ExpenseEntryPostDTO {
    private String ownerId;
    private Double amount;
    private ZonedDateTime timestamp;
    private String source;
    private String merchant;

    private LocationData locationData;
    private String category;
    private String receiptData;

    private Boolean shared;
    private Boolean outlier;

    public String getOwnerId() {
        return ownerId;
    }

    public ExpenseEntryPostDTO setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntryPostDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ExpenseEntryPostDTO setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ExpenseEntryPostDTO setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntryPostDTO setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public ExpenseEntryPostDTO setLocationData(LocationData locationData) {
        this.locationData = locationData;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseEntryPostDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getReceiptData() {
        return receiptData;
    }

    public ExpenseEntryPostDTO setReceiptData(String receiptData) {
        this.receiptData = receiptData;
        return this;
    }

    public Boolean getShared() {
        return shared;
    }

    public ExpenseEntryPostDTO setShared(Boolean shared) {
        this.shared = shared;
        return this;
    }

    public Boolean getOutlier() {
        return outlier;
    }

    public ExpenseEntryPostDTO setOutlier(Boolean outlier) {
        this.outlier = outlier;
        return this;
    }
}
