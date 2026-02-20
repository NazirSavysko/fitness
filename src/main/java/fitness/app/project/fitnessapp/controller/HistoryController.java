package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/history")
public final class HistoryController {

    private final WorkoutFacade workoutFacade;

    @GetMapping
    public String getHistoryPage(@RequestParam(defaultValue = "0") final int page, final Model model, final Principal principal) {
        final Page<WorkoutHistoryCardDTO> historyPage = this.workoutFacade.getHistory(principal.getName(), PageRequest.of(page, 10));
        model.addAttribute("historyPage", historyPage);
        return "history-list";
    }

    @GetMapping("/{id}")
    public String getHistoryDetailsPage(@PathVariable("id") final Integer sessionId, final Model model, final Principal principal) {
        model.addAttribute("activeWorkout", this.workoutFacade.getWorkoutDetails(sessionId, principal.getName()));
        return "history-details";
    }
}
