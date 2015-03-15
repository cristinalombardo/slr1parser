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
			 *
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

			//Step 3: Extract grammar with First and Follow set from EBNF
			Grammar g =	GrammarUtils.convertToGrammar(grammarLines);

			printGrammar(g);
			System.out.println("\nGrammar done. Press enter to contiune...");
			scan.nextLine();
			
			printFirsAndFollow(g);
			System.out.println("\nFollow and First set done. Press enter to contiune...");
			scan.nextLine();
			
			//Step 4: Create Characteristic Finite State Machine 
			Cfsm cfsm = CfsmUtils.createCfsm(g);
			System.out.println("Cfsm");

			System.out.println(cfsm);

			System.out.println("\nCarateristich finite state machine done. Press enter to contiune...");
			scan.nextLine();

			//Step 5: Create ActionGoto Table
			ActionGotoTable actionGotoTable = ActionGotoUtils.createTable(cfsm);
			
			printActionGoto(g, actionGotoTable);
			
			System.out.println("\nActionGoto table done.");
			
			//Step 6: String parsing
			while(true) {
				System.out.print("Write \"exit\" to exit.\nInsert a string to parse and press enter: ");
				String toCheck = scan.nextLine();
				if(toCheck.equals("exit"))
					break;
				System.out.println("\nParsing string: " + toCheck);
				if(Parser.checkString(toCheck, actionGotoTable)) {
					System.out.println("\nString \"" + toCheck +"\" Checked.");
				} else {
					System.out.println("\nString \"" + toCheck +"\" does not Checked.");
				}
				
				
			}

		
		} catch (GrammarSyntaxException e) {
			System.out.println("Parser error: check the input file.");
			System.out.println("Error line: " + e.getLine());
			System.out.println("Error message: " + e.getMessage());
		} catch (CfsmException e) {
			System.out.println("Error during CFSM creation see error.txt for detail");
			try {
				Files.write(Paths.get("./error.txt"), e.getMessage().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			scan.close();	
		}
	}

	private static void printGrammar(Grammar g) {
		
		//Print Terminal and NonTerminal Set
		System.out.println("\nNon terminals list: " + g.getNonTerminals());
		System.out.println("Terminals list: " + g.getTerminals());


		System.out.println("\nGrammar Productions: \n" + g.getAxiomProduction());
		//Print grammar's productions
		for(Production p: g.getProductions()) {
			System.out.println(p);
		}
		
	}
	
	private static void printFirsAndFollow(Grammar g) {
		System.out.println("\nFirst set of non terminals:");
		for(NonTerminal nt: g.getNonTerminals()) {
			System.out.println(nt.toString() + "\t:\t" + nt.getFirst().toString()); 
		}

		System.out.println("\nFollow set of non terminals:");
		for(NonTerminal nt: g.getNonTerminals()) {
			if(nt.getFollow() != null)
				System.out.println(nt.toString() + "\t:\t" + nt.getFollow().toString()); 
		}
	}

	private static void printActionGoto(Grammar g,
			ActionGotoTable actionGotoTable) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbFile = new StringBuilder();
		sbFile.append("state");
		for(Terminal t: g.getTerminals()) {
			if(Grammar.EPS.equals(t))
				continue;
			sb.append("\t|" + t + "\t");
			sbFile.append("," + t);
		}
		sb.append("\t|$\t");
		sbFile.append(",$");
		for(NonTerminal nt: g.getNonTerminals()) {
			sb.append("\t|" + nt);
			sbFile.append("," + nt);
		}

		for(State s: actionGotoTable.getStates()) {
			sb.append("\n\n");
			sbFile.append("\n");
			sb.append("S" + s.getState());
			sbFile.append("S" + s.getState());
			if(s.equals(actionGotoTable.getAcceptState())) {
				sb.append("\t| Accept");
				sbFile.append(",Accept");
				continue;
			}
			for(Terminal t: g.getTerminals()) {
				if(Grammar.EPS.equals(t))
					continue;
				sb.append("\t|");
				sbFile.append(",");
				Action a = actionGotoTable.getAction(s, t);
				String aString = (a!=null)?a.toString():"";

				sb.append(aString + ((aString.length() < 7)?"\t":""));
				sbFile.append(aString);
			}

			sb.append("\t|");
			sbFile.append(",");
			Action a = actionGotoTable.getAction(s, Grammar.END_LINE);
			String aString = (a!=null)?a.toString():"";

			sb.append(aString + ((aString.length() < 7)?"\t":""));
			sbFile.append(aString);
			for(NonTerminal nt: g.getNonTerminals()) {
				sb.append("\t|");
				sbFile.append(",");
				Goto gt = actionGotoTable.getGoto(s, nt);
				if(gt!=null) {
					sb.append(gt);
					sbFile.append(gt);
				}
			}
		}
		System.out.println(sb);
		try {
			Files.write(Paths.get("./OUTPUT.TXT"), sbFile.toString().getBytes());
			System.out.println("\nOUTPUT.TXT created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
