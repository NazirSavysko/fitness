package fitness.app.project.fitnessapp.controller;

import fitness.app.project.fitnessapp.dto.UserDashboardDTO;
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
    public String dashboard(final Model model, final Principal principal) {
        final UserDashboardDTO dashboardData = this.dashboardFacade.loadUserDashboardData(principal.getName());
        model.addAttribute("dashboardData", dashboardData);

        return "dashboard/dashboard";
    }
}
