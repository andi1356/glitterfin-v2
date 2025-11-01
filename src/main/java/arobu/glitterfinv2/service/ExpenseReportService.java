package arobu.glitterfinv2.service;

import arobu.glitterfinv2.model.entity.ExpenseEntry;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.view.ExpenseReport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseReportService {

    private static final int LAST_WEEK_DAYS = 7;
    private static final int LAST_MONTH_DAYS = 30;

    private final ExpenseEntryService expenseEntryService;

    public ExpenseReportService(ExpenseEntryService expenseEntryService) {
        this.expenseEntryService = expenseEntryService;
    }

    public ExpenseReport getExpenseReport(Owner owner) {
        List<ExpenseEntry> expenses = expenseEntryService.getAllExpenses(owner);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime lastWeek = now.minusDays(LAST_WEEK_DAYS);
        ZonedDateTime lastMonth = now.minusDays(LAST_MONTH_DAYS);

        List<ExpenseEntry> lastWeekExpenses = expenses.stream()
                .filter(expense -> hasTimestampOnOrAfter(expense, lastWeek))
                .toList();

        List<ExpenseEntry> lastMonthExpenses = expenses.stream()
                .filter(expense -> hasTimestampOnOrAfter(expense, lastMonth))
                .toList();

        ExpenseReport.PeriodSummary lastWeekSummary = buildPeriodSummary(lastWeekExpenses, LAST_WEEK_DAYS);
        ExpenseReport.PeriodSummary lastMonthSummary = buildPeriodSummary(lastMonthExpenses, LAST_MONTH_DAYS);
        List<ExpenseReport.CategorySummary> topCategories = buildCategorySummaries(lastMonthExpenses, lastMonthSummary.totalAmount());

        return new ExpenseReport(lastWeekSummary, lastMonthSummary, topCategories);
    }

    private static boolean hasTimestampOnOrAfter(ExpenseEntry expense, ZonedDateTime pointInTime) {
        return expense.getTimestamp() != null && !expense.getTimestamp().isBefore(pointInTime);
    }

    private static ExpenseReport.PeriodSummary buildPeriodSummary(List<ExpenseEntry> expenses, int daysInPeriod) {
        BigDecimal totalAmount = expenses.stream()
                .map(ExpenseEntry::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = (int) expenses.stream()
                .filter(expense -> expense.getAmount() != null)
                .count();

        BigDecimal averagePerExpense = count > 0
                ? totalAmount.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal averagePerDay = daysInPeriod > 0
                ? totalAmount.divide(BigDecimal.valueOf(daysInPeriod), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new ExpenseReport.PeriodSummary(totalAmount, count, averagePerExpense, averagePerDay);
    }

    private static List<ExpenseReport.CategorySummary> buildCategorySummaries(List<ExpenseEntry> expenses, BigDecimal periodTotal) {
        if (expenses.isEmpty()) {
            return List.of();
        }

        Map<String, List<ExpenseEntry>> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(expense -> {
                    String category = expense.getCategory();
                    return category == null || category.isBlank() ? "Uncategorized" : category;
                }));

        BigDecimal safePeriodTotal = periodTotal != null && periodTotal.compareTo(BigDecimal.ZERO) > 0
                ? periodTotal
                : BigDecimal.ONE;

        return expensesByCategory.entrySet().stream()
                .map(entry -> {
                    BigDecimal categoryTotal = entry.getValue().stream()
                            .map(ExpenseEntry::getAmount)
                            .filter(amount -> amount != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int count = (int) entry.getValue().stream()
                            .filter(expense -> expense.getAmount() != null)
                            .count();

                    BigDecimal percentage = categoryTotal.compareTo(BigDecimal.ZERO) > 0
                            ? categoryTotal
                            .multiply(BigDecimal.valueOf(100))
                            .divide(safePeriodTotal, 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return new ExpenseReport.CategorySummary(entry.getKey(), categoryTotal, count, percentage);
                })
                .sorted(Comparator.comparing(ExpenseReport.CategorySummary::totalAmount).reversed())
                .limit(5)
                .toList();
    }
}
