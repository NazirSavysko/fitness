package fitness.app.project.fitnessapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "email_verification", schema = "fitness_app")
public final class EmailVerification {

    @Id
    private Integer authId;

    private String verificationCode;

    private LocalDateTime expiryDate;
}
