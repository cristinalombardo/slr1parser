package cristina.compint.slr1parser.grammar;

import java.util.regex.Pattern;

public class NonTerminal extends Element {
	
	private static final long serialVersionUID = 1L;

	public static final String NON_TERMINAL_PATTERN_STRING = "<[a-zA-Z][a-zA-Z]*[0-9]*>";
	
	public static final Pattern NON_TERMINAL_PATTERN = Pattern.compile(NON_TERMINAL_PATTERN_STRING);

	public NonTerminal() {
		super();
	}

	public NonTerminal(String label) {
		super(label);
	}
	
	
}
