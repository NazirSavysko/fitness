package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("templates")
public final class TemplateController {

    @GetMapping
    public String getTemplatesPage() {
        return "template";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        CreateTemplateDTO formDto = new CreateTemplateDTO("", new ArrayList<>());
        model.addAttribute("templateDto", formDto);


        List<TemplateExerciseDTO> optionsList = new ArrayList<>();
        optionsList.add(new TemplateExerciseDTO(1L, "Жим лежа"));
        optionsList.add(new TemplateExerciseDTO(2L, "Приседания"));
        optionsList.add(new TemplateExerciseDTO(3L, "Становая тяга"));

        model.addAttribute("availableExercises", optionsList);

        return "exercises-container";
    }

}
