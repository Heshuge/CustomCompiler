//import java utilities
import java.util.ArrayList;
import java.util.HashMap;

//declare Hash class
public class SymbolHash {

	//instantiate stack object and scope tracker 
	private ArrayList<Symbol> symbolList = new ArrayList<Symbol>();
	private HashMap<String, String> symbolMap = new HashMap<String, String>();
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

	//print the current Hash symbols
	public void printHashSymbols() {

		System.out.println("Symbol table " + scope);
		//loop through indexes
		for(int i = 0; i < symbolList.size(); i++) {
			//check the indexes with the list, and print
			Symbol tempSymbol = symbolList.get(i);
			tempSymbol.printOutput();
		}
		System.out.println();
	}

}
