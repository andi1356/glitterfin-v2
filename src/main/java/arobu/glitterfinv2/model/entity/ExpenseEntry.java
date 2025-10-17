package arobu.glitterfinv2.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "expense")
public class ExpenseEntry {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private ExpenseOwner owner;
    private BigDecimal amount;
    private ZonedDateTime timestamp;
    private String timezone;
    private String source;
    private String merchant;

    @ManyToOne
    private Location location;
    private String category;
    private String receiptData;
    private String description;
    private String details;

    private Boolean shared = false;
    private Boolean outlier = false;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public ExpenseEntry setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getTimezone() {
        return timezone;
    }

    public ExpenseEntry setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ExpenseEntry setTimezone(String timezone) {
        this.timezone = timezone;
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

    @Override
    public String toString() {
        return "ExpenseEntry{" +
                "id=" + id +
                ", owner=" + owner +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExpenseEntry that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && Objects.equals(amount, that.amount) && Objects.equals(timestamp, that.timestamp) && Objects.equals(timezone, that.timezone) && Objects.equals(source, that.source) && Objects.equals(merchant, that.merchant) && Objects.equals(location, that.location) && Objects.equals(category, that.category) && Objects.equals(receiptData, that.receiptData) && Objects.equals(description, that.description) && Objects.equals(details, that.details) && Objects.equals(shared, that.shared) && Objects.equals(outlier, that.outlier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, amount, timestamp, timezone, source, merchant, location, category, receiptData, description, details, shared, outlier);
    }
}
