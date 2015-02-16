package cristina.compint.slr1parser.cfsm;

import java.util.ArrayList;
import java.util.List;

import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.grammar.Element;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Production;
import cristina.compint.slr1parser.grammar.Terminal;

/**
 * The Class CfsmUtils. 
 * This class allows to calculate the Characteristic Finite State Machine of a grammar.
 */
public class CfsmUtils {

	
	/**
	 * Creates the CFSM of a Grammar
	 *
	 * @param g the grammar
	 * @return the cfsm of a grammar
	 * @throws CfsmException the cfsm exception
	 */
	public static Cfsm createCfsm(Grammar g) throws CfsmException {
		Cfsm cfsm = new Cfsm();

		//Create axiom candidate with 0 index
		Candidate firstCandidate = new Candidate(g.getAxiomProduction(), 0);
		
		//Make the closure of first candidate
		List<Candidate> closureList = new ArrayList<Candidate>();
		closure(closureList, g, firstCandidate);
		
		//Add the closure to Cfsm first state
		State firstState = new State();
		firstState.addAll(closureList);
		
		createStateAndTransaction(cfsm, g, firstState);

		return cfsm;
	}
	
	/**
	 * Creates the state and transaction recursively.
	 *
	 * @param cfsm the cfsm
	 * @param g the grammar
	 * @param state the start state
	 * @throws CfsmException the cfsm exception
	 */
	private static void createStateAndTransaction(Cfsm cfsm, Grammar g, State state) throws CfsmException {
		
		//If there are some non-resolvable shift-reduce ambiguity throw exception  
		checkShiftReduce(state);
		
		if(cfsm.addState(state)) {
			State destState = null;
			for(Terminal t : g.getTerminals()) {
				destState = getDestState(state, t, g);
				if(destState!= null){
					destState = cfsm.getInnerSate(destState);
					Transition tr = new Transition(t, state, destState);
					cfsm.addTransition(tr);
					createStateAndTransaction(cfsm, g, destState);
				}
			}
			
			destState = getDestState(state, Grammar.END_LINE, g);
			if(destState!= null){
				destState = cfsm.getInnerSate(destState);
				Transition tr = new Transition(Grammar.END_LINE, state, destState);
				cfsm.addTransition(tr);
				createStateAndTransaction(cfsm, g, destState);
			}
			
			for(NonTerminal nt : g.getNonTerminals()) {
				destState = getDestState(state, nt, g);
				if(destState != null) {
					destState = cfsm.getInnerSate(destState);
					Transition tr = new Transition(nt, state, destState);
					cfsm.addTransition(tr);
					createStateAndTransaction(cfsm, g, destState);
				}
			}
		}
	}
	
	/**
	 * Check if a state present an shift-reduce problem that cannot eliminate. 
	 *
	 * @param state the state to check
	 * @throws CfsmException the cfsm exception
	 */
	private static void checkShiftReduce(State state) throws CfsmException {
		List<Terminal> shiftTerminals = new ArrayList<Terminal>();
		List<Terminal> reduceTerminals = new ArrayList<Terminal>();
		for(Candidate c: state.getCandidates()) {
			if(c.getCandidateElement() == null) { //Reduction
				if(!c.getProduction().getLeft().equals(Grammar.AXIOM)) {
					NonTerminal nt = c.getProduction().getLeft();  
					reduceTerminals.addAll(nt.getFollow());
				}
			} else if(c.getCandidateElement() instanceof Terminal) { //Terminal shift
				shiftTerminals.add((Terminal) c.getCandidateElement());
			} else { //Non Terminal Shift
				NonTerminal nt = (NonTerminal) c.getCandidateElement();
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
	
	/**
	 * Gets the destination state from the input state according to the element e.
	 *
	 * @param state the start state
	 * @param e the element that produce a transaction
	 * @param g the grammar
	 * @return the destination state
	 * @throws CfsmException the cfsm exception
	 */
	private static State getDestState(State state, Element e,
			Grammar g) throws CfsmException {
		State newState = null;
		for(Candidate c: state.getCandidates()) {
			if(c.getCandidateElement() != null && c.getCandidateElement().equals(e)) {
				Candidate newCandidate = new Candidate(c.getProduction(), c.getIndex() + 1);
				if(newState == null) {
					newState = new State();
				}
				List<Candidate> closureList = new ArrayList<Candidate>();
				closure(closureList, g, newCandidate);
				newState.addAll(closureList);
			}
		}
		
		return newState;
	}
	
	/**
	 * Closure. Performs the closure of a candidate recursively
	 *
	 * @param closure the closure list
	 * @param g the grammar
	 * @param c the input candidate
	 * @throws CfsmException the cfsm exception
	 */
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
