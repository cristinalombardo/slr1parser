package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;

import cristina.compint.slr1parser.grammar.Element;

public class Transition implements Serializable {

	private static final long serialVersionUID = 1L;
	private Element e;
	private State s1;
	private State s2;
	
	public Transition(Element e, State s1, State s2) {
		super();
		this.e = e;
		this.s1 = s1;
		this.s2 = s2;
	}

	public Element getE() {
		return e;
	}

	public void setE(Element e) {
		this.e = e;
	}

	public State getS1() {
		return s1;
	}

	public void setS1(State s1) {
		this.s1 = s1;
	}

	public State getS2() {
		return s2;
	}

	public void setS2(State s2) {
		this.s2 = s2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
		result = prime * result + ((s2 == null) ? 0 : s2.hashCode());
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
		Transition other = (Transition) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (s1 == null) {
			if (other.s1 != null)
				return false;
		} else if (!s1.equals(other.s1))
			return false;
		if (s2 == null) {
			if (other.s2 != null)
				return false;
		} else if (!s2.equals(other.s2))
			return false;
		return true;
	}
	
}
