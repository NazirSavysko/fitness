package fitness.app.project.fitnessapp.model;

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
@Table(name = "exercise_log", schema = "fitness_app")
public final class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;

    @Column(name = "set_number")
    private Integer setNumber;

    @Column(name = "reps")
    private Integer reps;

    // DECIMAL(6, 2) в базе -> BigDecimal в Java
    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    // К какой сессии относится этот подход
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private WorkoutSession workoutSession;

    // Какое именно упражнение делали (Жим, Присед и т.д.)
    // EAGER загрузка здесь нормальна, так как нам почти всегда нужно знать название упражнения
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_def_id")
    private ExerciseDefinition exerciseDefinition;
}
