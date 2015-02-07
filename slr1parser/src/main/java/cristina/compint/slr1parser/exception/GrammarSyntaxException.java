package cristina.compint.slr1parser.exception;

public class GrammarSyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String line;

	public GrammarSyntaxException(String message, String line) {
		super(message);
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
}
