package arobu.glitterfinv2.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.ZonedDateTime;

@Entity(name = "expense")
public class ExpenseEntry {
    @Id
    Integer id;

    @ManyToOne
    private ExpenseOwner owner;
    private Double amount;
    private ZonedDateTime timestamp;
    private String source;
    private String merchant;

    @ManyToOne
    private Location location;
    private String category;
    private String receiptData;
    private String description;
    private String details;

    private Boolean shared;
    private Boolean outlier;

    public Integer getId() {
        return id;
    }

    public ExpenseEntry setId(Integer id) {
        this.id = id;
        return this;
    }

    public ExpenseOwner getOwner() {
        return owner;
    }

    public ExpenseEntry setOwner(ExpenseOwner owner) {
        this.owner = owner;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseEntry setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ExpenseEntry setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ExpenseEntry setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseEntry setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public ExpenseEntry setLocation(Location location) {
        this.location = location;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseEntry setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getReceiptData() {
        return receiptData;
    }

    public ExpenseEntry setReceiptData(String receiptData) {
        this.receiptData = receiptData;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseEntry setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public ExpenseEntry setDetails(String details) {
        this.details = details;
        return this;
    }

    public Boolean getShared() {
        return shared;
    }

    public ExpenseEntry setShared(Boolean shared) {
        this.shared = shared;
        return this;
    }

    public Boolean getOutlier() {
        return outlier;
    }

    public ExpenseEntry setOutlier(Boolean outlier) {
        this.outlier = outlier;
        return this;
    }
}
