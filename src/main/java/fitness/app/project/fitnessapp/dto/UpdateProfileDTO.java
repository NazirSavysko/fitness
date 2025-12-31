package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidName;
import fitness.app.project.fitnessapp.annotations.ValidSurname;

public record UpdateProfileDTO(
        @ValidName String name,
        @ValidSurname String surname
) {
}
