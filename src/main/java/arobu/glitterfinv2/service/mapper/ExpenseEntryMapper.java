package arobu.glitterfinv2.service.mapper;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.dto.ExpenseFrontendDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;

import static java.time.ZonedDateTime.parse;
import static java.util.Objects.nonNull;

public class ExpenseEntryMapper {
    private static final String TEXT_DELIMITING_CHARACTERS = "||";
    private static final String REGEX_EXPRESSION = "\\|\\|";

    public static ExpenseEntry toEntity(final ExpenseEntryPostDTO dto, ExpenseOwner owner, Location location) {

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

    public static ExpenseFrontendDTO toFrontend(ExpenseEntry expense) {
        ExpenseFrontendDTO expenseFrontendDTO = new ExpenseFrontendDTO();

        return expenseFrontendDTO
                .setAmount(expense.getAmount())
                .setCategory(expense.getCategory())
                .setLocation(expense.getLocation().getDisplayName())
                .setMerchant(expense.getMerchant())
                .setTimestamp(expense.getTimestamp());
    }

    private String extractMerchant(final ExpenseEntryPostDTO dto) {
        String merchant = dto.getMerchant();
        if (merchant.contains(TEXT_DELIMITING_CHARACTERS)) {
            return merchant.split(REGEX_EXPRESSION)[0].trim();
        } else {
            return merchant.trim();
        }
    }

    private String extractDescription(final ExpenseEntryPostDTO dto) {
        String merchant = dto.getMerchant();
        if (merchant.contains(TEXT_DELIMITING_CHARACTERS)) {
            return merchant.split(REGEX_EXPRESSION)[1].trim();
        } else {
            return "";
        }
    }
}
