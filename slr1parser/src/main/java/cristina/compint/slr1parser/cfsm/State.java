package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {

	private static final long serialVersionUID = 1L;

	private int state;
	private List<Candidate> candidates;

	public State() {
		super();
		this.candidates = new ArrayList<Candidate>();
	}

	public State(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public void addAll(List<Candidate> closure) {
		for(Candidate c:closure) {
			if(!candidates.contains(c))
				candidates.add(c);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("S");
		sb.append(state);
		sb.append(": {\n");
		for(Candidate c: candidates) {
			sb.append("\t");
			sb.append(c.toString());
			sb.append("\n");
		}
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((candidates == null) ? 0 : candidates.hashCode());
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
		if (candidates == null) {
			if (other.candidates != null)
				return false;
		} else if (!candidates.equals(other.candidates))
			return false;
		return true;
	}

}
