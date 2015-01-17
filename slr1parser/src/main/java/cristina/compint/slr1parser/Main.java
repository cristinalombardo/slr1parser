package cristina.compint.slr1parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import cristina.compint.slr1parser.grammar.GrammarUtils;

public class Main {

	public static void main(String[] args) throws MalformedURLException, URISyntaxException {
		
		System.out.println("Usage: java -jar slr1parser.jar [grammar-file]\n"
				+ "\t[grammar-file]: if not set we use an example grammar\n");
		
		
		Path grammarPath = null;
		
		if(args.length > 0) {
			grammarPath = Paths.get(args[0]);
		} else {
			URL url = Main.class.getResource("/ebnfexample.txt");
			
			grammarPath = Paths.get(url.toURI());//"ebnfexample.txt");
			try {
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
			
			GrammarUtils.convertToGrammar(grammarLines);
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
