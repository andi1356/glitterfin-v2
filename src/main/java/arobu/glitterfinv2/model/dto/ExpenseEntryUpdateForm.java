package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ExpenseEntryUpdateForm {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private String description;
    private String category;
    private String merchant;
    private BigDecimal amount;
    private String timestamp;
    private String timezone;
    private String source;
    private String receiptData;
    private String details;
    private Boolean shared;
    private Boolean outlier;

    public static ExpenseEntryUpdateForm fromExpenseEntry(ExpenseEntry expenseEntry) {
        ExpenseEntryUpdateForm form = new ExpenseEntryUpdateForm();
        form.setDescription(expenseEntry.getDescription());
        form.setCategory(expenseEntry.getCategory());
        form.setMerchant(expenseEntry.getMerchant());
        form.setAmount(expenseEntry.getAmount());

        if (expenseEntry.getTimestamp() != null) {
            ZonedDateTime timestamp = expenseEntry.getTimestamp();
            ZoneId zoneId = resolveZone(expenseEntry);
            form.setTimestamp(timestamp.withZoneSameInstant(zoneId).format(TIMESTAMP_FORMATTER));
            form.setTimezone(zoneId.getId());
        } else {
            form.setTimezone(expenseEntry.getTimezone());
        }

        form.setSource(expenseEntry.getSource());
        form.setReceiptData(expenseEntry.getReceiptData());
        form.setDetails(expenseEntry.getDetails());
        form.setShared(expenseEntry.getShared());
        form.setOutlier(expenseEntry.getOutlier());

        return form;
    }

    private static ZoneId resolveZone(ExpenseEntry expenseEntry) {
        if (expenseEntry.getTimezone() != null && !expenseEntry.getTimezone().isBlank()) {
            try {
                return ZoneId.of(expenseEntry.getTimezone());
            } catch (DateTimeException ignored) {
                // fall through to the timestamp zone
            }
        }

        return expenseEntry.getTimestamp().getZone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReceiptData() {
        return receiptData;
    }

    public void setReceiptData(String receiptData) {
        this.receiptData = receiptData;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getOutlier() {
        return outlier;
    }

    public void setOutlier(Boolean outlier) {
        this.outlier = outlier;
    }
}
