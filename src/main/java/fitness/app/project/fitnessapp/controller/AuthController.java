package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.InvalidFieldFormatException;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.facade.RegistrationFitnessUserFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("auth")
public final class AuthController {

    private final RegistrationFitnessUserFacade fitnessUserFacade;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm(final Model model) {
        model.addAttribute("registrationPayload", new RegistrationDTO(null, null, null, null));

        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationPayload") final RegistrationDTO registrationPayload,
                           final BindingResult bindingResult,
                           final Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "registration";
        }

        try {
            this.fitnessUserFacade.register(registrationPayload);

            return "redirect:/verification" + registrationPayload.email() + "&type=EMAIL_VERIFICATION";

        } catch (final UserExistsException e) {
            bindingResult.rejectValue("email", "error.exist.user", e.getMessage());

            return "registration";
        }
    }
}
