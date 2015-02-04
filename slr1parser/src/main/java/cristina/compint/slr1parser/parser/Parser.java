package cristina.compint.slr1parser.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cristina.compint.slr1parser.grammar.Element;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.Terminal;

public class Parser {


	public static boolean checkString(String s, ActionGotoTable table) {
		boolean isChecked = false;
		Stack<StackElement> stack = new Stack<StackElement>();

		stack.push(new StackElement(table.getStates().get(0), Grammar.END_LINE));
		int index = 0;
		List<Terminal> terminalString = new ArrayList<Terminal>();
		for(char c: s.toCharArray()) {
			terminalString.add(new Terminal(String.valueOf(c)));
		}
		terminalString.add(Grammar.END_LINE);
		while(index != terminalString.size()) {
			Terminal t = terminalString.get(index);
			Action a = table.getAction(stack.peek().getState(), t);
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
}
