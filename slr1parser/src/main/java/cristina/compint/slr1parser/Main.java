package cristina.compint.slr1parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cristina.compint.slr1parser.exception.ParserSintaxException;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.GrammarUtils;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Production;

public class Main {

	public static void main(String[] args) throws MalformedURLException, URISyntaxException {

		System.out.println("Usage: java -jar slr1parser.jar [grammar-file]\n"
				+ "\t[grammar-file]: if not set we use an example grammar\n");


		Path grammarPath = null;

		if(args.length > 0) {
			grammarPath = Paths.get(args[0]);
		} else {
			URL url = Main.class.getResource("/ebnfexample.txt");
			
			try {
				Map<String, String> env = new HashMap<>(); 
				env.put("create", "true");
				
				//Comments if you run the project into eclipse
//				FileSystems.newFileSystem(url.toURI(), env);
				grammarPath = Paths.get(url.toURI());//"ebnfexample.txt");
				System.out.println("Example file\n");
				Files.lines(grammarPath).forEach(System.out::println);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			List<String> grammarLines = 
					Files.lines(grammarPath)
					.filter(x -> !x.startsWith("#") && !x.isEmpty())
					.collect(Collectors.toList());

			Grammar g =	GrammarUtils.convertToGrammar(grammarLines);

			System.out.print("\nNon terminals list: ");
			for(NonTerminal pippo: g.getNonTerminals()) {
				System.out.print(pippo.getLabel() + " ");
			}

			System.out.println();

			System.out.print("\nProduction left side: ");
			for(Production p: g.getProductions()) {
				
				System.out.println(p);
			}
			
			System.out.println();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserSintaxException e) {
			System.out.println("Parser error: check the input file.");
			System.out.println("Error line: " + e.getLine());
			System.out.println("Error message: " + e.getMessage());
			e.printStackTrace();
		} 

	}

}
