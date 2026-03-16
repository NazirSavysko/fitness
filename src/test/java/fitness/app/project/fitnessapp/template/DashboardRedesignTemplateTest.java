package fitness.app.project.fitnessapp.template;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardRedesignTemplateTest {

    @Test
    void dashboardContainsRequiredRedesignElements() throws IOException {
        final String content = readTemplate("templates/dashboard/dashboard.html");

        assertTrue(content.contains("id=\"weeklyCalendar\""));
        assertTrue(content.contains("class=\"indicator-dot planned\""));
        assertTrue(content.contains("class=\"indicator-dot completed\""));
        assertTrue(content.contains("My Templates"));
        assertTrue(content.contains("form.action = '/workouts/start';"));
        assertTrue(content.contains("input.name = 'templateId';"));
        assertFalse(content.contains("id=\"selectedDayTitle\""));
    }

    private String readTemplate(final String templatePath) throws IOException {
        final ClassPathResource templateResource = new ClassPathResource(templatePath);
        try (InputStream inputStream = templateResource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
