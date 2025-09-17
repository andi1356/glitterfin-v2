package arobu.glitterfinv2.model.dto;

public class ExpenseEntryPostDTO {
    private Double amount;
    private String timestamp;
    private String source;
    private String merchant;

    private LocationData locationData = new LocationData();;
    private String category;
    private String receiptData;

    private Boolean shared;
    private Boolean outlier;

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntryPostDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ExpenseEntryPostDTO setTimestamp(String timestamp) {
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
