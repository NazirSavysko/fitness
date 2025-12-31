package fitness.app.project.fitnessapp.mapper;

@FunctionalInterface
public interface Mapper<R, T> {
   R mapEntityToDto(T t);
}
