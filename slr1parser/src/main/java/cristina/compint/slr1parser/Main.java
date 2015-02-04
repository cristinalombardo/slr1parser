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

import cristina.compint.slr1parser.cfsm.Cfsm;
import cristina.compint.slr1parser.cfsm.CfsmUtils;
import cristina.compint.slr1parser.cfsm.State;
import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.exception.ParserSintaxException;
import cristina.compint.slr1parser.grammar.Grammar;
import cristina.compint.slr1parser.grammar.GrammarUtils;
import cristina.compint.slr1parser.grammar.NonTerminal;
import cristina.compint.slr1parser.grammar.Production;
import cristina.compint.slr1parser.grammar.Terminal;
import cristina.compint.slr1parser.parser.Action;
import cristina.compint.slr1parser.parser.ActionGotoTable;
import cristina.compint.slr1parser.parser.ActionGotoUtils;
import cristina.compint.slr1parser.parser.Goto;
import cristina.compint.slr1parser.parser.Parser;

public class Main {

	public static void main(String[] args) throws MalformedURLException, URISyntaxException {

		System.out.println("Usage: java -jar slr1parser.jar [grammar-file]\n"
				+ "\t[grammar-file]: if not set we use an example grammar\n");


		Path grammarPath = null;
		//Step 1: reading input file
		if(args.length > 0) {
			grammarPath = Paths.get(args[0]);
		} else {
			URL url = Main.class.getResource("/ebnfexample.txt");
			try {
				/*
				 * Comments if you run the project into eclipse
				Map<String, String> env = new HashMap<>(); 
				env.put("create", "true");
				FileSystems.newFileSystem(url.toURI(), env); 
				*/
				grammarPath = Paths.get(url.toURI());
				System.out.println("Example file");
				System.out.println("============================");
				Files.lines(grammarPath).forEach(System.out::println);
				System.out.println("============================");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		try {
			//Step 2: filter comments and blank lines from input file
			List<String> grammarLines = 
					Files.lines(grammarPath)
					.filter(x -> !x.startsWith("#") && !x.isEmpty())
					.collect(Collectors.toList());

			//Step 3: Extract grammar from EBNF format
			Grammar g =	GrammarUtils.convertToGrammar(grammarLines);
			
			
			System.out.println("\nNon terminals list: " + g.getNonTerminals());
			System.out.println("Terminals list: " + g.getTerminals());
			
			
			System.out.println("\nGrammar Productions: \n" + g.getAxiomProduction());
			
			for(Production p: g.getProductions()) {
				
				System.out.println(p);
			}
			
			System.out.println("\nFirst set of non teminals:");
			for(NonTerminal nt: g.getNonTerminals()) {
				System.out.println(nt.toString() + "\t:\t" + nt.getFirst().toString()); 
			}
			
			System.out.println("\nFollow set of non teminals:");
			for(NonTerminal nt: g.getNonTerminals()) {
				if(nt.getFollow() != null)
					System.out.println(nt.toString() + "\t:\t" + nt.getFollow().toString()); 
			}
			
			Cfsm cfsm = CfsmUtils.createCfsm(g);
			System.out.println("Cfsm");
			
			System.out.println(cfsm);
			
			ActionGotoTable actionGotoTable = ActionGotoUtils.createTable(cfsm, g);
			
			
			for(Terminal t: g.getTerminals()) {
				if(Grammar.EPS.equals(t))
					continue;
				System.out.print("\t|" + t + "\t");
			}
			System.out.print("\t|$\t");
			for(NonTerminal nt: g.getNonTerminals()) {
				System.out.print("\t|" + nt);
			}
			
			for(State s: actionGotoTable.getStates()) {
				
					
				System.out.println("\n");
				System.out.print("S" + s.getState());
				
				if(s.equals(actionGotoTable.getAcceptState())) {
					System.out.println("\t| Accept");
					continue;
				}
				for(Terminal t: g.getTerminals()) {
					if(Grammar.EPS.equals(t))
						continue;
					System.out.print("\t|");
					Action a = actionGotoTable.getAction(s, t);
					String aString = (a!=null)?a.toString():"";
					
					System.out.print(aString + ((aString.length() < 7)?"\t":""));
				}
				
				System.out.print("\t|");
				Action a = actionGotoTable.getAction(s, Grammar.END_LINE);
				String aString = (a!=null)?a.toString():"";
				
				System.out.print(aString + ((aString.length() < 7)?"\t":""));
				for(NonTerminal nt: g.getNonTerminals()) {
					System.out.print("\t|");
					Goto gt = actionGotoTable.getGoto(s, nt);
					if(gt!=null) {
						System.out.print(gt);
					}
				}
				
			}
			
			String toCheck = "a+b+c";
			System.out.println("\n Parsing string: " + toCheck);
			if(Parser.checkString(toCheck, actionGotoTable))
				System.out.println("\nString \"" + toCheck +"\" Checked.");
			else
				System.out.println("\nString \"" + toCheck +"\" does not Checked.");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserSintaxException e) {
			System.out.println("Parser error: check the input file.");
			System.out.println("Error line: " + e.getLine());
			System.out.println("Error message: " + e.getMessage());
			e.printStackTrace();
		} catch (CfsmException e) {
			System.out.println("Error during CFSM creation");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		

	}

}
