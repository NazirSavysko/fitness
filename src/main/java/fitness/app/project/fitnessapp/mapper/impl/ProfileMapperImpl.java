package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.mapper.UpdateProfileMapper;
import fitness.app.project.fitnessapp.model.FitnessUser;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class ProfileMapperImpl implements UpdateProfileMapper {

    @Contract("_ -> new")
    @Override
    public @NonNull UpdateProfileDTO mapEntityToDto(final @NonNull FitnessUser fitnessUser) {
        return new UpdateProfileDTO(
                fitnessUser.getName(),
                fitnessUser.getSurname()
        );
    }
}
