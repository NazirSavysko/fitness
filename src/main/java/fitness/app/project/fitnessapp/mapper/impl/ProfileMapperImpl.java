package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.UpdateProfileDTO;
import fitness.app.project.fitnessapp.mapper.UpdateProfileMapper;
import fitness.app.project.fitnessapp.model.User;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public final class ProfileMapperImpl implements UpdateProfileMapper {

    @Contract("_ -> new")
    @Override
    public @NonNull UpdateProfileDTO mapEntityToDto(final @NonNull User user) {
        String[] parts = user.getFullName().split(" ", 2);
        String name = parts.length > 0 ? parts[0] : "";
        String surname = parts.length > 1 ? parts[1] : "";

        return new UpdateProfileDTO(
                name,
                surname
        );
    }
}
