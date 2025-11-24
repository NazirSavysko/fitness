package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidEmail;
import fitness.app.project.fitnessapp.annotations.ValidName;
import fitness.app.project.fitnessapp.annotations.ValidPassword;
import fitness.app.project.fitnessapp.annotations.ValidSurname;

public record RegistrationDTO(
        @ValidEmail
        String email,
        @ValidPassword
        String password,
        @ValidName
        String name,
        @ValidSurname
        String surname
)
{
}
