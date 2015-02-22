package cristina.compint.slr1parser.parser;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Stack;

import cristina.compint.slr1parser.grammar.Element;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.Terminal;

/**
 * The Class Parser.
 * This class allows string parsing to check if a string is of a SLR(1) language
 */
public class Parser {


	/**
	 * Parsing string s according ActionGoto table.
	 *
	 * @param s the string to parse
	 * @param table the ActionGoto Table
	 * @return true if the string s is a valid string
	 */
	public static boolean checkString(String s, ActionGotoTable table) {
		boolean isChecked = false;
		Stack<StackElement> stack = new Stack<StackElement>();

		stack.push(new StackElement(table.getStates().get(0), Grammar.END_LINE));
		
		//Transforms input string into Terminal list
		List<Terminal> terminalString = new ArrayList<Terminal>();
		for(char c: s.toCharArray()) {
			terminalString.add(new Terminal(String.valueOf(c)));
		}
		terminalString.add(Grammar.END_LINE);
		
		Formatter formatter = new Formatter();
		formatter.format("%1$50s | %2$10s | %3$1s ", "STACK", "INPUT", "ACTION");
		System.out.println(formatter.toString());
		formatter.close();
		int index = 0;
		while(index < terminalString.size()) {
			Terminal t = terminalString.get(index);
			Action a = table.getAction(stack.peek().getState(), t);
			printLine(stack, terminalString, index, a);
			if(a == null)
				return false;
			if(a instanceof ActionShift) {
				ActionShift as = (ActionShift) a;
				stack.push(new StackElement(as.getDestState(), t));
				index++;
			} else {
				ActionReduce ar = (ActionReduce) a;
				
				for(int i = ar.getProduction().getRight().size()-1; i >= 0; i--) {
					Element e = ar.getProduction().getRight().get(i);
					if(Grammar.EPS.equals(e))
						break;
					if(!stack.pop().getElement().equals(e)) {
						return false;
					}
				}
				
				Goto gt = table.getGoto(stack.peek().getState(), ar.getProduction().getLeft());
				stack.push(new StackElement(gt.getDestState(), ar.getProduction().getLeft()));
				
			}
			
		}
		
		if(stack.peek().getState().equals(table.getAcceptState())){
			isChecked = true;
		} else {
			isChecked = false;
		}

		return isChecked;
	}

	/**
	 * Prints the line. Utility method to print the process
	 *
	 * @param stack the stack
	 * @param terminalString the terminal string
	 * @param index the index
	 * @param a the a
	 */
	private static void printLine(Stack<StackElement> stack,
			List<Terminal> terminalString, int index, Action a) {
		boolean isFirst = true;
		StringBuilder stackSb = new StringBuilder();
		for(StackElement se: stack) {
			if(!isFirst)
				stackSb.append(", ");
			else
				isFirst = false;
			stackSb.append(se.getElement());
			stackSb.append(" ");
			stackSb.append(se.getState().getState());
		}
		
		StringBuilder inputSb = new StringBuilder();
		for(int i = index; i < terminalString.size(); i++) {
			inputSb.append(terminalString.get(i));
		}
		Formatter formatter = new Formatter();
		formatter.format("%1$50s | %2$10s | %3$1s ", stackSb.toString(), inputSb.toString(), (a!=null)?a.toString():"");
		System.out.println(formatter.toString());
		formatter.close();
		
	}

}
