package fitness.app.project.fitnessapp.service.impl;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import fitness.app.project.fitnessapp.exception.InvalidVerificationCodeException;
import fitness.app.project.fitnessapp.model.EmailVerification;
import fitness.app.project.fitnessapp.repository.EmailVerificationRepository;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.IntStream.range;

@Service
@AllArgsConstructor
public final class EmailVerificationServiceImpl implements EmailVerificationService {
    private static final String VERIFICATION_EMAIL_SUBJECT = "Your Verification Code";
    private static final String VERIFICATION_EMAIL_TEMPLATE = "email/verification-code";
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_EXPIRY_MINUTES = 15;

    private final static String VERIFICATION_CODE_INVALID_ERROR = "Invalid verification code";

    private final EmailVerificationRepository emailVerificationRepository;
    private final TemplateEngine templateEngine;

    // Беремо ключ та адресу відправника з конфігурації
    @Value("${resend.api.key}")
    private String apiKey;

//    @Value("${mail.from:Bank Emulator <no-reply@bank-emulator.app>}")
//    private String fromEmail;

    @Override
    public void sendVerificationEmail(final String email) {
        final EmailVerification emailVerification = this.createEmailVerificationRecord(email);

        this.deleteVerificationRecordByEmail(email);
        this.saveVerificationRecord(emailVerification);

        final Context context = new Context();
        context.setVariable("code", emailVerification.getVerificationCode());
        final String htmlContent = templateEngine.process(VERIFICATION_EMAIL_TEMPLATE, context);

        final Resend resend = new Resend(apiKey);

        try {
            final CreateEmailOptions sendEmailRequest = CreateEmailOptions.builder()
                    .from("Fitness <no-reply@bank-emulator.app>")
                    .to(emailVerification.getEmail())
                    .subject(VERIFICATION_EMAIL_SUBJECT)
                    .html(htmlContent)
                    .build();

            // Відправляємо через HTTPS
            resend.emails().send(sendEmailRequest);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Помилка відправки листа через Resend API", e);
        }
    }

    @Override
    public void verifyEmailCode(final String email, final String code) {
        this.emailVerificationRepository.findByEmail(email)
                .filter(record -> record.getVerificationCode().equals(code) && !record.getExpiryDate().isBefore(now()))
                .orElseThrow(() -> new InvalidVerificationCodeException(VERIFICATION_CODE_INVALID_ERROR));

        this.deleteVerificationRecordByEmail(email);
    }

    @Override
    public void deleteVerificationRecordByEmail(final String email) {
        this.emailVerificationRepository.deleteEmailVerificationByEmail(email);
        this.emailVerificationRepository.flush();
    }

    @Override
    public void saveVerificationRecord(final EmailVerification emailVerification) {
        this.emailVerificationRepository.save(emailVerification);
    }

    @Override
    public @NonNull EmailVerification createEmailVerificationRecord(final String email) {
        final EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(email);
        emailVerification.setExpiryDate(now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES));
        emailVerification.setVerificationCode(this.generateVerificationCode());
        return emailVerification;
    }

    @Override
    public String generateVerificationCode() {
        return range(0, VERIFICATION_CODE_LENGTH)
                .map(i -> (int) (Math.random() * 10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}