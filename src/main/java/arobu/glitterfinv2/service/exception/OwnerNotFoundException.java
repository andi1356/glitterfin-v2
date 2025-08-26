package arobu.glitterfinv2.service.exception;

public class OwnerNotFoundException extends Throwable {
    public OwnerNotFoundException(String owner) {
        super("EXCEPTION: Owner not found: " + owner);
    }
}
