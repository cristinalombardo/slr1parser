package cristina.compint.slr1parser.exception;

public class CfsmException extends Exception{

	private static final long serialVersionUID = 1L;

	public CfsmException() {
		super();
	}

	public CfsmException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CfsmException(String message, Throwable cause) {
		super(message, cause);
	}

	public CfsmException(String message) {
		super(message);
	}

	public CfsmException(Throwable cause) {
		super(cause);
	}

}
