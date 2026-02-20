package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.dto.ActiveWorkoutDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.enums.SetType;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkoutSessionControllerTest {

    private final WorkoutFacade workoutFacade = mock(WorkoutFacade.class);
    private final WorkoutSessionController workoutSessionController = new WorkoutSessionController(workoutFacade);
    private final Principal principal = () -> "user@mail.com";
    private final Model model = mock(Model.class);

    @Test
    void addSetReturnsActiveWorkoutPageWhenBindingFails() {
        final AddSetDTO addSetDTO = new AddSetDTO(new BigDecimal("90"), 10, 60, SetType.NORMAL, null);
        final BindingResult bindingResult = new BeanPropertyBindingResult(addSetDTO, "addSetDto");
        final ActiveWorkoutDTO activeWorkoutDTO = new ActiveWorkoutDTO(7, null, "Workout", List.of());
        bindingResult.rejectValue("sessionExerciseId", "NotNull");
        when(workoutFacade.getWorkoutDetails(7, "user@mail.com")).thenReturn(activeWorkoutDTO);

        final String result = workoutSessionController.addSet(addSetDTO, bindingResult, 7, model, principal);

        assertEquals("workout-active", result);
        verify(workoutFacade).getWorkoutDetails(7, "user@mail.com");
        verify(workoutFacade, never()).addSetToExercise(addSetDTO, "user@mail.com");
    }

    @Test
    void addSetRedirectsToReturnedSessionWhenValid() {
        final AddSetDTO addSetDTO = new AddSetDTO(new BigDecimal("90"), 10, 60, SetType.NORMAL, 3);
        final BindingResult bindingResult = new BeanPropertyBindingResult(addSetDTO, "addSetDto");
        when(workoutFacade.addSetToExercise(addSetDTO, "user@mail.com")).thenReturn(11);

        final String result = workoutSessionController.addSet(addSetDTO, bindingResult, 7, model, principal);

        assertEquals("redirect:/workouts/11/active", result);
        verify(workoutFacade).addSetToExercise(addSetDTO, "user@mail.com");
    }
}
