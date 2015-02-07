package cristina.compint.slr1parser.grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cristina.compint.slr1parser.exception.ParserSintaxException;

public class GrammarUtils {

	
	public static Grammar convertToGrammar(List<String> grammarLines) throws ParserSintaxException {
		Grammar grammar = new Grammar();
		//Extract production
		for (String line: grammarLines){
			extractProduction(grammar, line);
		}

		//Axiom creation
		Production axiomProduction = new Production();
		axiomProduction.setLeft(Grammar.AXIOM);
		axiomProduction.addRightElement(grammar.getProductions().get(0).getLeft());
		axiomProduction.addRightElement(Grammar.END_LINE);

		grammar.setAxiomProduction(axiomProduction);

		//Calculate first and follow
		calculateFirstAndFollow(grammar);

		return grammar;
	}

	private static void extractProduction(Grammar grammar, String line) throws ParserSintaxException{

		int lineSeparatorIndex = line.indexOf(":");

		if (lineSeparatorIndex < 1) {
			throw new ParserSintaxException("The line do not start with line number.", line);
		}

		String lineNumber = line.substring(0, lineSeparatorIndex);
		try {
			Integer.parseInt(lineNumber);
		} catch(NumberFormatException e) {
			throw new ParserSintaxException("The line do not start with line number.", line);
		}

		String lineToParse = line.substring(lineSeparatorIndex + 1);

		String[] productionString = lineToParse.split(Production.ASSIGNMENT_STRING);

		if(productionString.length != 2)
			throw new ParserSintaxException("A production must be in format like <T> ::= <T>|a", line);

		String leftSide = productionString[0].replaceAll(" ", "");


		String ntLabel = leftSide.substring(1, leftSide.length() - 1);
		if(!NonTerminal.NON_TERMINAL_PATTERN.matcher(ntLabel).matches())
			throw new ParserSintaxException("Unespected non teminal " + ntLabel, line);
		NonTerminal leftNt = new NonTerminal(ntLabel);
		grammar.addNonTerminal(leftNt);

		Production p = new Production();
		grammar.addProduction(p);
		p.setLeft(leftNt);
		p.setRight(getRightSide(grammar, leftNt, productionString[1]));


	}

	private static List<Element> getRightSide(Grammar grammar, NonTerminal leftNt, String rightSide) throws ParserSintaxException {
		List<Element> rightSideElements = new ArrayList<Element>();

		for(int i = 0; i < rightSide.length(); i++) {
			switch (rightSide.charAt(i)) {
			case ' ':
				break;
			case '<':
				i++;
				int start = i;
				while(i < rightSide.length() && rightSide.charAt(i) != '>') {
					i++;
				}

				if(i >= rightSide.length()) 
					throw new ParserSintaxException("Unclosed non teminal", rightSide);

				String ntLabel = rightSide.substring(start, i);
				if(!NonTerminal.NON_TERMINAL_PATTERN.matcher(ntLabel).matches())
					throw new ParserSintaxException("Unespected non teminal " + ntLabel, rightSide);

				NonTerminal nt = grammar.findNonTerminl(ntLabel);
				if(nt == null) {
					nt = new NonTerminal(ntLabel);
					grammar.addNonTerminal(nt);
				}
				rightSideElements.add(nt);
				break;
			case '>':
			case ':':
			case '=':
			case ')':
			case '}':
			case ']':
				throw new ParserSintaxException("Unespected unescaped symbol " + rightSide.charAt(i), rightSide);
			case '|':
			{
				Production pOr = new Production();
				grammar.addProduction(pOr);
				pOr.setLeft(leftNt);
				pOr.setRight(getRightSide(grammar, leftNt, rightSide.substring(i+1)));

				i = rightSide.length(); //FOR EXIT
			}
			break;
			case '(':
			{

				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '(', ')');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset);
				NonTerminal newNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(newNt);
				Production pNested = new Production();
				grammar.addProduction(pNested);
				pNested.setLeft(newNt);
				pNested.setRight(getRightSide(grammar, newNt, nestedRighSide));
			}
			break;
			case '{':
			{
				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '{', '}');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset);
				NonTerminal kleenNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(kleenNt);

				Production p2kleen = new Production();

				p2kleen.setLeft(kleenNt);
				p2kleen.addRightElement(Grammar.EPS);

				Production p1kleen = new Production();

				grammar.addProduction(p1kleen);
				grammar.addProduction(p2kleen);

