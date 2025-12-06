package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.FitnessUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessUserRepository extends JpaRepository<FitnessUser, Integer> {
}
