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
		p.setRight(getRightSide(grammar, productionString[1]));
		
		grammar.addProduction(p);
	}
	
	private static List<Element> getRightSide(Grammar grammar, String rightSide) throws ParserSintaxException {
		List<Element> rightSideElements = new ArrayList<Element>();
		
		for(int i = 0; i < rightSide.length(); i++) {
			switch (rightSide.charAt(i)) {
			case ' ':
				break;
			case '<':
				i++;
				int start = i;
				while(rightSide.charAt(i) == '>' && i < rightSide.length()) {
					i++;
				}
				if(i == rightSide.length()) 
					throw new ParserSintaxException("Unclosed non teminal", rightSide);
				
				NonTerminal nt = new NonTerminal(rightSide.substring(start, i));
				if(!grammar.getNonTerminals().contains(nt))
					throw new ParserSintaxException("Unespected non teminal", rightSide);
				rightSideElements.add(nt);
				break;
			case '>':
			case ':':
			case '=':
			case '|':
			case ')':
			case '}':
			case ']':
				throw new ParserSintaxException("Unespected unescaped symbol " + rightSide.charAt(i), rightSide);
			case '(':
				//TODO or
				break;
			case '{':
				//TODO kleen star
				break;
			case '[':
				//TODO 0 or 1
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
	
	public static String getParenthesisSubstring(String source, char open, char close) {
		int parenthesisCount = 0;
		int start = -1;
		int end = -1;
		//TODO
		return source.substring(start, end);
	}
}
