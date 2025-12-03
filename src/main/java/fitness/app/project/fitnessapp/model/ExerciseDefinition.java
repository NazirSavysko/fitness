package fitness.app.project.fitnessapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "exercise_definition", schema = "fitness_app")
public final class ExerciseDefinition {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exercise_def_id")
    private Integer exerciseDefId;

    @Column
    private String name;

}
