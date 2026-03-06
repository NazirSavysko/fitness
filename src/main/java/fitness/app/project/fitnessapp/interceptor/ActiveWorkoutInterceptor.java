package fitness.app.project.fitnessapp.interceptor;

import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.repository.WorkoutSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;

@Component
@AllArgsConstructor
public final class ActiveWorkoutInterceptor implements HandlerInterceptor {

    private final WorkoutSessionRepository workoutSessionRepository;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return true;
        }

        final Optional<WorkoutSession> activeSession = this.workoutSessionRepository
                .findFirstByUser_EmailAndEndedAtIsNull(principal.getName());
        if (activeSession.isEmpty()) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/workouts/" + activeSession.get().getId() + "/active");
        return false;
    }
}
