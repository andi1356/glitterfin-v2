package arobu.glitterfinv2.controller.ui;

import arobu.glitterfinv2.model.entity.ExpenseCondition;
import arobu.glitterfinv2.model.entity.ExpenseRule;
import arobu.glitterfinv2.model.entity.Owner;
import arobu.glitterfinv2.model.entity.meta.ExpenseField;
import arobu.glitterfinv2.model.entity.meta.ExpenseRulesetUpdatableField;
import arobu.glitterfinv2.model.entity.meta.Predicate;
import arobu.glitterfinv2.model.form.ExpenseConditionForm;
import arobu.glitterfinv2.model.form.ExpenseRuleForm;
import arobu.glitterfinv2.service.ExpenseConditionService;
import arobu.glitterfinv2.service.ExpenseRuleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rulesets")
public class RulesetViewController {

    private final ExpenseConditionService conditionService;
    private final ExpenseRuleService ruleService;

    public RulesetViewController(ExpenseConditionService conditionService,
                                 ExpenseRuleService ruleService) {
        this.conditionService = conditionService;
        this.ruleService = ruleService;
    }

    @GetMapping("/conditions")
    public String listConditionsUI(@AuthenticationPrincipal Owner owner, Model model) {
        List<ExpenseCondition> conditions = conditionService.getConditions(owner);
        model.addAttribute("conditions", conditions);
        model.addAttribute("activeTab", "conditions");
        return "conditions";
    }

    @GetMapping("/conditions/new")
    public String newConditionUI(Model model) {
        model.addAttribute("conditionForm", new ExpenseConditionForm());
        model.addAttribute("fields", ExpenseField.values());
        model.addAttribute("predicates", Predicate.values());
        model.addAttribute("isEdit", false);
        return "forms/condition-form";
    }

    @GetMapping("/conditions/{id}/edit")
    public String editConditionUI(@AuthenticationPrincipal Owner owner,
                                  @PathVariable("id") Integer conditionId,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Optional<ExpenseCondition> condition = conditionService.getCondition(conditionId, owner);
        if (condition.isEmpty()) {
            redirectAttributes.addFlashAttribute("conditionMessage", "Unable to locate the requested condition.");
            return "redirect:/rulesets/conditions";
        }

        model.addAttribute("condition", condition.get());
        model.addAttribute("conditionForm", ExpenseConditionForm.fromEntity(condition.get()));
        model.addAttribute("fields", ExpenseField.values());
        model.addAttribute("predicates", Predicate.values());
        model.addAttribute("isEdit", true);
        return "forms/condition-form";
    }

    @PostMapping("/conditions")
    public String createCondition(@AuthenticationPrincipal Owner owner,
                                  @ModelAttribute("conditionForm") ExpenseConditionForm form,
                                  RedirectAttributes redirectAttributes) {
        final var creationMessage = conditionService.createCondition(owner, form);
        redirectAttributes.addFlashAttribute("conditionMessage", creationMessage);
        return "redirect:/rulesets/conditions";
    }

    @PostMapping("/conditions/{id}/edit")
    public String updateCondition(@AuthenticationPrincipal Owner owner,
                                  @PathVariable("id") Integer conditionId,
                                  @ModelAttribute("conditionForm") ExpenseConditionForm form,
                                  RedirectAttributes redirectAttributes) {
        boolean updated = conditionService.updateCondition(conditionId, owner, form).isPresent();
        String message = updated
                ? "Condition updated successfully."
                : "Unable to update the condition. Please try again.";
        redirectAttributes.addFlashAttribute("conditionMessage", message);
        return "redirect:/rulesets/conditions";
    }

    @PostMapping("/conditions/{id}/delete")
    public String deleteCondition(@AuthenticationPrincipal Owner owner,
                                  @PathVariable("id") Integer conditionId,
                                  RedirectAttributes redirectAttributes) {
        boolean deleted = conditionService.deleteCondition(owner, conditionId);
        String message = deleted
                ? "Condition deleted successfully."
                : "Unable to delete the condition.";
        redirectAttributes.addFlashAttribute("conditionMessage", message);
        return "redirect:/rulesets/conditions";
    }

