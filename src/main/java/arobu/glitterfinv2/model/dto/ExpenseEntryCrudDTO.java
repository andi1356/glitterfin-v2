package arobu.glitterfinv2.model.dto;

public class ExpenseEntryCrudDTO {
    private Integer id;
    private Double amount;
    private String timestamp;
    private String source;
    private String merchant;
    private String category;
    private String latitude;
    private String longitude;

    public Integer getId() {
        return id;
    }

    public ExpenseEntryCrudDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntryCrudDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public ExpenseEntryCrudDTO setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ExpenseEntryCrudDTO setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntryCrudDTO setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseEntryCrudDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public ExpenseEntryCrudDTO setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public ExpenseEntryCrudDTO setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
}
