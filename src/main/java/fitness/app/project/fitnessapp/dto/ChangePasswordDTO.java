package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidPassword;

public record ChangePasswordDTO(
        @ValidPassword String currentPassword,
        @ValidPassword String newPassword
) {
}
