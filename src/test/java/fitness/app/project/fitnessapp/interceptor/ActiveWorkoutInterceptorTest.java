package fitness.app.project.fitnessapp.interceptor;

import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ActiveWorkoutInterceptorTest {

    private final WorkoutSessionRepository workoutSessionRepository = mock(WorkoutSessionRepository.class);
    private final ActiveWorkoutInterceptor activeWorkoutInterceptor = new ActiveWorkoutInterceptor(workoutSessionRepository);

    @Test
    void preHandleAllowsWhenUserIsAnonymous() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        final boolean result = activeWorkoutInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(workoutSessionRepository);
    }

    @Test
    void preHandleAllowsWhenNoActiveSessionExists() throws Exception {
        final Principal principal = () -> "user@mail.com";
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(principal);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        when(workoutSessionRepository.findFirstByUser_EmailAndEndedAtIsNull("user@mail.com")).thenReturn(Optional.empty());

        final boolean result = activeWorkoutInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(workoutSessionRepository).findFirstByUser_EmailAndEndedAtIsNull("user@mail.com");
    }

    @Test
    void preHandleRedirectsWhenActiveSessionExists() throws Exception {
        final Principal principal = () -> "user@mail.com";
        final WorkoutSession activeSession = new WorkoutSession();
        activeSession.setId(17);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(principal);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        when(workoutSessionRepository.findFirstByUser_EmailAndEndedAtIsNull("user@mail.com")).thenReturn(Optional.of(activeSession));

        final boolean result = activeWorkoutInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        assertEquals("/workouts/17/active", response.getRedirectedUrl());
        verify(workoutSessionRepository).findFirstByUser_EmailAndEndedAtIsNull("user@mail.com");
    }
}
