package rbc.petstore;

public class PetCategoryNotExistsException extends RuntimeException {

    public PetCategoryNotExistsException(final String message) {
        super(message);
    }
}
