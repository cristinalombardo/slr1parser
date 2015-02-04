package cristina.compint.slr1parser.parser;

import java.io.Serializable;

import cristina.compint.slr1parser.cfsm.State;
import cristina.compint.slr1parser.grammar.Element;

public class StackElement implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private State state;
	private Element element;
	
	public StackElement(State state, Element element) {
		super();
		this.state = state;
		this.element = element;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return element + " " + state.getState();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		StackElement other = (StackElement) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	
}
