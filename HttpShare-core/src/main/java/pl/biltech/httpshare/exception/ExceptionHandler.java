package pl.biltech.httpshare.exception;

public interface ExceptionHandler {

	public void handle(String additionalMessage, Exception e);
}
