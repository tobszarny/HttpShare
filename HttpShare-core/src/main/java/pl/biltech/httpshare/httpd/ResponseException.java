package pl.biltech.httpshare.httpd;

public class ResponseException extends Exception {

    private static final long serialVersionUID = 6569838532917408380L;

    private final ResponseStatus status;

    public ResponseException(ResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ResponseException(ResponseStatus status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return this.status;
    }
}
