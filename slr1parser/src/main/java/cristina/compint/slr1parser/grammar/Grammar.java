package cristina.compint.slr1parser.grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String INTERNAL_NON_TERMINAL = "N";
	public static final Terminal EPS = new Terminal(">EPS<");
	public static final Terminal END_LINE = new Terminal(">$<");
	public static final NonTerminal AXIOM = new NonTerminal("AX"); 
	
	private Set<NonTerminal> nonTerminals;
	private Set<Terminal> terminals;
	private List<Production> productions;
	private Production axiomProduction;
	
	private int nonTerminalCounter;
	
	public Grammar() {
		super();
		this.nonTerminals = new HashSet<NonTerminal>();
		this.terminals = new HashSet<Terminal>();
		this.productions = new ArrayList<Production>();
		this.nonTerminalCounter = 1;
		this.addTerminal(EPS);
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

	public Production getAxiomProduction() {
		return axiomProduction;
	}

	public void setAxiomProduction(Production axiomProduction) {
		this.axiomProduction = axiomProduction;
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
	
	public NonTerminal getNewGrammarNonTerminal() {
		NonTerminal nt = new NonTerminal(INTERNAL_NON_TERMINAL + nonTerminalCounter++);
		
		while(nonTerminals.contains(nt)) 
			nt = new NonTerminal(INTERNAL_NON_TERMINAL + nonTerminalCounter++);
		
		this.addNonTerminal(nt);
		return nt;
	}
	
	public NonTerminal findNonTerminl(String label) {
		NonTerminal nt = null;
		for(NonTerminal n: this.nonTerminals) {
			if(n.getLabel().equals(label)) {
				nt = n;
				break;
			}
				
		}
			
		return nt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AXIOM == null) ? 0 : AXIOM.hashCode());
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
		if (axiomProduction == null) {
			if (other.axiomProduction != null)
				return false;
		} else if (!axiomProduction.equals(other.axiomProduction))
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
