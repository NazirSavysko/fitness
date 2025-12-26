package fitness.app.project.fitnessapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public final class DashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard";
    }
}
