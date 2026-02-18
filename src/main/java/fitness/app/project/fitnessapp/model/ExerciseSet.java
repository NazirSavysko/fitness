package fitness.app.project.fitnessapp.model;

import fitness.app.project.fitnessapp.model.enums.SetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "exercise_set", schema = "fitness_app")
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_exercise_id")
    private SessionExercise sessionExercise;

    private Integer setNumber;

    private BigDecimal weight;

    private Integer reps;

    private Integer restSeconds;

    @Enumerated(EnumType.STRING)
    private SetType setType;
}