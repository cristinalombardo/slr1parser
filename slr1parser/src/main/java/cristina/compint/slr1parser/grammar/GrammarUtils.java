package cristina.compint.slr1parser.grammar;

import java.util.List;

public class GrammarUtils {

	public static Grammar convertToGrammar(List<String> grammarLines) {
		Grammar grammar = new Grammar();
		System.out.println("\n");
		for(String line: grammarLines) {
			System.out.println(line);
		}
		return grammar;
	}
}
