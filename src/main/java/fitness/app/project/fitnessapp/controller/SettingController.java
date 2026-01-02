package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.ChangePasswordDTO;
import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.exception.PasswordInvalidException;
import fitness.app.project.fitnessapp.facade.SettingsFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.function.Supplier;

@Controller
@AllArgsConstructor
@RequestMapping("/settings")
class SettingController {
    private final SettingsFacade settingsFacade;


    @GetMapping
    public String getSettingsPage(final Principal principal, final Model model) {
        this.prepareModelForPage(model, principal.getName());

        return "settings";
    }

    @PostMapping("/profile")
    public String updateProfile(final @Valid @ModelAttribute("profile") UpdateProfileDTO profileDto,
                                final BindingResult bindingResult,
                                final Principal principal,
                                final Model model) {
        if (bindingResult.hasErrors()) {
            return this.handleValidationErrors(model, principal.getName(), bindingResult);
        }
        this.settingsFacade.updateProfile(profileDto, principal.getName());

        return "redirect:/settings";
    }

    @PostMapping("/password")
    public String changePassword(final @Valid @ModelAttribute("password") ChangePasswordDTO passwordDto,
                                 final BindingResult bindingResult,
                                 final Principal principal,
                                 final Model model) {
        if (bindingResult.hasErrors()) {
            return this.handleValidationErrors(model, principal.getName(), bindingResult);
        }
        try {
            this.settingsFacade.changePassword(passwordDto, principal.getName());
            return "redirect:/settings";
        } catch (final PasswordInvalidException e) {
            bindingResult.rejectValue("currentPassword", "error.invalid.current.password", e.getMessage());
            return this.handleValidationErrors(model, principal.getName(), bindingResult);
        }
    }

    @DeleteMapping("/delete")
    public String deleteAccount(final Principal principal) {
        this.settingsFacade.deleteAccount(principal.getName());

        return "redirect:/auth/login";
    }


    private @NonNull String handleValidationErrors(@NonNull Model model, String email, @NonNull BindingResult bindingResult) {
        model.addAttribute("errors", bindingResult.getAllErrors());
        this.prepareModelForPage(model, email);

        return "settings";
    }

    private void prepareModelForPage(Model model, String email) {
        this.addAttributeIfMissing(model, "profile", () -> settingsFacade.loadProfileData(email));
        this.addAttributeIfMissing(model, "password", () -> new ChangePasswordDTO("", ""));
    }

    private void addAttributeIfMissing(@NonNull Model model, String attributeName, Supplier<?> valueSupplier) {
        if (!model.containsAttribute(attributeName)) {
            model.addAttribute(attributeName, valueSupplier.get());
        }
    }
}
