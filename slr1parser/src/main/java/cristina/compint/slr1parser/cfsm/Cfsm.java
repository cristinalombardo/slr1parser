package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;
import java.util.List;

public class Cfsm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<State> states;

	public Cfsm() {
		super();
	}

	public Cfsm(List<State> states) {
		super();
		this.states = states;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public void addState(State state) {
		if(!states.contains(state))
			states.add(state);
	}

}