    @GetMapping("/rules")
    public String listRules(@AuthenticationPrincipal Owner owner,
                            Model model) {
        List<ExpenseRule> rules = ruleService.getRules(owner);
        model.addAttribute("rules", rules);
        model.addAttribute("conditions",
                conditionService.getConditions(owner));
        model.addAttribute("activeTab", "rules");
        return "rules";
    }

    @GetMapping("/rules/new")
    public String newRule(@AuthenticationPrincipal Owner owner,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        List<ExpenseCondition> conditions = conditionService.getConditions(owner);
        if (conditions.isEmpty()) {
            redirectAttributes.addFlashAttribute("conditionMessage", "Create a condition before adding rules.");
            return "redirect:/rulesets/conditions";
        }
        model.addAttribute("ruleForm", new ExpenseRuleForm());
        model.addAttribute("conditions", conditions);
        model.addAttribute("updatableFields", ExpenseRulesetUpdatableField.values());
        model.addAttribute("isEdit", false);
        return "forms/rule-form";
    }

    @GetMapping("/rules/{id}/edit")
    public String editRule(@AuthenticationPrincipal Owner owner,
                           @PathVariable("id") Integer ruleId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Optional<ExpenseRule> rule = ruleService.getRule(owner, ruleId);
        if (rule.isEmpty()) {
            redirectAttributes.addFlashAttribute("ruleMessage", "Unable to locate the requested rule.");
            return "redirect:/rulesets/rules";
        }
        List<ExpenseCondition> conditions = conditionService.getConditions(owner);
        if (conditions.isEmpty()) {
            redirectAttributes.addFlashAttribute("conditionMessage", "Create a condition before editing rules.");
            return "redirect:/rulesets/conditions";
        }
        model.addAttribute("rule", rule.get());
        model.addAttribute("ruleForm", ExpenseRuleForm.fromEntity(rule.get()));
        model.addAttribute("conditions", conditions);
        model.addAttribute("updatableFields", ExpenseRulesetUpdatableField.values());
        model.addAttribute("isEdit", true);
        return "forms/rule-form";
    }

    @PostMapping("/rules")
    public String createRule(@AuthenticationPrincipal Owner owner,
                             @ModelAttribute("ruleForm") ExpenseRuleForm form,
                             RedirectAttributes redirectAttributes) {
        String creationMessage = ruleService.createRule(owner, form);

        redirectAttributes.addFlashAttribute("ruleMessage", creationMessage);
        return "redirect:/rulesets/rules";
    }

    @PostMapping("/rules/{id}/edit")
    public String updateRule(@PathVariable("id") Integer ruleId,
                             @AuthenticationPrincipal Owner owner,
                             @ModelAttribute("ruleForm") ExpenseRuleForm form,
                             RedirectAttributes redirectAttributes) {
        boolean updated = ruleService.updateRule(ruleId, owner, form).isPresent();
        String message = updated
                ? "Rule updated successfully."
                : "Unable to update the rule. Please verify the selected condition.";
        redirectAttributes.addFlashAttribute("ruleMessage", message);
        return "redirect:/rulesets/rules";
    }

    @PostMapping("/rules/{id}/delete")
    public String deleteRule(@PathVariable("id") Integer ruleId,
                             @AuthenticationPrincipal Owner owner,
                             RedirectAttributes redirectAttributes) {
        boolean deleted = ruleService.deleteRule(ruleId, owner);
        String message = deleted
                ? "Rule deleted successfully."
                : "Unable to delete the rule. It may have already been removed.";
        redirectAttributes.addFlashAttribute("ruleMessage", message);
        return "redirect:/rulesets/rules";
    }
}
