package fitness.app.project.fitnessapp.model;

import fitness.app.project.fitnessapp.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users", schema = "fitness_app")
public final class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String email;

    private String passwordHash;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean enabled;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
