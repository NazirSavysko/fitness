package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.model.EmailVerification;
import fitness.app.project.fitnessapp.repository.EmailVerificationRepository;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.IntStream.range;

@Service
@AllArgsConstructor
public final class EmailVerificationServiceImpl implements EmailVerificationService {
    private static final String VERIFICATION_EMAIL_SUBJECT = "Your Verification Code";
    private static final String  VERIFICATION_EMAIL_TEMPLATE = "email/verification-code";
    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int VERIFICATION_CODE_EXPIRY_MINUTES = 15;

    private final EmailVerificationRepository emailVerificationRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    @Override
    @SneakyThrows(MessagingException.class)
    public void sendVerificationEmail(final String email, final EmailVerification emailVerification) {

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        final Context context = new Context();
        context.setVariable("code", emailVerification.getVerificationCode());

        final String htmlContent = templateEngine.process(VERIFICATION_EMAIL_TEMPLATE, context);


        helper.setTo(email);
        helper.setSubject(VERIFICATION_EMAIL_SUBJECT);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    @Override
    public boolean verifyEmailCode(final Integer authId, final String code) {
        return this.emailVerificationRepository.findByAuthId(authId)
                .map(record -> record.getVerificationCode().equals(code) && !record.getExpiryDate().isBefore(now()))
                .orElse(false);
    }

    @Override
    public void deleteVerificationRecord(final EmailVerification emailVerification) {
        this.emailVerificationRepository.delete(emailVerification);
    }

    @Override
    public void saveVerificationRecord(final EmailVerification emailVerification) {
        this.emailVerificationRepository.save(emailVerification);
    }

    @Override
    public @NonNull EmailVerification createEmailVerificationRecord(final Integer authId) {
        final EmailVerification emailVerification = new EmailVerification();

        emailVerification.setAuthId(authId);
        emailVerification.setExpiryDate(now().plusMinutes(VERIFICATION_CODE_EXPIRY_MINUTES));
        emailVerification.setVerificationCode(this.generateVerificationCode());

        return emailVerification;
    }

    @Override
    public String generateVerificationCode() {
        return range(0, VERIFICATION_CODE_LENGTH)
                .map(i -> (int) (Math.random() * 10))
                .mapToObj(String::valueOf).collect(Collectors.joining());
    }
}
