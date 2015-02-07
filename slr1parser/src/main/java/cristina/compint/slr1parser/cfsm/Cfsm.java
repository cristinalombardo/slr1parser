package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cfsm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<State> states;
	private List<Transition> transitions;
	private int stateCounter;
	

	public Cfsm() {
		super();
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.stateCounter = 0;
	}

	public List<State> getStates() {
		return states;
	}

	public boolean addState(State state) {
		boolean isInsert = false;
		if(!states.contains(state)) {
			state.setState(stateCounter);
			stateCounter++;
			isInsert = states.add(state);
		}
		return isInsert;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
	
	public boolean addTransition(Transition transition) {
		boolean isInsert = false;
		if(!transitions.contains(transition))
			isInsert = transitions.add(transition);
		return isInsert;
	}
	
	public State getInnerSate(State s) {
		int index = this.states.indexOf(s);
		if(index > 0)
			return this.states.get(index);
		return s;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Transition t: transitions) {
			sb.append("====================================\n");
			sb.append(t.getS1());
			sb.append("------------------------------------\n");
			sb.append("\t >>> " + t.getE() + " >>>");
			sb.append("\n------------------------------------\n");
			sb.append(t.getS2());
			
			
		}
		sb.append("====================================\n");
		return sb.toString();
	}

	

}
