package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.exception.InvalidVerificationCodeException;
import fitness.app.project.fitnessapp.facade.MailFacade;
import fitness.app.project.fitnessapp.model.VerificationType;
import fitness.app.project.fitnessapp.strategy.verification.VerificationRedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final MailFacade mailFacade;
    private final VerificationRedirectService redirectService;

    @GetMapping
    public String getVerifyPage(final @RequestParam("email") String email,
                                final @RequestParam(value = "type") VerificationType type,
                                Model model) {
        model.addAttribute("email", email);
        model.addAttribute("type", type);

        return "verify";
    }

    @PostMapping
    public String verifyCode(@RequestParam("email") String email,
                             @RequestParam("code") String code,
                             @RequestParam("type") VerificationType type,
                             Model model) {
        try {
            this.mailFacade.verifyEmail(email, code, type);

            return redirectService.getRedirectUrl(type, email, code);

        } catch (final InvalidVerificationCodeException e) {
            model.addAttribute("error", "Wrong verification code");
            model.addAttribute("email", email);
            model.addAttribute("type", type);

            return "verify";
        }
    }
}