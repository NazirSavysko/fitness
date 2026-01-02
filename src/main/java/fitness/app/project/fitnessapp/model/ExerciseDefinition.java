package fitness.app.project.fitnessapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Cacheable
@Cache(usage = READ_ONLY)
@Table(name = "exercise_definition", schema = "fitness_app")
public final class ExerciseDefinition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_def_id")
    private Integer exerciseDefId;

    @Column
    private String name;
}
