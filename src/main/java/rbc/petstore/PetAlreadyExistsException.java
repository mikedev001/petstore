package rbc.petstore;

public class PetAlreadyExistsException extends RuntimeException {

    public PetAlreadyExistsException(final String message) {
        super(message);
    }
}
