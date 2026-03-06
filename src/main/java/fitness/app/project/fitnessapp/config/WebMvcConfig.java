package fitness.app.project.fitnessapp.config;

import fitness.app.project.fitnessapp.interceptor.ActiveWorkoutInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ActiveWorkoutInterceptor activeWorkoutInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(activeWorkoutInterceptor)
                .addPathPatterns("/dashboard", "/history/**", "/templates/**", "/settings/**");
    }
}
