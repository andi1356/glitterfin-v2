package arobu.glitterfinv2.model.entity.meta;

public enum ExpenseField {
    ID("id"),
    OWNER("owner"),
    AMOUNT("amount"),
    TIMESTAMP("timestamp"),
    TIMEZONE("timezone"),
    SOURCE("source"),
    MERCHANT("merchant"),
    LOCATION("location"),
    CATEGORY("category"),
    RECEIPT_DATA("receiptData"),
    DESCRIPTION("description"),
    DETAILS("details"),
    IS_SHARED("shared"),
    IS_OUTLIER("outlier");

    private final String propertyName;

    ExpenseField(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
