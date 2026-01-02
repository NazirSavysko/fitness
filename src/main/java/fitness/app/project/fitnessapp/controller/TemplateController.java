package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("templates")
public final class TemplateController {
    private final TemplateFacade templateFacade;

    @GetMapping
    public String getTemplatesPage(final Model model, final Principal principal) {
        final List<GetTemplateDTO> templates = this.templateFacade.getTemplatesByEmail(principal.getName());
        model.addAttribute("templates", templates);

        return "template";
    }

    @GetMapping("/create")
    public String getCreatePage(final Model model) {
        final CreateTemplateDTO formDto = new CreateTemplateDTO("", new ArrayList<>());
        model.addAttribute("templateDto", formDto);


////        List<TemplateExerciseDTO> optionsList = new ArrayList<>();
////        optionsList.add(new TemplateExerciseDTO(1, "Жим лежа"));
////        optionsList.add(new TemplateExerciseDTO(2, "Приседания"));
////        optionsList.add(new TemplateExerciseDTO(3, "Становая тяга"));
//
//        model.addAttribute("availableExercises", optionsList);

        return "exercises-container";
    }

    @PostMapping("/create")
    public String createTemplate(final @Valid @ModelAttribute("templateDto") CreateTemplateDTO createTemplateDTO,
                                 final BindingResult result,
                                 final Principal principal,
                                 final Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());

            return "exercises-container";
        }


        return "redirect:/templates";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTemplate(final @PathVariable("id") Integer templateId,final Principal principal) {

        System.out.println("Видаляємо шаблон з ID: " + templateId);
        return "redirect:/templates";
    }

}
