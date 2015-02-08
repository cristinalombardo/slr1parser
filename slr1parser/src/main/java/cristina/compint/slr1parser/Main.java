package cristina.compint.slr1parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import cristina.compint.slr1parser.cfsm.Cfsm;
import cristina.compint.slr1parser.cfsm.CfsmUtils;
import cristina.compint.slr1parser.cfsm.State;
import cristina.compint.slr1parser.exception.CfsmException;
import cristina.compint.slr1parser.exception.GrammarSyntaxException;
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
			/*
			 * Comments if you run the project into eclipse
			 */
			Map<String, String> env = new HashMap<>(); 
			env.put("create", "true");
			try {
				FileSystems.newFileSystem(url.toURI(), env);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			/**/
			grammarPath = Paths.get(url.toURI());
		}

		Scanner scan = new Scanner(System.in);
		try {
			System.out.println("Input file");
			System.out.println("============================");
			Files.lines(grammarPath).forEach(System.out::println);
			System.out.println("============================");

			System.out.println("\nPress enter to contiune...");
			scan.nextLine();

			//Step 2: filter comments and blank lines from input file
			List<String> grammarLines = 
					Files.lines(grammarPath)
					.filter(x -> !x.startsWith("#") && !x.isEmpty())
					.collect(Collectors.toList());

			//Step 3: Extract grammar from EBNF
			Grammar g =	GrammarUtils.convertToGrammar(grammarLines);


			System.out.println("\nNon terminals list: " + g.getNonTerminals());
			System.out.println("Terminals list: " + g.getTerminals());


			System.out.println("\nGrammar Productions: \n" + g.getAxiomProduction());

			for(Production p: g.getProductions()) {
				System.out.println(p);
			}

			System.out.println("\nGrammar done. Press enter to contiune...");
			scan.nextLine();

			System.out.println("\nFirst set of non teminals:");
			for(NonTerminal nt: g.getNonTerminals()) {
				System.out.println(nt.toString() + "\t:\t" + nt.getFirst().toString()); 
			}

			System.out.println("\nFollow set of non teminals:");
			for(NonTerminal nt: g.getNonTerminals()) {
				if(nt.getFollow() != null)
					System.out.println(nt.toString() + "\t:\t" + nt.getFollow().toString()); 
			}

			System.out.println("\nFollow and First set done. Press enter to contiune...");
			scan.nextLine();

			Cfsm cfsm = CfsmUtils.createCfsm(g);
			System.out.println("Cfsm");

			System.out.println(cfsm);

			System.out.println("\nCarateristich finite state machine done. Press enter to contiune...");
			scan.nextLine();

			ActionGotoTable actionGotoTable = ActionGotoUtils.createTable(cfsm);
			
			
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
					System.out.print("\t| Accept");
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

			System.out.println("\nActionGoto table done.");
			
			while(true) {
				System.out.print("Write \"exit\" to exit.\nInsert a string to parse and press enter: ");
				String toCheck = scan.nextLine();
				if(toCheck.equals("exit"))
					break;
				System.out.println("\n Parsing string: " + toCheck);
				if(Parser.checkString(toCheck, actionGotoTable))
					System.out.println("\nString \"" + toCheck +"\" Checked.");
				else
					System.out.println("\nString \"" + toCheck +"\" does not Checked.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (GrammarSyntaxException e) {
			System.out.println("Parser error: check the input file.");
			System.out.println("Error line: " + e.getLine());
			System.out.println("Error message: " + e.getMessage());
		} catch (CfsmException e) {
			System.out.println("Error during CFSM creation");
			System.out.println(e.getMessage());
		}finally {
			scan.close();	
		}



	}

}
