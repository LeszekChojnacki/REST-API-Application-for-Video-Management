package pl.nowekolory.REST.exception;

public class ResponseNullException extends IllegalArgumentException {
    public ResponseNullException(String msg) {
        super(msg);
    }
}
