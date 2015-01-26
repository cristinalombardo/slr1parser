package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class State implements Serializable {

	private static final long serialVersionUID = 1L;

	private int state;
	private SortedSet<Candidate> closure;

	public State(int state) {
		super();
		this.state = state;
		this.closure = new TreeSet<Candidate>();
	}

	public State(int state, SortedSet<Candidate> closure) {
		this(state);
		this.closure = closure;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public SortedSet<Candidate> getClosure() {
		return closure;
	}

	public void setClosure(SortedSet<Candidate> closure) {
		this.closure = closure;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((closure == null) ? 0 : closure.hashCode());
		result = prime * result + state;
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
		State other = (State) obj;
		if (closure == null) {
			if (other.closure != null)
				return false;
		} else if (!closure.equals(other.closure))
			return false;
		if (state != other.state)
			return false;
		return true;
	}

}
