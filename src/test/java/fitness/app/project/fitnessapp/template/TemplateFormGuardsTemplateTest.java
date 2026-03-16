package fitness.app.project.fitnessapp.template;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateFormGuardsTemplateTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "templates/workout-templates/create.html",
            "templates/workout-templates/update.html"
    })
    void templateFormPagesContainNavigationGuardAndScrollRestoreScript(final String templatePath) throws IOException {
        final String content = readTemplate(templatePath);

        assertTrue(content.contains("data-template-submit=\"true\""),
                () -> "Missing submit bypass marker in " + templatePath);
        assertTrue(content.contains("templateScrollPos"),
                () -> "Missing template scroll key usage in " + templatePath);
        assertTrue(content.contains("window.addEventListener('beforeunload'"),
                () -> "Missing beforeunload guard in " + templatePath);
        assertTrue(content.contains("template-nav-locked"),
                () -> "Missing silent navigation lock in " + templatePath);
        assertTrue(content.contains("th:field=\"*{scheduledDay}\""),
                () -> "Missing scheduledDay day selector binding in " + templatePath);
        assertTrue(content.contains("type=\"radio\""),
                () -> "Missing radio button day selector in " + templatePath);
        assertTrue(content.contains("class=\"day-chip\""),
                () -> "Missing day chip selector UI in " + templatePath);
        assertTrue(content.contains("th:if=\"${errorMessage}\" class=\"alert alert-danger\""),
                () -> "Missing error message banner in " + templatePath);
    }

    private String readTemplate(final String templatePath) throws IOException {
        final ClassPathResource templateResource = new ClassPathResource(templatePath);
        try (InputStream inputStream = templateResource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
