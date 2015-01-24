package cristina.compint.slr1parser.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import cristina.compint.slr1parser.exception.ParserSintaxException;

public class GrammarUtils {

	public static Grammar convertToGrammar(List<String> grammarLines) throws ParserSintaxException {
		Grammar grammar = new Grammar();
		// Extract the NonTerminals from input lines

		for (String line: grammarLines){
			extractNonTerminal(grammar, line); 
		}

		for (String line: grammarLines){
			String lineToParse = line.substring(line.indexOf(":") + 1);
			extractProduction(grammar, lineToParse);
		}

		//TODO Add axiom
		return grammar;
	}

	private static void extractNonTerminal(Grammar grammar, String line) throws ParserSintaxException{
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

		Matcher matcher = NonTerminal.NON_TERMINAL_PATTERN.matcher(lineToParse);
		NonTerminal nt;
		String ntString;
		while (matcher.find()) {
			ntString = matcher.group(); 
			nt = new NonTerminal(ntString.substring(1, ntString.length()-1));
			grammar.addNonTerminal(nt);
		}

	}

	private static void extractProduction(Grammar grammar, String line) throws ParserSintaxException{

		String[] productionString = line.split(Production.ASSIGNMENT_STRING);

		if(productionString.length != 2)
			throw new ParserSintaxException("A production must be in format like <T> ::= <T>|a", line);

		String leftSide = productionString[0].replaceAll(" ", "");
		NonTerminal leftNt = new NonTerminal(leftSide.substring(1, leftSide.length() - 1));
		if(!grammar.getNonTerminals().contains(leftNt))
			throw new ParserSintaxException("Left side of production do not cotains a single non terminal.", line);

		Production p = new Production();

		p.setLeft(leftNt);
		p.setRight(getRightSide(grammar, leftNt, productionString[1]));

		grammar.addProduction(p);
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
				while(rightSide.charAt(i) != '>' && i < rightSide.length()) {
					i++;
				}
				if(i == rightSide.length()) 
					throw new ParserSintaxException("Unclosed non teminal", rightSide);

				NonTerminal nt = new NonTerminal(rightSide.substring(start, i));
				if(!grammar.getNonTerminals().contains(nt))
					throw new ParserSintaxException("Unespected non teminal " + nt.getLabel(), rightSide);
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
				pOr.setLeft(leftNt);
				pOr.setRight(getRightSide(grammar, leftNt, rightSide.substring(i+1)));
				grammar.addProduction(pOr);
				i = rightSide.length(); //FOR EXIT
			}
			break;
			case '(':
			{
				
				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '(', ')');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset + 1);
				System.out.println("Nested: " + nestedRighSide);
				NonTerminal newNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(newNt);
				Production pNested = new Production();
				pNested.setLeft(newNt);
				pNested.setRight(getRightSide(grammar, newNt, nestedRighSide));
				grammar.addProduction(pNested);
			}
			break;
			case '{':
			{
				
				
				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '{', '}');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset + 1);
				System.out.println("Nested: " + nestedRighSide);
				NonTerminal kleenNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(kleenNt);
				
				NonTerminal newNt = grammar.getNewGrammarNonTerminal();
				Production pNested = new Production();
				pNested.setLeft(newNt);
				pNested.setRight(getRightSide(grammar, newNt, nestedRighSide));
				grammar.addProduction(pNested);
				
				Production p1kleen = new Production();
				p1kleen.setLeft(kleenNt);
				p1kleen.addRightElement(newNt);
				p1kleen.addRightElement(kleenNt);
				grammar.addProduction(p1kleen);
				
				Production p2kleen = new Production();
				p2kleen.setLeft(kleenNt);
				p2kleen.addRightElement(Grammar.EPS);
				grammar.addProduction(p2kleen);
			}
			break;
			case '[':
			{
				i++;
				int offset = getParenthesisSubstringIndex(rightSide.substring(i), '{', '}');
				String nestedRighSide = rightSide.substring(i, i + offset);
				i += (offset + 1);
				
				NonTerminal oneOrZeroNt = grammar.getNewGrammarNonTerminal();
				rightSideElements.add(oneOrZeroNt);
				
				NonTerminal newNt = grammar.getNewGrammarNonTerminal();
				Production pNested = new Production();
				pNested.setLeft(newNt);
				pNested.setRight(getRightSide(grammar, newNt, nestedRighSide));
				grammar.addProduction(pNested);
				
				Production p1kleen = new Production();
				p1kleen.setLeft(oneOrZeroNt);
				p1kleen.addRightElement(newNt);
				grammar.addProduction(p1kleen);
				
				Production p2kleen = new Production();
				p2kleen.setLeft(oneOrZeroNt);
				p2kleen.addRightElement(Grammar.EPS);
				grammar.addProduction(p2kleen);
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
}
