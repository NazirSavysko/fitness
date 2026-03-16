package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.UserDashboardDTO;

public interface DashboardFacade {
    UserDashboardDTO loadUserDashboardData(String userEmail);
}
