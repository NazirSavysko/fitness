package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.ChangePasswordDTO;
import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.facade.SettingsFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/settings")
class SettingController {
    private final SettingsFacade settingsFacade;


    @GetMapping
    public String getSettingsPage(final Principal principal, final Model model) {
        String email = principal.getName();

        final UpdateProfileDTO profileDto = this.settingsFacade.loadProfileData(email);
        model.addAttribute("profile", profileDto);
        model.addAttribute("password", new ChangePasswordDTO("", ""));

        return "settings";
    }

    @PostMapping("/profile")
    public String updateProfile(final @Valid @ModelAttribute("profile") UpdateProfileDTO profileDto,
                                final BindingResult bindingResult,
                                final Principal principal,
                                final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "settings";
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
            model.addAttribute("errors", bindingResult.getAllErrors());

            return "settings";
        }
        this.settingsFacade.changePassword(passwordDto, principal.getName());

        return "redirect:/settings";
    }

    @DeleteMapping("/delete")
    public String deleteAccount(final Principal principal) {
        this.settingsFacade.deleteAccount(principal.getName());

        return "redirect:/auth/logout";
    }
}
