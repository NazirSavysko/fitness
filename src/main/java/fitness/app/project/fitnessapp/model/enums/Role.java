package fitness.app.project.fitnessapp.model.enums;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public @NotNull String getAuthority() {
        return name();
    }
}
