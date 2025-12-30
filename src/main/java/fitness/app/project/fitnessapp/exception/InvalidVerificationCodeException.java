package fitness.app.project.fitnessapp.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(final String verificationCodeInvalidError) {
        super(verificationCodeInvalidError);
    }
}
