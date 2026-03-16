package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.TemplateExerciseConfigDTO;
import fitness.app.project.fitnessapp.dto.UpdateTemplateDTO;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.security.Principal;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class TemplateControllerTest {

    private final TemplateFacade templateFacade = mock(TemplateFacade.class);
    private final TemplateController templateController = new TemplateController(templateFacade);
    private final Principal principal = () -> "user@mail.com";
    private final Model model = mock(Model.class);

    @Test
    void createTemplateRedirectsBackToCreateWhenDayConflictHappens() {
        final CreateTemplateDTO dto = new CreateTemplateDTO(
                "Push Day",
                List.of(new TemplateExerciseConfigDTO(1, 3, 0)),
                Set.of(DayOfWeek.MONDAY)
        );
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "templateDto");
        final RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        doThrow(new IllegalArgumentException("Day conflict: You already have a template scheduled for MONDAY."))
                .when(templateFacade)
                .createTemplate(dto, "user@mail.com");

        final String result = templateController.createTemplate(dto, bindingResult, principal, model, redirectAttributes);

        assertEquals("redirect:/templates/create", result);
        assertEquals("Day conflict: You already have a template scheduled for MONDAY.",
                redirectAttributes.getFlashAttributes().get("errorMessage"));
    }

    @Test
    void editTemplateRedirectsBackToEditWhenDayConflictHappens() {
        final UpdateTemplateDTO dto = new UpdateTemplateDTO(
                9,
                "Leg Day",
                List.of(new TemplateExerciseConfigDTO(1, 4, 1)),
                Set.of(DayOfWeek.MONDAY)
        );
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "UpdateTemplateDto");
        final RedirectAttributesModelMap redirectAttributes = new RedirectAttributesModelMap();
        doThrow(new IllegalArgumentException("Day conflict: You already have a template scheduled for MONDAY."))
                .when(templateFacade)
                .updateTemplate(dto, "user@mail.com");

        final String result = templateController.editTemplate(dto, bindingResult, principal, model, redirectAttributes);

        assertEquals("redirect:/templates/edit/9", result);
        assertEquals("Day conflict: You already have a template scheduled for MONDAY.",
                redirectAttributes.getFlashAttributes().get("errorMessage"));
    }
}
