package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.ChangePasswordDTO;
import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.exception.PasswordInvalidException;
import fitness.app.project.fitnessapp.facade.SettingsFacade;
import fitness.app.project.fitnessapp.mapper.UpdateProfileMapper;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.service.FitnessUserService;
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

    private final FitnessUserService fitnessUserService;
    private final UserService userService;
    private final UpdateProfileMapper updateProfileMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UpdateProfileDTO loadProfileData(final String email) {
        final FitnessUser fitnessUser = this.fitnessUserService.getFitnessUserByEmail(email);

        return this.updateProfileMapper.mapEntityToDto(fitnessUser);
    }

    @Override
    public void updateProfile(final @NonNull UpdateProfileDTO profileDto, final String email) {
        final FitnessUser fitnessUser = this.fitnessUserService.getFitnessUserByEmail(email);

        final boolean nameChanged = !Objects.equals(fitnessUser.getName(), profileDto.name());
        final boolean surnameChanged = !Objects.equals(fitnessUser.getSurname(), profileDto.surname());

        if (!nameChanged && !surnameChanged) {
            return;
        }

        fitnessUser.setName(profileDto.name());
        fitnessUser.setSurname(profileDto.surname());

        this.fitnessUserService.saveFitnessUser(fitnessUser);
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
