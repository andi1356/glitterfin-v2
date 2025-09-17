package arobu.glitterfinv2.model.dto;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ExpenseFormData {
    private Integer id;
    private Double amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime occurredAt;

    private String source;
    private String merchant;
    private String category;
    private String description;
    private boolean shared;
    private boolean outlier;

    public Integer getId() {
        return id;
    }

    public ExpenseFormData setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseFormData setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public ExpenseFormData setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
        return this;
    }

    public String getSource() {
        return source;
    }

    public ExpenseFormData setSource(String source) {
        this.source = source;
        return this;
    }

    public String getMerchant() {
        return merchant;
    }

    public ExpenseFormData setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ExpenseFormData setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseFormData setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isShared() {
        return shared;
    }

    public ExpenseFormData setShared(boolean shared) {
        this.shared = shared;
        return this;
    }

    public boolean isOutlier() {
        return outlier;
    }

    public ExpenseFormData setOutlier(boolean outlier) {
        this.outlier = outlier;
        return this;
    }

    public static ExpenseFormData fromEntity(ExpenseEntry entry) {
        ExpenseFormData formData = new ExpenseFormData()
                .setId(entry.getId())
                .setAmount(entry.getAmount())
                .setSource(entry.getSource())
                .setMerchant(entry.getMerchant())
                .setCategory(entry.getCategory())
                .setDescription(entry.getDescription())
                .setShared(Boolean.TRUE.equals(entry.getShared()))
                .setOutlier(Boolean.TRUE.equals(entry.getOutlier()));

        ZonedDateTime timestamp = entry.getTimestamp();
        if (timestamp != null) {
            ZoneId zoneId = timestamp.getZone();
            if (entry.getTimezone() != null) {
                try {
                    zoneId = ZoneId.of(entry.getTimezone());
                } catch (DateTimeException ignored) {
                    zoneId = timestamp.getZone();
                }
            }
            formData.setOccurredAt(timestamp.withZoneSameInstant(zoneId).toLocalDateTime());
        }

        return formData;
    }
}
