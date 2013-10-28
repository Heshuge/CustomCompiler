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

	//print the Tiny Code
	public void printTinyCode() {

		System.out.println(";tiny code");
		//loop through symbol indexes
		for(int i = 0; i < symbolList.size(); i++) {
			//check the indexes with the list, and print
			Symbol tempSymbol = symbolList.get(i);
			tempSymbol.printTinyCode();
		}
	}

	//print IRCode header label for step_4
	public void printIRCode() {
		
		System.out.println(";IR code");
	}
	//print IRNode data
	public void printIRNode() {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.printNode();
	}

}
