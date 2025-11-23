package fitness.app.project.fitnessapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public final class HelloController {

    @GetMapping
    public String hello() {
        System.out.println();
        return "dashboard";
    }
}
