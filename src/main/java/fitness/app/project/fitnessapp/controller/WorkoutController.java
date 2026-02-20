package fitness.app.project.fitnessapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/workout")
public final class WorkoutController {

    @PostMapping("/start")
    public String startWorkout(@RequestParam(required = false) final Integer templateId) {
        return "forward:/workouts/start";
    }
}
