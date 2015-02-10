package cristina.compint.slr1parser.cfsm;

import java.io.Serializable;

import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.grammar.Element;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.Production;

public class Candidate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Production production;
	private final int index;
	
	public Candidate(Production production, int index) throws CfsmException {
		super();
		if( index > production.getRight().size() || index < 0)
			throw new CfsmException("Wrong candidate index " + index + " for production " + production);
		this.production = production;
		this.index = index;
		
	}

	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}

	public int getIndex() {
		return index;
	}
	
	public Element getCandidateElement() {
		if(index == this.production.getRight().size())
			return null;  //Reduction candidate
		Element e = this.production.getRight().get(index);
		if(e.equals(Grammar.EPS))
			return null; //Reduction candidate 
		return this.production.getRight().get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.production.getLeft().toString());
		sb.append("\t->\t");
		for(int i=0; i< this.production.getRight().size(); i++) {
			Element e = this.production.getRight().get(i);
			if( i == index)
				sb.append("° ");
			sb.append(e.toString());
			sb.append(" ");
		}
		if(index == this.production.getRight().size())
			sb.append("°");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Candidate other = (Candidate) obj;
		if (index != other.index)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}
	
	

}
