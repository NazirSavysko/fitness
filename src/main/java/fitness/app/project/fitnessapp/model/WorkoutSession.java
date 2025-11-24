package fitness.app.project.fitnessapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "workout_session", schema = "fitness_app")
public final class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;

    @Column(name = "session_date")
    private LocalDateTime sessionDate;

    // Связь с пользователем (Кто тренировался?)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private FitnessUser user;

    // Связь с шаблоном (По какой программе?)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private WorkoutTemplate template;

    // Обратная связь: список всех подходов в этой тренировке
    // cascade = CascadeType.ALL означает, что если удалить сессию, удалятся и логи
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLog> logs;
}
