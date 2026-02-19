package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.UserDashboardDTO;

@FunctionalInterface
public interface DashboardFacade {
    UserDashboardDTO loadUserDashboardData(String userEmail);
}
