package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.enums.SetType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/workouts")
public final class WorkoutSessionController {

    private final WorkoutFacade workoutFacade;

    @PostMapping("/start")
    public String startWorkout(@RequestParam(required = false) final Integer templateId, final Principal principal) {
        final Integer workoutId = this.workoutFacade.startWorkout(templateId, principal.getName());
        return "redirect:/workouts/" + workoutId + "/active";
    }

    @GetMapping("/{id}/active")
    public String getActiveWorkoutPage(@PathVariable("id") final Integer sessionId, final Model model, final Principal principal) {
        model.addAttribute("activeWorkout", this.workoutFacade.getWorkoutDetails(sessionId, principal.getName()));
        if (!model.containsAttribute("addSetDto")) {
            model.addAttribute("addSetDto", new AddSetDTO(null, null, null, SetType.NORMAL, null));
        }
        model.addAttribute("setTypes", SetType.values());
        return "workout-active";
    }

    @PostMapping("/set/add")
    public String addSet(@Valid @ModelAttribute("addSetDto") final AddSetDTO addSetDTO,
                         final BindingResult bindingResult,
                         @RequestParam("sessionId") final Integer sessionId,
                         final Model model,
                         final Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeWorkout", this.workoutFacade.getWorkoutDetails(sessionId, principal.getName()));
            model.addAttribute("setTypes", SetType.values());
            return "workout-active";
        }
        final Integer activeSessionId = this.workoutFacade.addSetToExercise(addSetDTO, principal.getName());
        return "redirect:/workouts/" + activeSessionId + "/active";
    }

    @PostMapping("/{id}/finish")
    public String finishWorkout(@PathVariable("id") final Integer sessionId, final Principal principal) {
        this.workoutFacade.finishWorkout(sessionId, principal.getName());
        return "redirect:/history";
    }
}
