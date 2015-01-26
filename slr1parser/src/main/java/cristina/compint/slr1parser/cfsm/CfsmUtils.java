package cristina.compint.slr1parser.cfsm;

import java.util.SortedSet;

import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Production;

public class CfsmUtils {

	public static SortedSet<Candidate> closure(SortedSet<Candidate> closure, Grammar g, Candidate c) throws CfsmException {
		closure.add(c);
		if(c.getCandidateElement() instanceof NonTerminal) {
			for(Production p: g.getProductions()) {
				if(p.getLeft().equals(c.getCandidateElement())) {
					Candidate nc = new Candidate(p, 0);
					if(!closure.contains(nc)){
						closure.addAll(closure(closure, g, nc));
					}
				}
			}
		}
		return closure;
	}
}
