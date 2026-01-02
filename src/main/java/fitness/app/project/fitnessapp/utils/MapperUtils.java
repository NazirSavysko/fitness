package fitness.app.project.fitnessapp.utils;

import fitness.app.project.fitnessapp.mapper.Mapper;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class MapperUtils {

    private MapperUtils() {
    }

    public static <T, R> @Unmodifiable @NonNull List<R> mapList(final @NonNull List<T> sourceList, final @NonNull Mapper<R, T> mapperFunction) {
        return sourceList.stream()
                .map(mapperFunction::mapEntityToDto)
                .toList();
    }
}
