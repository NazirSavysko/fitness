package fitness.app.project.fitnessapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/workout")
public final class WorkoutController {

    @PostMapping("/start")
    public String startWorkout(@RequestParam(required = false) Long templateId) {

        Long newSessionId;

        if (templateId != null) {
            System.out.println("Создаем тренировку по шаблону ID: " + templateId);
            newSessionId = 10L;
        } else {
            System.out.println("Создаем пустую тренировку (Quick Start)");
            newSessionId = 20L; // Заглушка
        }

        return "redirect:/workout/" + newSessionId;
    }

    @GetMapping("/{id}")
    public String getActiveWorkoutPage(@PathVariable("id") Long sessionId, Model model) {

        return "workout-active";
    }
}