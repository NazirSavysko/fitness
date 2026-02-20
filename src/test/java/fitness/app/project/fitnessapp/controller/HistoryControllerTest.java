package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HistoryControllerTest {

    private final WorkoutFacade workoutFacade = mock(WorkoutFacade.class);
    private final WorkoutTemplateService workoutTemplateService = mock(WorkoutTemplateService.class);
    private final HistoryController historyController = new HistoryController(workoutFacade, workoutTemplateService);
    private final Model model = mock(Model.class);
    private final Principal principal = () -> "user@mail.com";

    @Test
    void getHistoryPageAddsFiltersAndCallsFacade() {
        final WorkoutTemplate template = new WorkoutTemplate();
        template.setId(5);
        template.setName("Leg Day");
        when(workoutFacade.getHistory(eq("user@mail.com"), eq(5L), eq("LAST_30_DAYS"), eq("DATE_ASC"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(new WorkoutHistoryCardDTO(1, "Free Workout", "Jan 10, 2026", 45, 8, ""))));
        when(workoutTemplateService.getTemplatesByUserEmail("user@mail.com")).thenReturn(List.of(template));

        final String result = historyController.getHistoryPage(2, 5L, "LAST_30_DAYS", "DATE_ASC", model, principal);

        assertEquals("history/list", result);
        verify(workoutFacade).getHistory("user@mail.com", 5L, "LAST_30_DAYS", "DATE_ASC", PageRequest.of(2, 10));
        verify(workoutTemplateService).getTemplatesByUserEmail("user@mail.com");
        verify(model).addAttribute(eq("selectedTemplateId"), eq(5L));
        verify(model).addAttribute(eq("selectedDateRange"), eq("LAST_30_DAYS"));
        verify(model).addAttribute(eq("selectedSortBy"), eq("DATE_ASC"));
    }
}
