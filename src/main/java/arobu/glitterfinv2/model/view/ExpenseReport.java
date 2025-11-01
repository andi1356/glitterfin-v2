package arobu.glitterfinv2.model.view;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseReport(
        PeriodSummary lastSevenDays,
        PeriodSummary lastThirtyDays,
        List<CategorySummary> topCategories
) {
    public record PeriodSummary(
            BigDecimal totalAmount,
            int expenseCount,
            BigDecimal averagePerExpense,
            BigDecimal averagePerDay
    ) {
    }

    public record CategorySummary(
            String category,
            BigDecimal totalAmount,
            int expenseCount,
            BigDecimal percentageOfTotal
    ) {
    }
}
