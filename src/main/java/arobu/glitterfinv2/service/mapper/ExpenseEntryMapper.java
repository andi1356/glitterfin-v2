package arobu.glitterfinv2.service.mapper;

import arobu.glitterfinv2.model.dto.ExpenseEntryPostDTO;
import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.ExpenseOwner;
import arobu.glitterfinv2.model.entity.Location;

public class ExpenseEntryMapper {
    private static final String TEXT_DELIMITING_CHARACTERS = "||";
    private static final String REGEX_EXPRESSION = "\\|\\|";

    public static ExpenseEntry toEntity(final ExpenseEntryPostDTO dto, ExpenseOwner owner, Location location) {

        ExpenseEntry expense = new ExpenseEntry();

        expense
                .setOwner(owner)
                .setAmount(dto.getAmount())
                .setTimestamp(dto.getTimestamp())
                .setSource(dto.getSource())
                .setMerchant(dto.getMerchant())

                .setLocation(location)
//                .setCategory(dto.getCategory()) // parse category from DTO ||
                .setReceiptData(dto.getReceiptData())
//                .setDescription()
                .setDetails(dto.getReceiptData())

                .setShared(dto.getShared())
                .setShared(dto.getOutlier());

        return expense;
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
