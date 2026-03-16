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

        assertTrue(content.contains("id=\"monthlyCalendar\""));
        assertTrue(content.contains("class=\"calendar-weekdays\""));
        assertTrue(content.contains("class=\"planned-icon\""));
        assertTrue(content.contains("class=\"completion-badge\""));
        assertTrue(content.contains("My Templates"));
        assertTrue(content.contains("card.action = '/workouts/start';"));
        assertTrue(content.contains("input.name = 'templateId';"));
        assertTrue(content.contains("const dayTemplates = dashboardData.templates.filter(t => t.scheduledDay === dayKey);"));
        assertTrue(content.contains("No workout scheduled for this day. Rest and recover!"));
        assertTrue(content.contains("completedDayCopy.textContent = `Workout completion: ${completionPercentage}%. ${COMPLETED_DAY_SUFFIX}`;"));
        assertTrue(content.contains("templatesSection.hidden = completed;"));
        assertTrue(content.contains("th:if=\"${errorMessage}\" class=\"alert alert-danger\""));
        assertTrue(content.contains("selectedDateIsToday"));
        assertTrue(content.indexOf("id=\"templatesSection\"") < content.indexOf("class=\"calendar-card\""));
        assertFalse(content.contains("id=\"selectedDayTitle\""));
        assertFalse(content.contains("id=\"weeklyCalendar\""));
        assertFalse(content.contains("Quick Workout"));
        assertFalse(content.contains("Start Empty"));
    }

    private String readTemplate(final String templatePath) throws IOException {
        final ClassPathResource templateResource = new ClassPathResource(templatePath);
        try (InputStream inputStream = templateResource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
