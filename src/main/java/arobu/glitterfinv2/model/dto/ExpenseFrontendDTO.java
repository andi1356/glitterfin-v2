package arobu.glitterfinv2.model.dto;

import java.time.ZonedDateTime;

public class ExpenseFrontendDTO {
    private Double amount;
    private ZonedDateTime timestamp;
    private String merchant;

    private String location;
    private String category;

    public ExpenseFrontendDTO() {
    }

    public Double getAmount() {
        return amount;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseFrontendDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseFrontendDTO setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ExpenseFrontendDTO setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public ExpenseFrontendDTO setLocation(String location) {
        this.location = location;
        return this;
    }

    public ExpenseFrontendDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public String toString() {
        return  "amount=" + amount +
                ", timestamp='" + timestamp + '\'' +
                ", merchant='" + merchant + '\'' +
                ", location='" + location + '\'' +
                ", category='" + category + '\'';
    }
}
