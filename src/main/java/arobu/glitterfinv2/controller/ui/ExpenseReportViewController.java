package arobu.glitterfinv2.controller.ui;

import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.view.ExpenseReport;
import arobu.glitterfinv2.service.ExpenseReportService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ExpenseReportViewController {

    private final ExpenseReportService expenseReportService;

    public ExpenseReportViewController(ExpenseReportService expenseReportService) {
        this.expenseReportService = expenseReportService;
    }

    @GetMapping("/expenses")
    public String showExpenseReport(@AuthenticationPrincipal Owner owner, Model model) {
        ExpenseReport report = expenseReportService.getExpenseReport(owner);
        model.addAttribute("report", report);
        return "reports";
    }
}
