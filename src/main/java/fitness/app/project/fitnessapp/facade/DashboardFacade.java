package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.FitnessUserDashboardDTO;

public interface DashboardFacade {

    FitnessUserDashboardDTO loadUserDashboardData(String userEmail);
}
