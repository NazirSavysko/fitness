package fitness.app.project.fitnessapp.model;

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
@Table(name = "email_verification", schema = "fitness_app")
public final class EmailVerification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer authId;

    private String verificationCode;

    private LocalDateTime expiryDate;

    private String email;
}
