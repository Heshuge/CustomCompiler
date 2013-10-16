//import java utilities
import java.util.ArrayList;
import java.util.HashMap;

//declare Hash class
public class SymbolHash {

	//instantiate stack object and scope tracker 
	private ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
	private HashMap<String, String> symbolMap = new HashMap<String, String>();
	private ArrayList<IRNode> irNodeList = new ArrayList<IRNode>();
	private HashMap<String, String> irNodeMap = new HashMap<String, String>();
	private String scope; 

	//***********************
	//	methods
	//
	//***********************

	//set hash scope
	public SymbolHash(String scope) {

		this.scope = scope;
	}

	//check hash scope
	public String checkScope() {

		return this.scope;
	}

	//create new symbol
	public void newSymbol(String type, String name, String value) {

		//split name args
		String[] names = name.split(",");
		//loop through hash symbols
		for (int i = 0; i < names.length; i++) {
			//check if its not already in the HashMap
			if (!symbolMap.containsKey(names[i].trim())) {
				//if not, add it
				symbolList.add(new Symbol(names[i].trim(), type, value));
				symbolMap.put(names[i].trim(), type);
			}
			else {
				//else throw error
				System.out.println("DECLARATION ERROR " + name);
			}
		}
	}

	//create new IRNode
	public void newIRNode(String opcode, String first_operand, String second_operand, String result) {

		//check if its not already in the HashMap
		if (!symbolMap.containsKey(result)) {
			//if not, add it
			irNodeList.add(new IRNode(opcode, first_operand, second_operand, result));
			irNodeMap.put(result, opcode);
		}
		else {
			//else throw error
			System.out.println("DECLARATION ERROR " + result);
		}
	}

	//create new IRNode TAIL
	public void newIRNodeTail(String opcode, String first_operand) {

		irNodeList.add(new IRNode(opcode, first_operand, "UNDEFINED", "UNDEFINED"));
		irNodeMap.put(first_operand, opcode);
		
	}

	//add factor to second_operand
	public void addFactor(String opcode, String second_operand) {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.storeFactor(opcode, second_operand);
		irNodeList.add(tempIRNode);
		
	}

	//add id to result
	public void addIRId(String result) {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.storeResult(result);
		irNodeList.add(tempIRNode);
		
	}

	//print the current Hash symbols
	public void printHashSymbols() {

		System.out.println("Symbol table " + scope);
		//loop through indexes
		for(int i = 0; i < symbolList.size(); i++) {
			//check the indexes with the list, and print
			Symbol tempSymbol = symbolList.get(i);
			tempSymbol.printOutput();
		}
		//loop through indexes
		for(int i = 0; i < irNodeList.size(); i++) {
			//check the indexes with the list, and print
			IRNode tempIRNode = irNodeList.get(i);
			tempIRNode.printIRNode();
		}
		System.out.println();
	}

	//print IRNode
	public void printIRNode() {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.printIRNode();
	}

	//print IRUpdate
	public void printIRUpdate() {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.printIRUpdate();
	}
}
