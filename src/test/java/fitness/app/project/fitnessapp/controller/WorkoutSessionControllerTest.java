package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.ActiveWorkoutDTO;
import fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkoutSessionControllerTest {

    private final WorkoutFacade workoutFacade = mock(WorkoutFacade.class);
    private final WorkoutSessionController workoutSessionController = new WorkoutSessionController(workoutFacade);
    private final Principal principal = () -> "user@mail.com";
    private final Model model = mock(Model.class);

    @Test
    void getActiveWorkoutPageReturnsTemplate() {
        final ActiveWorkoutDTO activeWorkoutDTO = new ActiveWorkoutDTO(7, null, "Workout", List.of());
        when(workoutFacade.getWorkoutDetails(7, "user@mail.com")).thenReturn(activeWorkoutDTO);

        final String result = workoutSessionController.getActiveWorkoutPage(7, model, principal);

        assertEquals("workout/active", result);
        verify(model).addAttribute("activeWorkout", activeWorkoutDTO);
    }

    @Test
    void updateSetRedirectsToReturnedSessionWhenValid() {
        final UpdateExerciseSetDTO updateSetDTO = new UpdateExerciseSetDTO(4, new BigDecimal("72.5"), 12);
        final BindingResult bindingResult = new BeanPropertyBindingResult(updateSetDTO, "updateSetDto");
        final UpdateExerciseSetDTO expectedDto = new UpdateExerciseSetDTO(4, new BigDecimal("72.5"), 12);
        when(workoutFacade.updateExerciseSet(expectedDto, "user@mail.com")).thenReturn(21);

        final String result = workoutSessionController.updateSet(updateSetDTO, 4, bindingResult, 7, model, principal);

        assertEquals("redirect:/workouts/21/active", result);
        verify(workoutFacade).updateExerciseSet(expectedDto, "user@mail.com");
    }
}
