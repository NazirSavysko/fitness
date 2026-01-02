package fitness.app.project.fitnessapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("templates")
public final class TemplateController {

    @GetMapping
    public String getTemplatesPage() {

        return "template";
    }
}
