package arobu.glitterfinv2.model.dto;

public class ExpenseFrontendDTO {
    private Double amount;
    private String timestamp;
    private String merchant;

    private String location;
    private String category;

    public ExpenseFrontendDTO() {
    }

    public ExpenseFrontendDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseFrontendDTO setTimestamp(String timestamp) {
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
