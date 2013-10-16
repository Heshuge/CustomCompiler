//Hello, I'm a java class

//Import
import org.antlr.v4.runtime.*;
import java.io.*;
import java.util.*;



@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})

public class Micro{

	public static void main(String[] args) throws IOException{


		if(args.length != 1) {
			throw new IOException("Needs one argument");
		}

		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		MicroGrammarLexer L1 = new MicroGrammarLexer(input);
		CommonTokenStream T1 = new CommonTokenStream(L1);
		MicroGrammarParser P1 = new MicroGrammarParser(T1);

		try {
			P1.program();

			
		}

		catch (Exception e) {
			System.out.println("Not Accepted");
		}
	}
}
