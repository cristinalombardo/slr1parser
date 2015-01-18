package cristina.compint.slr1parser.grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String INTERNAL_NON_TERMINAL = "IN";
	public static final Terminal EPS = new Terminal("eps");
	
	private Set<NonTerminal> nonTerminals;
	private Set<Terminal> terminals;
	private List<Production> productions;
	private Production axiom;
	
	private int nonTerminalCounter;
	
	public Grammar() {
		super();
		this.nonTerminals = new HashSet<NonTerminal>();
		this.terminals = new HashSet<Terminal>();
		this.productions = new ArrayList<Production>();
		this.nonTerminalCounter = 1;
	}

	public Set<NonTerminal> getNonTerminals() {
		return nonTerminals;
	}

	public void setNonTerminals(Set<NonTerminal> nonTerminals) {
		this.nonTerminals = nonTerminals;
	}

	public Set<Terminal> getTerminals() {
		return terminals;
	}

	public void setTerminals(Set<Terminal> terminals) {
		this.terminals = terminals;
	}

	public List<Production> getProductions() {
		return productions;
	}

	public void setProductions(List<Production> productions) {
		this.productions = productions;
	}

	public Production getAxiom() {
		return axiom;
	}

	public void setAxiom(Production axiom) {
		this.axiom = axiom;
	}
	
	public void addProduction(Production p) {
		this.productions.add(p);
	}
	
	public void addTerminal(Terminal t) {
		this.terminals.add(t);
	}
	
	public void addNonTerminal (NonTerminal nt) {
		this.nonTerminals.add(nt);		
	}
	
	public NonTerminal getNewNonTerminal() {
		NonTerminal nt = new NonTerminal(INTERNAL_NON_TERMINAL + nonTerminalCounter++);
		
		while(nonTerminals.contains(nt)) 
			nt = new NonTerminal(INTERNAL_NON_TERMINAL + nonTerminalCounter++);
		
		this.addNonTerminal(nt);
		return nt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((axiom == null) ? 0 : axiom.hashCode());
		result = prime * result
				+ ((nonTerminals == null) ? 0 : nonTerminals.hashCode());
		result = prime * result
				+ ((productions == null) ? 0 : productions.hashCode());
		result = prime * result
				+ ((terminals == null) ? 0 : terminals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grammar other = (Grammar) obj;
		if (axiom == null) {
			if (other.axiom != null)
				return false;
		} else if (!axiom.equals(other.axiom))
			return false;
		if (nonTerminals == null) {
			if (other.nonTerminals != null)
				return false;
		} else if (!nonTerminals.equals(other.nonTerminals))
			return false;
		if (productions == null) {
			if (other.productions != null)
				return false;
		} else if (!productions.equals(other.productions))
			return false;
		if (terminals == null) {
			if (other.terminals != null)
				return false;
		} else if (!terminals.equals(other.terminals))
			return false;
		return true;
	}
	
}
