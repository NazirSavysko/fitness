package fitness.app.project.fitnessapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "fitness_user", schema = "fitness_app")
public final class FitnessUser {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer userId;

    private String name;

    private String surname;

    @OneToOne(cascade = {PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "auth_id")
    private User userDetails;
}
