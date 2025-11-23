package fitness.app.project.fitnessapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "auth", schema = "fitness_app")
public final class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String passwordHash;

    private LocalDateTime createdAt;
}
