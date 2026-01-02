package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.ChangePasswordDTO;
import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;


public interface SettingsFacade {
    @Transactional(readOnly = true)
    UpdateProfileDTO loadProfileData(String email);

    @Transactional
    void updateProfile(UpdateProfileDTO profileDto, final String name);

    @Transactional
    void changePassword(@Valid ChangePasswordDTO passwordDto, String name);

    @Transactional
    void deleteAccount(String email);
}
