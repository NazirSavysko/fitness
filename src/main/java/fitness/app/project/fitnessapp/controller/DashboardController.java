package fitness.app.project.fitnessapp.controller;


import fitness.app.project.fitnessapp.dto.GetDashboardTemplateDTO;
import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.facade.DashboardFacade;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public final class DashboardController {
    private final DashboardFacade dashboardFacade;
    private final TemplateFacade templateFacade;
    private final WorkoutFacade workoutFacade;

    @GetMapping("/dashboard")
    public String dashboard(final Model model,final Principal principal) {
        final List<GetDashboardTemplateDTO> templates = this.templateFacade.getDashboardTemplates(principal.getName());
        final List<WorkoutHistoryCardDTO> historyCards = this.workoutFacade
                .getHistory(principal.getName(), null, "ALL", "DATE_DESC", PageRequest.of(0, 400))
                .getContent();
        final Map<String, Integer> workoutCompletionByDate = new LinkedHashMap<>();
        for (WorkoutHistoryCardDTO historyCard : historyCards) {
            workoutCompletionByDate.putIfAbsent(historyCard.dateKey(), historyCard.completionPercentage());
        }

        model.addAttribute("templates", templates);
        model.addAttribute("completedWorkoutDayKeys", workoutCompletionByDate.keySet());
        model.addAttribute("workoutCompletionByDate", workoutCompletionByDate);

        return "dashboard/dashboard";
    }
}
