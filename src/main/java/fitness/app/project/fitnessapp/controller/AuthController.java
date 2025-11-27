package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.facade.FitnessUserFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final FitnessUserFacade fitnessUserFacade;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm() {

        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationPayload") final RegistrationDTO registrationPayload,
                           final BindingResult bindingResult) {

        this.fitnessUserFacade.register(bindingResult,registrationPayload);

        return "redirect:/auth/login?success";
    }

}
