package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidEmail;
import fitness.app.project.fitnessapp.annotations.ValidPassword;

public record LoginDTO(
        @ValidEmail
        String email,
        @ValidPassword
        String password
) {
}
