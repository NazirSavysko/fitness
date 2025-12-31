package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.FitnessUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessUserRepository extends JpaRepository<FitnessUser, Integer> {
    Optional<FitnessUser> findByUserDetails_Email(String email);
}
