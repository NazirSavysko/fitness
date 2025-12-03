package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.DeactivatedToken;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface DeactivatedTokenRepository extends Repository<DeactivatedToken, UUID> {
    boolean existsById(UUID id);

    void save(DeactivatedToken token);
}
