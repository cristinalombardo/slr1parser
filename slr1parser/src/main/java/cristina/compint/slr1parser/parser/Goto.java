package cristina.compint.slr1parser.parser;

import java.io.Serializable;

import cristina.compint.slr1parser.cfsm.State;

public class Goto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private State destState;

	public Goto(State destState) {
		super();
		this.destState = destState;
	}

	public State getDestState() {
		return destState;
	}

	public void setDestState(State destState) {
		this.destState = destState;
	}

	@Override
	public String toString() {
		return String.valueOf(destState.getState());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destState == null) ? 0 : destState.hashCode());
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
		Goto other = (Goto) obj;
		if (destState == null) {
			if (other.destState != null)
				return false;
		} else if (!destState.equals(other.destState))
			return false;
		return true;
	}
	
}
