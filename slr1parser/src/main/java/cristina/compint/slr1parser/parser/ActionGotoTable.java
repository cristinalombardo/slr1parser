package cristina.compint.slr1parser.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cristina.compint.slr1parser.cfsm.State;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Terminal;

public class ActionGotoTable implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<State, ActionGotoRow> table;
	private State acceptState;

	public ActionGotoTable() {
		super();
		this.table = new HashMap<State, ActionGotoRow>();
	}

	public Map<State, ActionGotoRow> getTable() {
		return table;
	}

	public void setTable(Map<State, ActionGotoRow> table) {
		this.table = table;
	}

	public State getAcceptState() {
		return acceptState;
	}

	public void setAcceptState(State acceptState) {
		this.acceptState = acceptState;
	}

	public Action getAction(State s, Terminal t) {
		ActionGotoRow row = table.get(s);
		return row.getActionMap().get(t);
	}
	
	public Goto getGoto(State s, NonTerminal nt) {
		ActionGotoRow row = table.get(s);
		return row.getGotoMap().get(nt);
	}
	
	public void addAction(State s, Terminal t, Action a) {
		ActionGotoRow row = table.get(s);
		if(row == null) {
			row = new ActionGotoRow();
			table.put(s, row);
		}
		row.addAction(t, a);
	}
	
	public void addGoto(State s, NonTerminal nt, Goto g) {
		ActionGotoRow row = table.get(s);
		if(row == null) {
			row = new ActionGotoRow();
			table.put(s, row);
		}
		row.addGoto(nt, g);
	}
}
