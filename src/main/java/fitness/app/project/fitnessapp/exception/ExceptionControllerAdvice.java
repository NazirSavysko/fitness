package fitness.app.project.fitnessapp.exception;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.slf4j.LoggerFactory.getLogger;

@ControllerAdvice
class ExceptionControllerAdvice {

    private static final Logger LOGGER = getLogger(ExceptionControllerAdvice.class);


    @ExceptionHandler({JOSEException.class, ParseException.class})
    public String handleTechnicalExceptions(final Exception ex, HttpServletResponse response) {
        LOGGER.error("Security/Parsing error: {}", ex.getMessage(), ex);
        response.setStatus(SC_INTERNAL_SERVER_ERROR);

        return "error/500"; // Общая страница ошибки
    }

    // 2. Пользователь не найден -> Страница 404
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(final @NonNull UsernameNotFoundException ex,final HttpServletResponse response) {
        LOGGER.error("User not found: {}", ex.getMessage());
        response.setStatus(SC_NOT_FOUND);

        return "error/404";
    }

    // 3. Ошибка почты -> Специальная страница
    @ExceptionHandler(MessagingException.class)
    public String handleMessagingException(final MessagingException ex, HttpServletResponse response) {
        LOGGER.error("Error sending email: {}", ex.getMessage(), ex);
        response.setStatus(SC_SERVICE_UNAVAILABLE);

        return "error/email_error";
    }
}