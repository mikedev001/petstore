package rbc.petstore;

public class PetNotExistsException extends RuntimeException {

    public PetNotExistsException(final String message) {
        super(message);
    }
}
