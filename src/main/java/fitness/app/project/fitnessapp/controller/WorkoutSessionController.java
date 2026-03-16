package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.BulkSetUpdateDTO;
import fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/workouts")
public final class WorkoutSessionController {

    private final WorkoutFacade workoutFacade;

    @PostMapping("/start")
    public String startWorkout(@RequestParam(required = false) final Integer templateId,
                               final Principal principal,
                               final RedirectAttributes redirectAttributes) {
        try {
            final Integer workoutId = this.workoutFacade.startWorkout(templateId, principal.getName());
            return "redirect:/workouts/" + workoutId + "/active";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/{id}/active")
    public String getActiveWorkoutPage(@PathVariable("id") final Integer sessionId, final Model model, final Principal principal) {
        model.addAttribute("activeWorkout", this.workoutFacade.getWorkoutDetails(sessionId, principal.getName()));
        return "workout/active";
    }

    @PostMapping("/set/{setId}/update")
    public String updateSet(@Valid @ModelAttribute("updateSetDto") final UpdateExerciseSetDTO requestData,
                            @PathVariable("setId") final Integer setId,
                            final BindingResult bindingResult,
                            @RequestParam("sessionId") final Integer sessionId,
                            final Model model,
                            final Principal principal) {
        if (requestData.setId() != null && !setId.equals(requestData.setId())) {
            bindingResult.rejectValue("setId", "Mismatch", "The set ID in the request body does not match the set ID in the URL path");
        }
        final UpdateExerciseSetDTO updateSetDTO = new UpdateExerciseSetDTO(setId, requestData.weight(), requestData.reps());
        if (bindingResult.hasErrors()) {
            model.addAttribute("activeWorkout", this.workoutFacade.getWorkoutDetails(sessionId, principal.getName()));
            return "workout/active";
        }
        final Integer activeSessionId = this.workoutFacade.updateExerciseSet(updateSetDTO, principal.getName());
        return "redirect:/workouts/" + activeSessionId + "/active";
    }

    @PostMapping(value = "/bulk-update-sets", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> bulkUpdateSets(@Valid @RequestBody final List<@Valid BulkSetUpdateDTO> bulkSetUpdateDTOs,
                                               final Principal principal) {
        this.workoutFacade.bulkUpdateSets(bulkSetUpdateDTOs, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finish")
    public String finishWorkout(@PathVariable("id") final Integer sessionId, final Principal principal) {
        this.workoutFacade.finishWorkout(sessionId, principal.getName());
        return "redirect:/history";
    }
}
