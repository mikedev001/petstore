package rbc.petstore;

public class PetTagNotExistsException extends RuntimeException {

    public PetTagNotExistsException(final String message) {
        super(message);
    }
}
