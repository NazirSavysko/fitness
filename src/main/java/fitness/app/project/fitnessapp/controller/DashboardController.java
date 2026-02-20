package fitness.app.project.fitnessapp.controller;


import fitness.app.project.fitnessapp.dto.GetDashboardTemplateDTO;
import fitness.app.project.fitnessapp.facade.DashboardFacade;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public final class DashboardController {
    private final DashboardFacade dashboardFacade;
    private final TemplateFacade templateFacade;

    @GetMapping("/dashboard")
    public String dashboard(final Model model,final Principal principal) {
        final List<GetDashboardTemplateDTO> templates = this.templateFacade.getDashboardTemplates(principal.getName());
        model.addAttribute("templates", templates);

        return "dashboard/dashboard";
    }
}
