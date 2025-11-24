package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final RegistrationDTO registrationPayload;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm() {

        return "registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registrationPayload") final RegistrationDTO registrationPayload,
                           final BindingResult bindingResult) {

        return "redirect:/auth/login?success";
    }

}
