//import java stack utility
import java.util.Stack;

//declare stack class
public class SymbolHashStack {

	//instantiate stack object and scope tracker 
	private static Stack<SymbolHash> symbolHashStack = new Stack<SymbolHash>();
	private static int scope = 1;
	
	//***********************
	//	methods 
	//
	//***********************

	//push to global hash
	public static void newGlobal() {

		symbolHashStack.push(new SymbolHash("GLOBAL"));
	}
	
	//push to function hash
	public static void newFunction(String name) {

		symbolHashStack.push(new SymbolHash(name));
	}
	
	//push to block hash
	public static void newBlock() {

		symbolHashStack.push(new SymbolHash("BLOCK " + Integer.toString(scope++)));
	}
	
	//add new symbol to current stack hash
	public static void newSymbol(String type, String name, String value) {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.newSymbol(type, name, value);
		symbolHashStack.push(tempHash);
	}
	
	//add new IRNodeTail to current stack hash
	public static void newIRNodeTail(String opcode, String first_operand) {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.newIRNodeTail(opcode, first_operand);
		symbolHashStack.push(tempHash);
	}
	
	//add new factor to current IRNode first_op
	public static void addFactor(String opcode, String second_operand) {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.addFactor(opcode, second_operand);
		symbolHashStack.push(tempHash);
	}

	//add new id to current IRNode result
	public static void addIRId(String result) {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.addIRId(result);
		symbolHashStack.push(tempHash);
	}
	
	//print hash table
	public static void printHash() {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.printHashSymbols();
		symbolHashStack.push(tempHash);
	}
	
	//print IRNode
	public static void printIRNode() {

		SymbolHash tempHash = symbolHashStack.pop();
		tempHash.printIRNode();
		symbolHashStack.push(tempHash);
	}
	
	
	//pop hash and return to last
	public static void popHash() {

		SymbolHash tempHash = symbolHashStack.pop();
	}
}
