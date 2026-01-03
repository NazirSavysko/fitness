package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidEmail;

public record ForgotPasswordDTO(
        @ValidEmail String email
) {}