package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.ChangePasswordDTO;
import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.exception.PasswordInvalidException;
import fitness.app.project.fitnessapp.facade.SettingsFacade;
import fitness.app.project.fitnessapp.mapper.UpdateProfileMapper;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public final class SettingsFacadeImpl implements SettingsFacade {
    private static final String PASSWORD_MISMATCH_ERROR = "New password is invalid or does not match current password.";

    private final UserService userService;
    private final UpdateProfileMapper updateProfileMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UpdateProfileDTO loadProfileData(final String email) {
        final User user = this.userService.getUserByEmail(email);

        return this.updateProfileMapper.mapEntityToDto(user);
    }

    @Override
    public void updateProfile(final @NonNull UpdateProfileDTO profileDto, final String email) {
        final User user = this.userService.getUserByEmail(email);

        String currentFullName = user.getFullName();
        String newFullName = profileDto.name() + " " + profileDto.surname();

        if (Objects.equals(currentFullName, newFullName)) {
            return;
        }

        user.setFullName(newFullName);

        this.userService.saveUser(user);
    }

    @Override
    public void changePassword(final @NonNull ChangePasswordDTO passwordDto, final String name) {
        final User user = this.userService.getUserByEmail(name);

        if(!this.passwordEncoder.matches(passwordDto.currentPassword(), user.getPasswordHash())) {
            throw new PasswordInvalidException(PASSWORD_MISMATCH_ERROR);
        }


        user.setPasswordHash(this.passwordEncoder.encode(passwordDto.newPassword()));

        this.userService.saveUser(user);
    }

    @Override
    public void deleteAccount(final String email) {
        final User user = this.userService.getUserByEmail(email);

        this.userService.deleteUser(user);
    }
}
