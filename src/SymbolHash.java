//import java utilities
import java.util.ArrayList;
import java.util.HashMap;

//declare Hash class
public class SymbolHash {

	//instantiate stack object and scope tracker 
	//build off pre-defined Symbol class
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
				//System.out.println("New Symbol: " + type + " " + names[i].trim() + " " + value);
			}
			else {
				//else throw error
				System.out.println("DECLARATION ERROR " + name);
			}
		}
	}
	
	//check IR register Type
	public String getIRType(String name) {
		return irNodeMap.get(name);
	}

	//new IR
	public void newIRNode(String name, String irtype) {

		if (!irNodeMap.containsKey(name)) {
			//irNodeList.add(new IRNode(name, irtype, ""));
			irNodeMap.put(name, irtype);
			//System.out.println("New IR: " + irNodeMap.get(name));
		} else {
			irNodeMap.put(name, irNodeMap.get(name));
			System.out.println("Old IR: " + irNodeMap.get(name));
		}
	}	
	
	public String checkType(String name) {

		if (symbolMap.get(name) != null) {
			return symbolMap.get(name);
		}
		//else throw error
		//System.out.println(";ERROR: " + name + " TYPE NOT FOUND IN SCOPE: " + checkScope());
		return "E";
	}

	//print the Tiny Code
	public void printTinyCode() {
	
		System.out.println();
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
		System.out.println();
	}
	//print IRNode data
	public void printIRNode() {

		IRNode tempIRNode = irNodeList.get(irNodeList.size()-1);
		tempIRNode.printNode();
	}

}
