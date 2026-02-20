package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.enums.SetType;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class WorkoutSessionControllerTest {

    private final WorkoutFacade workoutFacade = mock(WorkoutFacade.class);
    private final WorkoutSessionController workoutSessionController = new WorkoutSessionController(workoutFacade);
    private final Principal principal = () -> "user@mail.com";

    @Test
    void addSetRedirectsBackToSessionWhenBindingFails() {
        final AddSetDTO addSetDTO = new AddSetDTO(new BigDecimal("90"), 10, 60, SetType.NORMAL, null);
        final BindingResult bindingResult = new BeanPropertyBindingResult(addSetDTO, "addSetDto");
        bindingResult.rejectValue("sessionExerciseId", "NotNull");

        final String result = workoutSessionController.addSet(addSetDTO, bindingResult, 7, principal);

        assertEquals("redirect:/workouts/7/active", result);
        verifyNoInteractions(workoutFacade);
    }

    @Test
    void addSetRedirectsToReturnedSessionWhenValid() {
        final AddSetDTO addSetDTO = new AddSetDTO(new BigDecimal("90"), 10, 60, SetType.NORMAL, 3);
        final BindingResult bindingResult = new BeanPropertyBindingResult(addSetDTO, "addSetDto");
        when(workoutFacade.addSetToExercise(addSetDTO, "user@mail.com")).thenReturn(11);

        final String result = workoutSessionController.addSet(addSetDTO, bindingResult, 7, principal);

        assertEquals("redirect:/workouts/11/active", result);
        verify(workoutFacade).addSetToExercise(addSetDTO, "user@mail.com");
    }
}
