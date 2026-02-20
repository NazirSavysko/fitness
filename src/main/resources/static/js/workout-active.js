function selectExercise(exerciseId) {
    if (!exerciseId) return;

    sessionStorage.setItem('activeExerciseId', exerciseId);

    const exerciseListPanel = document.getElementById('exerciseListPanel');
    const exerciseDetailPanel = document.getElementById('exerciseDetailPanel');

    document.querySelectorAll('.exercise-detail').forEach(function (detail) {
        if (detail.dataset.exerciseId === String(exerciseId)) {
            detail.style.display = 'block';
        } else {
            detail.style.display = 'none';
        }
    });

    document.querySelectorAll('[data-exercise-id].exercise-item-card').forEach(function (item) {
        if (item.dataset.exerciseId === String(exerciseId)) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    if (exerciseListPanel) exerciseListPanel.classList.remove('hidden-mobile');
    if (exerciseDetailPanel) exerciseDetailPanel.classList.remove('hidden-mobile');

    if (window.matchMedia('(max-width: 768px)').matches) {
        if (exerciseListPanel) exerciseListPanel.classList.add('hidden-mobile');
    }
}

function showExerciseList() {
    const exerciseListPanel = document.getElementById('exerciseListPanel');
    if (exerciseListPanel) {
        exerciseListPanel.classList.remove('hidden-mobile');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    let targetId = (typeof serverSelectedExerciseId !== 'undefined' && serverSelectedExerciseId)
                   ? String(serverSelectedExerciseId)
                   : null;

    if (!targetId) {
        targetId = sessionStorage.getItem('activeExerciseId');
    }

    if (!targetId) {
        const firstExercise = document.querySelector('[data-exercise-id].exercise-item-card');
        if (firstExercise) {
            targetId = firstExercise.dataset.exerciseId;
        }
    }

    if (targetId) {
        const exists = document.querySelector(`.exercise-detail[data-exercise-id="${targetId}"]`);
        if (exists) {
            selectExercise(targetId);
        } else {
            // Fallback: if stored ID is invalid (e.g. exercise deleted), select first available
            const first = document.querySelector('[data-exercise-id].exercise-item-card');
            if (first) selectExercise(first.dataset.exerciseId);
        }
    }
});