				p1kleen.setLeft(kleenNt);
				p1kleen.setRight(getRightSide(grammar, kleenNt, nestedRighSide));
				p1kleen.addRightElement(kleenNt);


			}
			break;
			case '[':
			{
				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '[', ']');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset);

				NonTerminal oneOrZeroNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(oneOrZeroNt);

				Production p2oneOrZero = new Production();
				p2oneOrZero.setLeft(oneOrZeroNt);
				p2oneOrZero.addRightElement(Grammar.EPS);

				Production p1oneOrZero = new Production();
				grammar.addProduction(p1oneOrZero);
				grammar.addProduction(p2oneOrZero);
				p1oneOrZero.setLeft(oneOrZeroNt);
				p1oneOrZero.setRight(getRightSide(grammar, oneOrZeroNt, nestedRighSide));



			}
			break;
			case '\\':
				i++;
				if(i == rightSide.length()) 
					throw new ParserSintaxException("Unespected symbol \\", rightSide);
			default:
				Terminal terminal = new Terminal(rightSide.substring(i, i+1));
				grammar.addTerminal(terminal);
				rightSideElements.add(terminal);
				break;
			}
		}

		return rightSideElements;
	}

	public static int getParenthesisSubstringIndex(String source, char open, char close) throws ParserSintaxException {
		int parenthesisCount = 1;
		int end = -1;
		for(int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			if( c == open)
				parenthesisCount++;
			if( c == close )
				parenthesisCount--;
			if(parenthesisCount == 0){
				end = i;
				break;
			}
		}
		if(end == -1)
			throw new ParserSintaxException("Could not find end parenthesy of " + open, source);

		return end;
	}

	public static void calculateFirstAndFollow(Grammar grammar) {
		for(NonTerminal nt: grammar.getNonTerminals()) {
			nt.setFirst(first(grammar, nt));
		}
		for(NonTerminal nt: grammar.getNonTerminals()) {
			nt.setFollow(follow(grammar, nt, new ArrayList<NonTerminal>()));
		}
	}

	public static Set<Terminal> first(Grammar grammar, NonTerminal nt) {
		if ( nt.getFirst() != null )
			return nt.getFirst();

		Set<Terminal> firstSet = new HashSet<Terminal>();
		for (int i = 0; i < grammar.getProductions().size(); i++) {
			Production p = grammar.getProductions().get(i);
			if(p.getLeft().equals(nt)) {
				for(Element e: p.getRight()) {
					if(e instanceof Terminal) {
						firstSet.add((Terminal) e);
						break;
					} else {
						NonTerminal nt1 = (NonTerminal) e;
						if (nt1.getFirst() == null) {
							nt1.setFirst(first(grammar, nt1));
						}
						Set<Terminal> nt1First = new HashSet<Terminal>(nt1.getFirst()); 
						if(i < (grammar.getProductions().size() -1)) {
							nt1First.remove(Grammar.EPS);
						}
						firstSet.addAll(nt1First);
						if(!nt1.getFirst().contains(Grammar.EPS)) {
							break;
						}
					}
				}
			}
		}
		return firstSet;
	}

	public static Set<Terminal> follow(Grammar grammar, NonTerminal nt, List<NonTerminal> trace) {
		if ( nt.getFollow() != null)
			return nt.getFollow();


		Set<Terminal> followSet = new HashSet<Terminal>();
		if(trace.contains(nt)) {
			for(NonTerminal ntrace: trace) {
				if(ntrace.getFollow() != null) {
					followSet.addAll(ntrace.getFollow());
				}
			}
			return followSet;
		}
		trace.add(nt);

		List<Production> productions = new ArrayList<Production>(grammar.getProductions());
		productions.add(grammar.getAxiomProduction());

		for ( Production p: productions) {

			for(int index= 0; index < p.getRight().size(); index++) {
				//			int index = p.getRight().indexOf(nt);
				if(p.getRight().get(index).equals(nt)) {
					if( index == p.getRight().size() - 1) {
						NonTerminal nt1 = p.getLeft();
						if(nt1.equals(nt))
							continue;
						if( nt1.getFollow() == null) {
							nt1.setFollow(follow(grammar, nt1, trace));
						}

						followSet.addAll(nt1.getFollow());

					} else {
						for(int i = (index + 1); i < p.getRight().size(); i++) {
							Element e = p.getRight().get(i);
							if(e instanceof Terminal) {
								followSet.add((Terminal) e);
								break;
							} else {
								NonTerminal nt1 = (NonTerminal) e;
								followSet.addAll(nt1.getFirst());
								if ( nt1.getFirst().contains(Grammar.EPS) ) {

									if( i == (p.getRight().size() - 1) ) {
										NonTerminal nt2 = p.getLeft();
										if(nt2.equals(nt))
											continue;
										if( nt2.getFollow() == null) {
											nt2.setFollow(follow(grammar, nt1, trace));
										}
										followSet.addAll(nt2.getFollow());
									}
								} else {
									break;
								}
							}	
						}
					}
				}
			}
		}
		return followSet;

	}
}
