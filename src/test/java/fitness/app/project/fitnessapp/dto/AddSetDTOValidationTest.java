package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.model.enums.SetType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddSetDTOValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsWeightAboveDatabaseLimit() {
        final AddSetDTO dto = new AddSetDTO(new BigDecimal("10000.00"), 10, 30, SetType.NORMAL, 1);

        final String messages = validator.validate(dto).stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(" "));

        assertTrue(messages.contains("Weight must not exceed 9999.99"));
    }

    @Test
    void rejectsRepsOutsideSupportedRange() {
        final AddSetDTO dto = new AddSetDTO(new BigDecimal("90.00"), 1000, 30, SetType.NORMAL, 1);

        final String messages = validator.validate(dto).stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(" "));

        assertTrue(messages.contains("Reps must not exceed 999"));
    }

    @Test
    void acceptsValidWeightAndRepsRange() {
        final AddSetDTO dto = new AddSetDTO(new BigDecimal("9999.99"), 999, 30, SetType.NORMAL, 1);

        assertFalse(validator.validate(dto).stream().findAny().isPresent());
    }
}
