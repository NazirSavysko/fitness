package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TemplateFacade {
    @Transactional(readOnly = true)
    List<GetTemplateDTO> getTemplatesByEmail(String email);

    @Transactional
    void deleteTemplateByIdAndUserEmail(Integer templateId, String name);
}
