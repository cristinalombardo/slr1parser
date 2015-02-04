package cristina.compint.slr1parser.parser;

import cristina.compint.slr1parser.cfsm.Candidate;
import cristina.compint.slr1parser.cfsm.Cfsm;
import cristina.compint.slr1parser.cfsm.State;
import cristina.compint.slr1parser.cfsm.Transition;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Terminal;

public class ActionGotoUtils {

	public static ActionGotoTable createTable(Cfsm cfsm, Grammar g) {
		
		ActionGotoTable table = new ActionGotoTable();
		for(Transition tr: cfsm.getTransitions()) {
			if(tr.getE() instanceof NonTerminal) {
				Goto gt = new Goto(tr.getS2());
				table.addGoto(tr.getS1(), (NonTerminal) tr.getE(), gt);
			} else {
				Action a = new ActionShift(tr.getS2());
				table.addAction(tr.getS1(), (Terminal) tr.getE(), a);
				
			}
		}
		
		for(State s: cfsm.getStates()) {
			for(Candidate c: s.getCandidates()) {
				if(c.getCandidateElement() == null) { //Reduction
					if(!c.getProduction().getLeft().equals(Grammar.AXIOM)) {
						NonTerminal nt = g.findNonTerminl(c.getProduction().getLeft().getLabel());
						for(Terminal t: nt.getFollow()) {
							Action a = new ActionReduce(c.getProduction());
							table.addAction(s, t, a);
						}
					}
				} 
			}
			if(Grammar.END_LINE.equals(s.getCandidates().get(0).getCandidateElement())) {
				table.setAcceptState(s);
			}
		}
		return table;
	}
}
