package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.ForgotPasswordDTO;
import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.facade.ForgotPasswordFacade;
import fitness.app.project.fitnessapp.facade.RegistrationFitnessUserFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("auth")
public final class AuthController {

    private final RegistrationFitnessUserFacade registrationFacade;
    private final ForgotPasswordFacade forgotPasswordFacade;

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
            this.registrationFacade.register(registrationPayload);

            return "redirect:/verification?email=" + registrationPayload.email() + "&type=REGISTRATION";

        } catch (final UserExistsException e) {
            bindingResult.rejectValue("email", "error.exist.user", e.getMessage());

            return "registration";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(final Model model) {
        model.addAttribute("forgotPasswordDto", new ForgotPasswordDTO(""));

        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@Valid @ModelAttribute("forgotPasswordDto") ForgotPasswordDTO dto,
                                        BindingResult result,
                                        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());

            return "forgot-password";
        }

        try {
            this.forgotPasswordFacade.forgotPassword(dto.email());

            return "redirect:/verification?email=" + dto.email() + "&type=PASSWORD_RESET";

        } catch (UsernameNotFoundException e) {
            return "redirect:/auth/forgot-password?error";
        }
    }

}
