package fitness.app.project.fitnessapp.controller;


import fitness.app.project.fitnessapp.dto.FitnessUserDashboardDTO;
import fitness.app.project.fitnessapp.facade.DashboardFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public final class DashboardController {
    private final DashboardFacade dashboardFacade;

    @GetMapping("/dashboard")
    public String dashboard(final Principal principal, final Model model) {
        final String userEmail = principal.getName();

        final FitnessUserDashboardDTO dashboardDTO = this.dashboardFacade.loadUserDashboardData(userEmail);

        model.addAttribute("dashboardData", dashboardDTO);

        return "dashboard";
    }
}
