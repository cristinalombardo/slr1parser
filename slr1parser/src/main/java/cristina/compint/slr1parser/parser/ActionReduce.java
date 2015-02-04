package cristina.compint.slr1parser.parser;

import cristina.compint.slr1parser.grammar.Production;

public class ActionReduce extends Action {

	private static final long serialVersionUID = 1L;

	private Production production;

	public ActionReduce(Production production) {
		super();
		this.production = production;
	}

	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}
	
	@Override
	public String toString() {
		return production.toCompactString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((production == null) ? 0 : production.hashCode());
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
		ActionReduce other = (ActionReduce) obj;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}
	
	
}
