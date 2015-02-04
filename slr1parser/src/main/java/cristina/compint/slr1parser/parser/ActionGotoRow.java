package cristina.compint.slr1parser.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Terminal;

public class ActionGotoRow implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Terminal, Action> actionMap;
	private Map<NonTerminal, Goto> gotoMap;
	
	public ActionGotoRow() {
		super();
		this.actionMap = new HashMap<Terminal, Action>();
		this.gotoMap = new HashMap<NonTerminal, Goto>();
	}

	public Map<Terminal, Action> getActionMap() {
		return actionMap;
	}
	
	public void setActionMap(Map<Terminal, Action> actionMap) {
		this.actionMap = actionMap;
	}
	
	public Map<NonTerminal, Goto> getGotoMap() {
		return gotoMap;
	}
	
	public void setGotoMap(Map<NonTerminal, Goto> gotoMap) {
		this.gotoMap = gotoMap;
	}
	
	public void addAction(Terminal t, Action a) {
		this.actionMap.put(t, a);
	}
	
	public void addGoto(NonTerminal nt, Goto g) {
		this.gotoMap.put(nt, g);
	}
	
	
}
