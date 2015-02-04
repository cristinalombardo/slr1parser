package cristina.compint.slr1parser.cfsm;

import java.util.ArrayList;
import java.util.List;

import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.grammar.Element;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Production;
import cristina.compint.slr1parser.grammar.Terminal;

public class CfsmUtils {

	public static Cfsm createCfsm(Grammar g) throws CfsmException {
		Cfsm cfsm = new Cfsm();

		Candidate firstCandidate = new Candidate(g.getAxiomProduction(), 0);
		State firstState = new State();
		List<Candidate> closureList = new ArrayList<Candidate>();
		closure(closureList, g, firstCandidate);
		firstState.addAll(closureList);
		
		createStateAndTransanction(cfsm, g, firstState);

		return cfsm;
	}
	
	private static void createStateAndTransanction(Cfsm cfsm, Grammar g, State state) throws CfsmException {
		checkShiftReduce(state, g);
		if(cfsm.addState(state)) {
			State destState = null;
			for(Terminal t : g.getTerminals()) {
				destState = getDestState(state, t, g);
				if(destState!= null){
					destState = cfsm.getInnerSate(destState);
					Transition tr = new Transition(t, state, destState);
					cfsm.addTransition(tr);
					createStateAndTransanction(cfsm, g, destState);
				}
			}
			
			for(NonTerminal nt : g.getNonTerminals()) {
				destState = getDestState(state, nt, g);
				if(destState != null) {
					destState = cfsm.getInnerSate(destState);
					Transition tr = new Transition(nt, state, destState);
					cfsm.addTransition(tr);
					createStateAndTransanction(cfsm, g, destState);
				}
			}
			
		}
	}
	
	private static void checkShiftReduce(State state, Grammar g) throws CfsmException {
		List<Terminal> shiftTerminals = new ArrayList<Terminal>();
		List<Terminal> reduceTerminals = new ArrayList<Terminal>();
		for(Candidate c: state.getCandidates()) {
			if(c.getCandidateElement() == null) { //Reduction
				if(!c.getProduction().getLeft().equals(Grammar.AXIOM)) {
					NonTerminal nt = g.findNonTerminl(c.getProduction().getLeft().getLabel());
					reduceTerminals.addAll(nt.getFollow());
				}
			} else if(c.getCandidateElement() instanceof Terminal) { //Terminal shift
				shiftTerminals.add((Terminal) c.getCandidateElement());
			} else { //Non Terminal Shift
				NonTerminal nt = g.findNonTerminl(c.getCandidateElement().getLabel());
				shiftTerminals.addAll(nt.getFirst());
			}
		}
		
		if(reduceTerminals.size() > 0) {
			for(Terminal st: shiftTerminals) {
				if(reduceTerminals.contains(st)) {
					throw new CfsmException("Unresolvable shift reduce problem for state: \n" + state);
				}
			}
		}
	}
	private static State getDestState(State state, Element e,
			Grammar g) throws CfsmException {
		State newState = null;
		for(Candidate c: state.getCandidates()) {
			if(c.getCandidateElement() != null && c.getCandidateElement().equals(e)) {
				Candidate newCandidate = new Candidate(c.getProduction(), c.getIndex() + 1);
				if(newState == null)
					newState = new State();
				List<Candidate> closureList = new ArrayList<Candidate>();
				closure(closureList, g, newCandidate);
				newState.addAll(closureList);
			}
		}
		
		return newState;
	}
	
	public static void closure(List<Candidate> closure, Grammar g, Candidate c) throws CfsmException {
		closure.add(c);
		if(c.getCandidateElement() instanceof NonTerminal) {
			for(Production p: g.getProductions()) {
				if(p.getLeft().equals(c.getCandidateElement())) {
					Candidate nc = new Candidate(p, 0);
					if(!closure.contains(nc)){
						closure(closure, g, nc);
					}
				}
			}
		}
	}
}
