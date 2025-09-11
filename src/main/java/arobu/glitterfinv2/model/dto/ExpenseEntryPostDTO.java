package arobu.glitterfinv2.model.dto;

public class ExpenseEntryPostDTO {
    private Double amount;
    private String timestamp;
    private String source;
    private String merchant;

    private LocationData locationData;
    private String category;
    private String receiptData;

    private Boolean shared;
    private Boolean outlier;

    public Double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getMerchant() {
        return merchant;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public String getCategory() {
        return category;
    }

    public String getReceiptData() {
        return receiptData;
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

    public ExpenseEntryPostDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseEntryPostDTO setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ExpenseEntryPostDTO setSource(String source) {
        this.source = source;
        return this;
    }

    public ExpenseEntryPostDTO setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public ExpenseEntryPostDTO setLocationData(LocationData locationData) {
        this.locationData = locationData;
        return this;
    }

    public ExpenseEntryPostDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public ExpenseEntryPostDTO setReceiptData(String receiptData) {
        this.receiptData = receiptData;
        return this;
    }
}
