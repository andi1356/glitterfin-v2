package arobu.glitterfinv2.model.mapper;

import arobu.glitterfinv2.model.dto.ExpenseEntryApiPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseEntryUpdateForm;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.ZonedDateTime.parse;
import static java.util.Objects.nonNull;

public class ExpenseEntryMapper {
    private static final String TEXT_DELIMITING_CHARACTERS = "||";
    private static final String REGEX_EXPRESSION = "\\|\\|";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    public static ExpenseEntryUpdateForm toExpenseEntryUpdateForm(ExpenseEntry expenseEntry) {
        ExpenseEntryUpdateForm form = new ExpenseEntryUpdateForm();
        form.setDescription(expenseEntry.getDescription());
        form.setCategory(expenseEntry.getCategory());
        form.setMerchant(expenseEntry.getMerchant());
        form.setAmount(expenseEntry.getAmount());

        if (expenseEntry.getTimestamp() != null) {
            ZonedDateTime timestamp = expenseEntry.getTimestamp();
            ZoneId zoneId = ZoneId.of(expenseEntry.getTimezone());
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

    public static ExpenseEntry toEntity(final ExpenseEntryApiPostDTO dto, ExpenseOwner owner, Location location) {

        ExpenseEntry expense = new ExpenseEntry();

        expense
                .setOwner(owner)
                .setAmount(dto.getAmount())
                .setTimestamp(parse(dto.getTimestamp()))
                .setTimezone(parse(dto.getTimestamp()).getZone().getId())
                .setSource(dto.getSource())
                .setMerchant(dto.getMerchant())

                .setLocation(location)
//                .setCategory(dto.getCategory()) // parse category from DTO ||
                .setReceiptData(dto.getReceiptData())
//                .setDescription()  // parse from merchant
                .setDetails(dto.getMerchant());

        if (nonNull(dto.getShared())) {
            expense.setShared(dto.getShared());
        }
        if (nonNull(dto.getOutlier())) {
            expense.setOutlier(dto.getOutlier());
        }

        return expense;
    }

    private String extractMerchant(final ExpenseEntryApiPostDTO dto) {
        String merchant = dto.getMerchant();
        if (merchant.contains(TEXT_DELIMITING_CHARACTERS)) {
            return merchant.split(REGEX_EXPRESSION)[0].trim();
        } else {
            return merchant.trim();
        }
    }

    private String extractDescription(final ExpenseEntryApiPostDTO dto) {
        String merchant = dto.getMerchant();
        if (merchant.contains(TEXT_DELIMITING_CHARACTERS)) {
            return merchant.split(REGEX_EXPRESSION)[1].trim();
        } else {
            return "";
        }
    }
}
