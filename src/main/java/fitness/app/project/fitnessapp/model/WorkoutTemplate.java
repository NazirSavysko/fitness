package fitness.app.project.fitnessapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "workout_template", schema = "fitness_app")
public final class WorkoutTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Integer id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private FitnessUser user;

     @ManyToMany
     @JoinTable(
         name = "template_exercise",
         schema = "fitness_app",
         joinColumns = @JoinColumn(name = "template_id"),
         inverseJoinColumns = @JoinColumn(name = "exercise_def_id")
     )
     private List<ExerciseDefinition> exercises;
}
