package app.domain;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(Class<?> objectType, long id) {
        super(String.format("Couldn't find object [%s] with id=[%d].", objectType.getSimpleName(), id));
    }
}
