package cristina.compint.slr1parser.grammar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Production implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String ASSIGNMENT_STRING = "::=";
	
	private NonTerminal left;
	private List<Element> right;
	
	public Production() {
		super();
		right = new ArrayList<Element>();
	}

	public NonTerminal getLeft() {
		return left;
	}

	public void setLeft(NonTerminal left) {
		this.left = left;
	}

	public List<Element> getRight() {
		return right;
	}

	public void setRight(List<Element> right) {
		this.right = right;
	}
	
	public void addRightElement (Element e){
		this.right.add(e);
	}
	
	public String toCompactString() {
		StringBuilder sb = new StringBuilder(left.toString());
		sb.append("->");
		for(Element e: right) {
			sb.append(e.toString());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(left.toString());
		sb.append("\t->\t");
		for(Element e: right) {
			sb.append(e.toString());
			sb.append(" ");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		Production other = (Production) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
	

}
