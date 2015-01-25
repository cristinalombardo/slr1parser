package cristina.compint.slr1parser.grammar;

import java.util.Set;
import java.util.regex.Pattern;

public class NonTerminal extends Element {
	
	private static final long serialVersionUID = 1L;

	public static final String NON_TERMINAL_PATTERN_STRING = "[a-zA-Z]+[0-9]*";
	
	public static final Pattern NON_TERMINAL_PATTERN = Pattern.compile(NON_TERMINAL_PATTERN_STRING);
	
	private Set<Terminal> first;
	private Set<Terminal> follow;
	
	public NonTerminal(String label) {
		super(label);
	}

	public Set<Terminal> getFirst() {
		return first;
	}

	public void setFirst(Set<Terminal> first) {
		this.first = first;
	}

	public void addFirstElement(Terminal t) {
		this.first.add(t);
	}
	
	public Set<Terminal> getFollow() {
		return follow;
	}

	public void setFollow(Set<Terminal> follow) {
		follow.remove(Grammar.EPS);
		this.follow = follow;
		
	}
	
	public void addFollowElement(Terminal t) {
		this.follow.add(t);
	}

}
