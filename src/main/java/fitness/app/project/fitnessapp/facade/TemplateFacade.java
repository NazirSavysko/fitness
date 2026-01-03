package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.*;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TemplateFacade {
    @Transactional(readOnly = true)
    List<GetTemplateDTO> getTemplatesByEmail(String email);

    @Transactional
    void deleteTemplateByIdAndUserEmail(Integer templateId, String name);

    @Transactional
    void updateTemplate(UpdateTemplateDTO templateDTO, String email);

    @Transactional(readOnly = true)
    GetTemplateDTO getTemplateForUpdateById(Integer templateId,String email);

    @Transactional(readOnly = true)
    @Unmodifiable
    @NonNull
    List<TemplateExerciseDTO> getExerciseDefinitions();

    @Transactional
    void createTemplate(CreateTemplateDTO createTemplateDTO, String name);

    @Transactional(readOnly = true)
    List<GetDashboardTemplateDTO> getDashboardTemplates(String name);
}
