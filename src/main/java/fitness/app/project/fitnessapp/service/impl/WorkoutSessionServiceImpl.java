package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.dto.BulkSetUpdateDTO;
import fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO;
import fitness.app.project.fitnessapp.model.ExerciseSet;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.model.enums.SetType;
import fitness.app.project.fitnessapp.repository.ExerciseSetRepository;
import fitness.app.project.fitnessapp.repository.SessionExerciseRepository;
import fitness.app.project.fitnessapp.repository.WorkoutSessionRepository;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@AllArgsConstructor
public final class WorkoutSessionServiceImpl implements WorkoutSessionService {

    private final WorkoutTemplateService workoutTemplateService;
    private final UserService userService;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final SessionExerciseRepository sessionExerciseRepository;
    private final ExerciseSetRepository exerciseSetRepository;

    @Override
    @Transactional
    public Integer startWorkout(final Integer templateId, final String userEmail) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        final LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);
        if (this.workoutSessionRepository.existsByUser_EmailAndStartedAtBetween(userEmail, startOfDay, endOfDay)) {
            throw new IllegalStateException("Only one workout is allowed per day.");
        }

        final WorkoutTemplate workoutTemplate = templateId == null
                ? null
                : this.workoutTemplateService.getWorkoutTemplateById(templateId, userEmail);
        if (!canStartTemplateToday(workoutTemplate)) {
            throw new IllegalStateException("You can only start templates scheduled for today.");
        }

        final WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setUser(this.userService.getUserByEmail(userEmail));
        workoutSession.setSourceTemplate(workoutTemplate);
        workoutSession.setStartedAt(now);
        final WorkoutSession savedSession = this.workoutSessionRepository.saveAndFlush(workoutSession);
        savedSession.setExercises(List.of());

        if (workoutTemplate == null || workoutTemplate.getExercises() == null) {
            return savedSession.getId();
        }

        final List<TemplateExercise> templateExercises = new ArrayList<>();
        for (TemplateExercise templateExercise : workoutTemplate.getExercises()) {
            if (templateExercise != null) {
                templateExercises.add(templateExercise);
            }
        }
        if (templateExercises.isEmpty()) {
            return savedSession.getId();
        }

        final List<SessionExercise> sessionExercises = new ArrayList<>();
        for (TemplateExercise templateExercise : templateExercises) {
            final SessionExercise sessionExercise = new SessionExercise();
            sessionExercise.setSession(savedSession);
            sessionExercise.setExercise(templateExercise.getExercise());
            sessionExercise.setOrderIndex(templateExercise.getOrderIndex());
            sessionExercises.add(sessionExercise);
        }
        final List<SessionExercise> savedExercises = this.sessionExerciseRepository.saveAll(sessionExercises);

        final List<ExerciseSet> sets = new ArrayList<>();
        for (int index = 0; index < templateExercises.size(); index++) {
            final List<ExerciseSet> exerciseSets = buildSetsFromTemplateConfig(savedExercises.get(index), templateExercises.get(index));
            savedExercises.get(index).setSets(exerciseSets);
            sets.addAll(exerciseSets);
        }
        if (!sets.isEmpty()) {
            this.exerciseSetRepository.saveAll(sets);
        }

        savedSession.setExercises(savedExercises);
        return savedSession.getId();
    }

    private static boolean canStartTemplateToday(final WorkoutTemplate workoutTemplate) {
        return workoutTemplate == null
                || workoutTemplate.getScheduledDays() == null
                || workoutTemplate.getScheduledDays().isEmpty()
                || workoutTemplate.getScheduledDays().contains(LocalDate.now().getDayOfWeek());
    }

    private static List<ExerciseSet> buildSetsFromTemplateConfig(final SessionExercise sessionExercise,
                                                                 final TemplateExercise templateExercise) {
        final int normalSets = templateExercise.getNormalSets() == null ? 0 : templateExercise.getNormalSets();
        final int failureSets = templateExercise.getFailureSets() == null ? 0 : templateExercise.getFailureSets();
        final List<ExerciseSet> sets = new ArrayList<>(normalSets + failureSets);
        int setNumber = 1;

        for (int i = 0; i < normalSets; i++) {
            sets.add(createPresetSet(sessionExercise, setNumber++, SetType.NORMAL));
        }
        for (int i = 0; i < failureSets; i++) {
            sets.add(createPresetSet(sessionExercise, setNumber++, SetType.FAILURE));
        }

        return sets;
    }

    private static ExerciseSet createPresetSet(final SessionExercise sessionExercise,
                                               final int setNumber,
                                               final SetType setType) {
        final ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setSessionExercise(sessionExercise);
        exerciseSet.setSetNumber(setNumber);
        exerciseSet.setWeight(null);
        exerciseSet.setReps(null);
        exerciseSet.setSetType(setType);
        return exerciseSet;
    }

    @Override
    @Transactional
    public Integer addSetToExercise(final AddSetDTO addSetDTO, final String userEmail) {
        final SessionExercise sessionExercise = this.sessionExerciseRepository
                .findByIdAndSession_User_Email(addSetDTO.sessionExerciseId(), userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Session exercise not found"));

        final int nextSetNumber = this.exerciseSetRepository
                .findTopBySessionExercise_IdOrderBySetNumberDesc(addSetDTO.sessionExerciseId())
                .map(ExerciseSet::getSetNumber)
                .orElse(0) + 1;

        final ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setSessionExercise(sessionExercise);
        exerciseSet.setSetNumber(nextSetNumber);
        exerciseSet.setWeight(addSetDTO.weight());
        exerciseSet.setReps(addSetDTO.reps());
        exerciseSet.setRestSeconds(addSetDTO.restSeconds());
        exerciseSet.setSetType(addSetDTO.setType());

        this.exerciseSetRepository.save(exerciseSet);
        return sessionExercise.getSession().getId();
    }

    @Override
    @Transactional
    public Integer updateExerciseSet(final UpdateExerciseSetDTO updateExerciseSetDTO, final String userEmail) {
        final ExerciseSet exerciseSet = this.exerciseSetRepository
                .findByIdAndSessionExercise_Session_User_Email(updateExerciseSetDTO.setId(), userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Exercise set not found"));

        exerciseSet.setWeight(updateExerciseSetDTO.weight());
        exerciseSet.setReps(updateExerciseSetDTO.reps());
        this.exerciseSetRepository.save(exerciseSet);

        return exerciseSet.getSessionExercise().getSession().getId();
    }

    @Override
    @Transactional
    public void bulkUpdateSets(final List<BulkSetUpdateDTO> bulkSetUpdateDTOs, final String userEmail) {
        if (bulkSetUpdateDTOs == null || bulkSetUpdateDTOs.isEmpty()) {
            return;
        }

        final List<Integer> setIds = bulkSetUpdateDTOs.stream()
                .map(BulkSetUpdateDTO::setId)
                .toList();
        final Collection<ExerciseSet> existingExerciseSets = this.exerciseSetRepository
                .findAllByIdInAndSessionExercise_Session_User_Email(setIds, userEmail);
        final Map<Integer, ExerciseSet> exerciseSetById = new HashMap<>();
        for (ExerciseSet exerciseSet : existingExerciseSets) {
            exerciseSetById.put(exerciseSet.getId(), exerciseSet);
        }
        final List<Integer> missingSetIds = setIds.stream()
                .filter(setId -> !exerciseSetById.containsKey(setId))
                .distinct()
                .toList();
        if (!missingSetIds.isEmpty()) {
            throw new EntityNotFoundException("Exercise sets not found for IDs: " + missingSetIds);
        }

        for (BulkSetUpdateDTO setUpdateDTO : bulkSetUpdateDTOs) {
            final ExerciseSet exerciseSet = exerciseSetById.get(setUpdateDTO.setId());
            exerciseSet.setWeight(setUpdateDTO.weight());
            exerciseSet.setReps(setUpdateDTO.reps());
            exerciseSet.setRestSeconds(setUpdateDTO.restSeconds());
        }

        this.exerciseSetRepository.saveAll(existingExerciseSets);
    }

    @Override
    @Transactional
    public void finishWorkout(final Integer sessionId, final String userEmail) {
        final WorkoutSession workoutSession = this.workoutSessionRepository
                .findByIdAndUser_Email(sessionId, userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Workout session not found"));

        workoutSession.setEndedAt(LocalDateTime.now());
        this.workoutSessionRepository.save(workoutSession);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutSession> getHistory(final String userEmail, final Long templateId, final String dateRange, final String sortBy, final Pageable pageable) {
        Specification<WorkoutSession> specification = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("user").get("email"), userEmail),
                criteriaBuilder.isNotNull(root.get("endedAt"))
        );

        if (templateId != null) {
            if (templateId <= 0) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("sourceTemplate")));
            } else {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sourceTemplate").get("id"), templateId.intValue()));
            }
        }

        final LocalDateTime fromDate = resolveFromDate(dateRange);
        if (fromDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("startedAt"), fromDate));
        }

        final PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                resolveSort(sortBy)
        );
        return this.workoutSessionRepository.findAll(specification, pageRequest);
    }

    private static LocalDateTime resolveFromDate(final String dateRange) {
        if (dateRange == null) {
            return null;
        }
        return switch (dateRange.toUpperCase(Locale.ROOT)) {
            case "LAST_7_DAYS" -> LocalDateTime.now().minusDays(7);
            case "LAST_30_DAYS" -> LocalDateTime.now().minusDays(30);
            default -> null;
        };
    }

    private static Sort resolveSort(final String sortBy) {
        if (sortBy == null) {
            return Sort.by("startedAt").descending();
        }
        return switch (sortBy.toUpperCase(Locale.ROOT)) {
            case "DATE_ASC" -> Sort.by("startedAt").ascending();
            case "DURATION_DESC" -> JpaSort.unsafe(Sort.Direction.DESC, "endedAt - startedAt");
            default -> Sort.by("startedAt").descending();
        };
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutSession getWorkoutDetails(final Integer sessionId, final String userEmail) {
        final WorkoutSession workoutSession = this.workoutSessionRepository
                .findByIdAndUser_Email(sessionId, userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Workout session not found"));

        if (workoutSession.getExercises() != null) {
            workoutSession.getExercises().sort(Comparator.comparing(SessionExercise::getOrderIndex));
            for (SessionExercise sessionExercise : workoutSession.getExercises()) {
                if (sessionExercise.getSets() != null) {
                    sessionExercise.getSets().sort(Comparator.comparing(ExerciseSet::getSetNumber));
                }
            }
        }

        return workoutSession;
    }
}
