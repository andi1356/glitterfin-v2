package arobu.glitterfinv2.model.dto;

import java.time.ZonedDateTime;

/**
 * DTO used for displaying expense entries on the UI. Only exposes the
 * amount, timestamp, merchant and location fields along with the id for
 * further operations.
 */
public class ExpenseEntryQuickShowDTO {

    private Integer id;
    private Double amount;
    private ZonedDateTime timestamp;
    private String merchant;
    private String location;

    public Integer getId() {
        return id;
    }

    public ExpenseEntryQuickShowDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntryQuickShowDTO setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ExpenseEntryQuickShowDTO setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntryQuickShowDTO setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ExpenseEntryQuickShowDTO setLocation(String location) {
        this.location = location;
        return this;
    }
}

