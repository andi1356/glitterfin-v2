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
}
