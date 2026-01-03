package fitness.app.project.fitnessapp.facade;

import org.springframework.transaction.annotation.Transactional;

@FunctionalInterface
public interface ForgotPasswordFacade {

    @Transactional
    void forgotPassword(String email);
}
