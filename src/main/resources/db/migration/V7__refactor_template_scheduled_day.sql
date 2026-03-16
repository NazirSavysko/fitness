ALTER TABLE fitness_app.workout_template
    ADD COLUMN IF NOT EXISTS scheduled_day VARCHAR(16);

UPDATE fitness_app.workout_template wt
SET scheduled_day = source.scheduled_day
FROM (
         -- Legacy templates may have multiple scheduled days; keep a deterministic single value
         -- by selecting the lexicographically first enum string.
         SELECT workout_template_id,
                MIN(scheduled_days) AS scheduled_day
         FROM fitness_app.workout_template_scheduled_days
         GROUP BY workout_template_id
     ) source
WHERE wt.id = source.workout_template_id
  AND wt.scheduled_day IS NULL;

DROP TABLE IF EXISTS fitness_app.workout_template_scheduled_days;
