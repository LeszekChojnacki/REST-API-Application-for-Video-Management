package pl.nowekolory.REST.exception;

public class ResponseBodyFormatException extends RuntimeException {
    public ResponseBodyFormatException(String msg) {
        super(msg);
    }
}
