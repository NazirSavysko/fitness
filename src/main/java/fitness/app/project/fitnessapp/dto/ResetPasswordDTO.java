package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidEmail;
import fitness.app.project.fitnessapp.annotations.ValidPassword;

public record ResetPasswordDTO(
        @ValidEmail String email,
        @ValidPassword String newPassword,
        @ValidPassword String confirmPassword
) {
}