package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/workout")
public final class WorkoutController {

    private final WorkoutFacade workoutFacade;

    @PostMapping("/start")
    public String startWorkout(@RequestParam(required = false) Long templateId) {

        return "redirect:/workout/" ;
    }

    @GetMapping("/{id}")
    public String getActiveWorkoutPage(@PathVariable("id") Long sessionId, Model model) {

        return "workout-active";
    }
}