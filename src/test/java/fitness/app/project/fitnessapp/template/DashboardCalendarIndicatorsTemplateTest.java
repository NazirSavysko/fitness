package fitness.app.project.fitnessapp.template;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DashboardCalendarIndicatorsTemplateTest {

    @Test
    void dashboardTemplateContainsCalendarIndicatorStylesAndLogic() throws IOException {
        final ClassPathResource templateResource = new ClassPathResource("templates/dashboard/dashboard.html");
        final String content;
        try (InputStream inputStream = templateResource.getInputStream()) {
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        assertTrue(content.contains(".indicator-dot"),
                "Dashboard template should define indicator-dot style");
        assertTrue(content.contains(".day-btn.has-workout .indicator-dot"),
                "Dashboard template should style workout indicator dots");
        assertTrue(content.contains("function renderCalendarIndicators()"),
                "Dashboard template should define renderCalendarIndicators");
        assertTrue(content.contains("renderCalendarIndicators();"),
                "Dashboard template should render indicators on load");
    }
}
