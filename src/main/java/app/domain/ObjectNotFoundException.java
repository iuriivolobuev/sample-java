package app.domain;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(Class<?> objectType, String id) {
        super(String.format("Couldn't find object [%s] with id=[%s].", objectType.getSimpleName(), id));
    }
}
