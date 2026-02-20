package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.dto.UpdateTemplateDTO;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("templates")
public final class TemplateController {
    private static final Comparator<TemplateExerciseDTO> ORDER_INDEX_COMPARATOR =
            Comparator.comparing(TemplateExerciseDTO::orderIndex, Comparator.nullsLast(Integer::compareTo));

    private final TemplateFacade templateFacade;


    @GetMapping
    public String getTemplatesPage(final Model model, final Principal principal) {
        final List<GetTemplateDTO> templates = this.templateFacade.getTemplatesByEmail(principal.getName());
        model.addAttribute("templates", templates);

        return "workout-templates/list";
    }

    @GetMapping("/{id}")
    public String getTemplateDetailsPage(final @PathVariable("id") Integer templateId, final Model model, final Principal principal) {
        final GetTemplateDTO templateDTO = this.templateFacade.getTemplateForUpdateById(templateId, principal.getName());
        model.addAttribute("template", templateDTO);
        model.addAttribute("exercises", templateDTO.exercises().stream()
                .sorted(ORDER_INDEX_COMPARATOR)
                .toList());

        return "workout-templates/details";
    }

    @GetMapping("/create")
    public String getCreatePage(final Model model) {
        final List<TemplateExerciseDTO> exercises = this.templateFacade.getExerciseDefinitions();
        final CreateTemplateDTO formDto = new CreateTemplateDTO("", new ArrayList<>());
        model.addAttribute("templateDto", formDto);
        model.addAttribute("exercises", exercises);

        return "workout-templates/create";
    }

    @PostMapping("/create")
    public String createTemplate(final @Valid @ModelAttribute("templateDto") CreateTemplateDTO createTemplateDTO,
                                 final BindingResult result,
                                 final Principal principal,
                                 final Model model) {

        if (result.hasErrors()) {
            final List<TemplateExerciseDTO> exercises = this.templateFacade.getExerciseDefinitions();
            model.addAttribute("exercises", exercises);
            model.addAttribute("errors", result.getAllErrors());

            return "workout-templates/create";
        }

        this.templateFacade.createTemplate(createTemplateDTO, principal.getName());


        return "redirect:/templates";
    }

    @GetMapping("edit/{id}")
    public String editTemplate(final @PathVariable("id") Integer templateId, final Model model, final Principal principal) {
        final GetTemplateDTO templateDTO = this.templateFacade.getTemplateForUpdateById(templateId, principal.getName());
        final List<TemplateExerciseDTO> exercises = this.templateFacade.getExerciseDefinitions();
        final UpdateTemplateDTO updateTemplateDTO = new UpdateTemplateDTO(
                templateDTO.id(),
                templateDTO.name(),
                templateDTO.exercises().stream()
                        .sorted(ORDER_INDEX_COMPARATOR)
                        .map(TemplateExerciseDTO::exerciseId)
                        .toList()
        );

        model.addAttribute("UpdateTemplateDto", updateTemplateDTO);
        model.addAttribute("exercises", exercises);

        return "workout-templates/update";
    }
    @PostMapping("/edit")
    public String editTemplate(final @Valid @ModelAttribute("UpdateTemplateDto") UpdateTemplateDTO templateDTO,
                               final BindingResult result,
                               final Principal principal,
                               final Model model) {
        if (result.hasErrors()) {
            final List<TemplateExerciseDTO> exercises = this.templateFacade.getExerciseDefinitions();
            model.addAttribute("exercises", exercises);


            return "workout-templates/update";
        }

        this.templateFacade.updateTemplate(templateDTO, principal.getName());

        return "redirect:/templates";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteTemplate(final @PathVariable("id") Integer templateId, final Principal principal) {

        this.templateFacade.deleteTemplateByIdAndUserEmail(templateId, principal.getName());

        return "redirect:/templates";
    }

}
