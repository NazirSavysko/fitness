package fitness.app.project.fitnessapp.template;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SidebarMenuTemplateTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "templates/dashboard.html",
            "templates/template.html",
            "templates/template-details.html",
            "templates/exercises-container.html",
            "templates/exercises-container-update.html",
            "templates/settings.html"
    })
    void sidebarMenuContainsHistoryLink(final String templatePath) throws IOException {
        final ClassPathResource templateResource = new ClassPathResource(templatePath);
        final String content;
        try (InputStream inputStream = templateResource.getInputStream()) {
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        assertTrue(content.contains("href=\"/history\""), () -> "Missing history link in " + templatePath);
    }
}
