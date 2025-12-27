package fitness.app.project.fitnessapp.exception;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.ParseException;

import static org.slf4j.LoggerFactory.getLogger;

@ControllerAdvice
class ExceptionControllerAdvice {

    private static final Logger LOGGER = getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(JOSEException.class)
    public void handleJOSEException(JOSEException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    @ExceptionHandler(ParseException.class)
    public void handleParseException(ParseException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public void handleUsernameNotFoundException(final UsernameNotFoundException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

}
