package it.prova.triage_be.web.api.exception;

public class PazienteNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PazienteNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public PazienteNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PazienteNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public PazienteNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PazienteNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
