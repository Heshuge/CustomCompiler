import java.io.*;
import java.util.*;

public class ExprStack {

	//variable name stack
    private static Stack<String> stack = new Stack<String>();
	//if and dowhile jump logic
    private static Stack<String> labelstack = new Stack<String>();
    private static Stack<String> dowhilestack = new Stack<String>();
	//function linked-list
	private LinkedList<Function> functionList = new LinkedList<Function>();
	//function stack
	private static Stack<String> functionstack = new Stack<String>();
	//tiny node list
	private static ArrayList<TinyNode> tinylist = new ArrayList<TinyNode>();
	//counters
    private static int parametercount = 1;
    private static int localIRcount = 1;
    private static int registercount = 1;
    private static int tinyregcount = 1;
    private static int labelcount = 1;
    private static int tinylabelcount = 1;
	private static int argcounter = 0;
	//if and dowhile labels
	private static String endiflabel = "";
	private static String dowhilelabel = "";

	////////////////////////
	//
	//	globalPush
	//
	//
	////////////////////////
	public static void globalPush() {
	
		
		functionstack.push("");
		TinyNode tnode1 = new TinyNode("push", "", "");
		tinylist.add(tnode1);
		functionstack.push("");
		TinyNode tnode2 = new TinyNode("push", "r0", "");
		tinylist.add(tnode2);
		functionstack.push("");
		TinyNode tnode3 = new TinyNode("push", "r1", "");
		tinylist.add(tnode3);
		functionstack.push("");
		TinyNode tnode4 = new TinyNode("push", "r2", "");
		tinylist.add(tnode4);
		functionstack.push("");
		TinyNode tnode5 = new TinyNode("push", "r3", "");
		tinylist.add(tnode5);
	}
	
	////////////////////////
	//
	//	functionLabel
	//
	//
	////////////////////////
	public static void functionLabel(String id, String parameters_list) {
	
		String p_res = "";
		String type = "";
		String name = "";
		String value = "";

		//check types
		if (parameters_list == null) {
			//empty params
		} else {
			String[] parameters = parameters_list.split(",");
			
			for (int i=0; i<parameters.length; i++) {
				
				p_res = newParameter();
				if (parameters[i].contains("INT")) {
					type = "INT";
					name = parameters[i].replace("INT", "");
					
				} else if (parameters[i].contains("FLOAT")) {
					type = "FLOAT";
					name = parameters[i].replace("FLOAT", "");

				} else if (parameters[i].contains("STRING")) {
					type = "STRING";
					name = parameters[i].replace("STRING", "");

				} else {
					System.out.println(";Warning: Unknown type");
				}
			}
		}

		//IR
		IRNode irnode = new IRNode("LABEL", id, "", "");
		irnode.printNode();
		IRNode irnode2 = new IRNode("LINK", "", "", "");
		irnode2.printNode();
		
		//Tiny
		TinyNode tnode1 = new TinyNode("jsr ", "main", "");		
		tinylist.add(tnode1);
		TinyNode tnode2 = new TinyNode("sys ", "halt", "");		
		tinylist.add(tnode2);		
		TinyNode tnode3 = new TinyNode("label ", id, "");		
		tinylist.add(tnode3);		
		TinyNode tnode4 = new TinyNode("link", "?", "");		
		tinylist.add(tnode4);

		
		//reset regsters
		localIRcount = 1;
		registercount = 1;
		parametercount = 1;
	}

	////////////////////////
	//
	//	functionEnd
	//
	//
	////////////////////////
	public static void functionEnd() {	

		IRNode irnode = new IRNode("RET", "", "", "");
		irnode.printNode();
		System.out.println();
		
		//Tiny
		TinyNode tnode = new TinyNode("end", "", "");		
		tinylist.add(tnode);
	}

	////////////////////////
	//
	//	functionPushParameter
	//
	//
	////////////////////////
	public static void functionPushParameter(String parameter) {

		//need to know localIR associated with this variable
		//look for $L that matched parameter
		//check if its not already in the HashMap

		//IR
		//Push return node
		if (argcounter++ == 0) {
			IRNode irnode = new IRNode("PUSH", "", "", "");
			irnode.printNode();
		}

		String l_res = SymbolHashStack.getIRReg(parameter);
		//String t_res = SymbolHashStack.checkType(parameter);

		//System.out.println("                    " + parameter + ":" + l_res + ":" + t_res);
	
		SymbolHashStack.newIRNode(parameter, l_res);
		
		IRNode irnode = new IRNode("PUSH", l_res, "", "");
		irnode.printNode();

		//Tiny
		functionstack.push(parameter);
		TinyNode tnode2 = new TinyNode("push", "?", "");
		tinylist.add(tnode2);
	}

	////////////////////////
	//
	//	functionRP
	//
	//
	////////////////////////
	public static void functionRP() {
	
		functionstack.push("");
		TinyNode tnode = new TinyNode("push", "", "");
		tinylist.add(tnode);
	}
	
	////////////////////////
	//
	//	functionCall
	//
	//
	////////////////////////
	public static void functionCall(String id) {
	
		String name = "";

		//System.out.println(id);
	
		//IR
		IRNode irnode = new IRNode("JSR", id, "", "");
		irnode.printNode();

		for (int i=0; i<argcounter; i++) {
			
			name = functionstack.pop();
			//System.out.println(name);
			irnode = new IRNode("POP", "", "", "");
			irnode.printNode();
		}
		
		//create register for return
		String res = newRegister();

		//move this when tiny tree is followed		
		//String type = SymbolHashStack.getIRReg(id);	
		
		
		name = functionstack.pop();
		//System.out.println("                  " + name + ":" + res);
		IRNode irnode2 = new IRNode("POP", res, "", "");
		irnode2.printNode();	
		//SymbolHashStack.newIRNode(name, res);




		//Tiny	

	
		functionstack.push("");
		TinyNode tnode8 = new TinyNode("push", "r0", "");
		tinylist.add(tnode8);
		functionstack.push("");
		TinyNode tnode7 = new TinyNode("push", "r1", "");
		tinylist.add(tnode7);
		functionstack.push("");
		TinyNode tnode6 = new TinyNode("push", "r2", "");
		tinylist.add(tnode6);
		functionstack.push("");
		TinyNode tnode5 = new TinyNode("push", "r3", "");
		tinylist.add(tnode5);
		TinyNode tnode = new TinyNode("jsr", id, "");
		tinylist.add(tnode);
		TinyNode tnode1 = new TinyNode("pop", "r3", "");
		tinylist.add(tnode1);
		TinyNode tnode2 = new TinyNode("pop", "r2", "");
		tinylist.add(tnode2);
		TinyNode tnode3 = new TinyNode("pop", "r1", "");
		tinylist.add(tnode3);
		TinyNode tnode4 = new TinyNode("pop", "r0", "");
		tinylist.add(tnode4);
		TinyNode tnode9 = new TinyNode("pop", "", "");
		for (int i=0;i<argcounter;argcounter--) {
			tinylist.add(tnode9);
		}
		//create new register
		//String t_res = newTinyReg();
		
		//pop return value		
		TinyNode tnode10 = new TinyNode("pop", "?", "");
		tinylist.add(tnode10);

		stack.push(res);
		
	}

	////////////////////////
	//
	//	functionReturn
	//
	//
	////////////////////////
	public static void functionReturn(String id) {

		//if constant
		if (SymbolHashStack.getIRReg(id) == null) {
			
			//track things
			String optype = SymbolHashStack.checkType(id).split("")[1];

			//create new register
			String res = newRegister();
		
			//IR
			IRNode irnode1 = new IRNode("STORE"+optype, id, res, "");
			irnode1.printNode();
			IRNode irnode2 = new IRNode("STORE"+optype, res, "$R", "");
			irnode2.printNode();

		} else {
			//if variable
			//check optype
			String optype = SymbolHashStack.checkType(id).split("")[1];

			//track things
		
			//IR
			IRNode irnode = new IRNode("STORE"+optype, "$L"+(localIRcount-1), "$R", "");
			irnode.printNode();
		}

		//Tiny
		TinyNode tnode1 = new TinyNode("unlnk", "", "");
		tinylist.add(tnode1);
		TinyNode tnode2 = new TinyNode("ret", "", "");
		tinylist.add(tnode2);
	}

	////////////////////////
	//
	//	functionPop
	//
	//
	////////////////////////
	public static void functionPop(String ret) {
	
		functionstack.pop();
		TinyNode tnode1 = new TinyNode("pop", "", "");		
		tinylist.add(tnode1);
		ret = functionstack.pop();
		TinyNode tnode2 = new TinyNode("pop", "", ret);		
		tinylist.add(tnode2);
	}

	////////////////////////
	//
	//	addOperator
	//
	//
	////////////////////////
    public static void addOperator(String op) {

		String res = newRegister();
	
		String tiny_ot = "";

		String op2 = stack.pop();
		String op1 = stack.pop();
		String p_res1 = newParameter();
		String p_res2 = newParameter();

		stack.push(res);

		//check optype
		String optype = SymbolHashStack.checkType(op1).split("")[1];

		if ( optype.contains("F")) {
			tiny_ot = "r";
		} else {
			tiny_ot = optype.toLowerCase();
		}

		String instruction = "";
		String tiny_instr = "";
		
		if (op.equals("+")) {

			instruction = "ADD"; 
			tiny_instr	= "add";
		}

		else if (op.equals("-")) {

			instruction = "SUB";
			tiny_instr	= "sub"; 
		}

		else if (op.equals("*")) {

			instruction = "MULT";
			tiny_instr	= "mul"; 
		}

		else if (op.equals("/")) {

			instruction = "DIV";
			tiny_instr	= "div"; 
		}	

		
		//IR
		IRNode irnode = new IRNode(instruction+optype, p_res1, p_res2, res);
		irnode.printNode();

		//Tiny
		//String t_res = newTinyReg();
		TinyNode t1node = new TinyNode("move", "?", "?");
		tinylist.add(t1node);
		TinyNode t2node = new TinyNode(tiny_instr+tiny_ot, "?", "?");
		tinylist.add(t2node);
    }

	////////////////////////
	//
	//	evaluateExpr
	//
	//
	////////////////////////
    public static void evaluateExpr() {

		//pop values off stack to be evaluated
		String res = stack.pop();
		String op1 = stack.pop();

		String l_IR = newLocalIR();
		//bind local register and variable name
		SymbolHashStack.newIRNode(res, l_IR);

		//check optype
		String optype = SymbolHashStack.checkType(res).split("")[1];

		//IR	
		IRNode irnode = new IRNode("STORE"+optype, "$T"+(registercount-1), l_IR, "");
		irnode.printNode();
		
		//Tiny
		
		TinyNode tnode = new TinyNode("move", "?", "?");
		tinylist.add(tnode);
    }

	////////////////////////
	//
	//	evaluateWrite
	//
	//
	////////////////////////
	public static void evaluateWrite(String id_list) { 

		String i_type = "";
		String t_type = "";
		String v_type = "";
		String optype = "";
		String reg = "";

		//split args
		String[] ids = id_list.split(",");
		//loop through args
		for (int i=0;i<ids.length;i++) {

			reg = ids[i];
			optype = SymbolHashStack.checkType(ids[i]);
			//find type
			if (optype.contains("STRING")) {
				i_type = "S";
				t_type = "s";
			}
			if (optype.contains("INT")) {
				i_type = "I";
				t_type = "i";
				reg = "?";
			}
			if (optype.contains("FLOAT")) {
				i_type = "F";
				t_type = "r";
				reg = "?";
			}

			
			String l_reg = SymbolHashStack.getIRReg(ids[i]);
			if (l_reg == null) {
				IRNode irnode = new IRNode("WRITE"+i_type, ids[i], "", "");
				irnode.printNode();
				TinyNode tnode = new TinyNode("sys", "write"+t_type, reg);
				tinylist.add(tnode);
			} else {
				IRNode irnode = new IRNode("WRITE"+i_type, l_reg, "", "");
				irnode.printNode();
				TinyNode tnode = new TinyNode("sys", "write"+t_type, reg);
				tinylist.add(tnode);
			}
		}		
    }
	
	////////////////////////
	//
	//	evaluateRead
	//
	//
	////////////////////////
	public static void evaluateRead(String id_list) {

		String i_type = "";
		String t_type = "";
		String v_type = "";
		String optype = "";
		String reg = "";

		String l_IR = "";	

		//loop through args
		String[] ids = id_list.split(",");
		for (int i=0;i<ids.length;i++) {

			reg = ids[i];
			l_IR = newLocalIR();	
			optype = SymbolHashStack.checkType(ids[i]);
			SymbolHashStack.newIRNode(ids[i], l_IR);

			//find type
			if (optype.contains("STRING")) {
				
				i_type = "S";
				t_type = "s";
			}
			if (optype.contains("INT")) {

				i_type = "I";
				t_type = "i";
				reg = "?";
			}
			if (optype.contains("FLOAT")) {
		
				i_type = "F";
				t_type = "r";
				reg = "?";
			}

			//create IR
			IRNode irnode = new IRNode("READ"+i_type, l_IR, "", "");
			irnode.printNode();

			//create tiny
			TinyNode tnode = new TinyNode("sys", "read"+t_type, reg);
			tinylist.add(tnode);
		}		
    }

	////////////////////////
	//
	//	evaluateIf
	//
	//
	////////////////////////
	public static void evaluateIf(String cond) {

		String label = newLabel();
		String t_label = newTinyLabel();
		
		//String t_res = newTinyReg();
	
		String compop = "";
		String lhe = "";
		String rhe = "";
	
		if (cond.contains("TRUE")) {
			lhe = "0";
			rhe = "0";
			compop = "jne";
		}
	
		if (cond.contains("!=")) {
			
			//split into left and right hand expressions
			lhe = cond.split("!=")[0];
			rhe = cond.split("!=")[1];
			compop = "jeq";
			
		} else if (cond.contains(">=")) {

			//split into left and right hand expressions
			lhe = cond.split(">=")[0];
			rhe = cond.split(">=")[1];
			compop = "jlt";

		} else if (cond.contains("<=")) {

			//split into left and right hand expressions
			lhe = cond.split("<=")[0];
			rhe = cond.split("<=")[1];
			compop = "jgt";
		
		} else if (cond.contains(">")) {

			//split into left and right hand expressions
			lhe = cond.split(">")[0]; 
			rhe = cond.split(">")[1]; 
			compop = "jlt";

		} else if (cond.contains("<")) {
	
			//split into left and right hand expressions
			lhe = cond.split("<")[0]; 
			rhe = cond.split("<")[1]; 
			compop = "jgt";

		} else if (cond.contains("=")) {

			//split into left and right hand expressions
			lhe = cond.split("=")[0];
			rhe = cond.split("=")[1];
			compop = "jne";

		}

		//push if-statement label onto stack
		

		//add label node for endif
		TinyNode tnode = new TinyNode("label", "", t_label);
		labelstack.push(t_label);
		//tinylist.add(tnode);

		//add mov node(s)
		TinyNode t1node = new TinyNode("move", "?", "?");
		tinylist.add(t1node);

		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", "?", "?");
		tinylist.add(t2node);

		//add jne node
		TinyNode t3node = new TinyNode(compop, "", "label"+tinylabelcount);
		tinylist.add(t3node);
		
		
	}

	////////////////////////
	//
	//	evaluateElseIf
	//
	//
	////////////////////////
	public static void evaluateElseIf(String cond) {

		String label = newLabel();
		String t_label = newTinyLabel();

		String compop = "";
			
		String lhe = "";
		String rhe = "";

		
		endiflabel = labelstack.pop();
		
		labelstack.push(endiflabel);

		TinyNode tnode0 = new TinyNode("jmp", "", endiflabel);
		tinylist.add(tnode0);


		if (cond.contains("TRUE")) {
			lhe = "0";
			rhe = "0";
			compop = "jne";
		}
		
		if (cond.contains("!=")) {
			
			//split into left and right hand expressions
			lhe = cond.split("!=")[0];
			rhe = cond.split("!=")[1];
			compop = "jeq";
			
		} else if (cond.contains(">=")) {

			//split into left and right hand expressions
			lhe = cond.split(">=")[0];
			rhe = cond.split(">=")[1];
			compop = "jlt";

		} else if (cond.contains("<=")) {

			//split into left and right hand expressions
			lhe = cond.split("<=")[0];
			rhe = cond.split("<=")[1];
			compop = "jgt";
		
		} else if (cond.contains(">")) {

			//split into left and right hand expressions
			lhe = cond.split(">")[0];
			rhe = cond.split(">")[1]; 
			compop = "jlt";

		} else if (cond.contains("<")) {
	
			//split into left and right hand expressions
			lhe = cond.split("<")[0]; 
			rhe = cond.split("<")[1]; 
			compop = "jgt";

		} else if (cond.contains("=")) {

			//split into left and right hand expressions
			lhe = cond.split("=")[0];
			rhe = cond.split("=")[1];
			compop = "jne";

		}

		//add label node
		IRNode irnode = new IRNode("LABEL", "", "", label);
		irnode.printNode();
		TinyNode tnode = new TinyNode("label", "", t_label);
		tinylist.add(tnode);

		//add mov node(s)
		TinyNode t1node = new TinyNode("move", "?", "?");
		tinylist.add(t1node);
		
		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", "?", "?");
		tinylist.add(t2node);

		//add compop node
		TinyNode t3node = new TinyNode(compop, "", "label"+tinylabelcount);
		tinylist.add(t3node);
		
	}

	////////////////////////
	//
	//	labelEndIf
	//
	//
	////////////////////////
	public static void labelEndIf() {
		
		String label = newLabel();
		String t_label = newTinyLabel();
		
		endiflabel = labelstack.pop();

		//add label node
		IRNode irnode = new IRNode("LABEL", "", "", label);
		irnode.printNode();
		TinyNode tnode1 = new TinyNode("label", "", t_label);
		tinylist.add(tnode1);
		TinyNode tnode2 = new TinyNode("label", "", endiflabel);
		tinylist.add(tnode2);

	}

	////////////////////////
	//
	//	labelDoWhile
	//
	//
	////////////////////////
	public static void labelDoWhile() {

		String label = newLabel();
		String t_label = newTinyLabel();

		//add dowhile label to stack
		dowhilestack.push(t_label);

		//add label node
		IRNode irnode = new IRNode("LABEL", "", "", label);
		irnode.printNode();
		TinyNode tnode = new TinyNode("label", "", t_label);
		tinylist.add(tnode);

		
		
	}

	////////////////////////
	//
	//	evaluateDoWhile
	//
	//
	////////////////////////
	public static void evaluateDoWhile(String cond) {

		//String t_res = newTinyReg();
		String compop = "";
			
		String lhe = "";
		String rhe = "";
		
		dowhilelabel = dowhilestack.pop();
		


		//dowhile equalities work in the intuitive way

		if (cond.contains("!=")) {
			
			//split into left and right hand expressions
			lhe = cond.split("!=")[0];
			rhe = cond.split("!=")[1];
			compop = "jne";
			
		} else if (cond.contains(">=")) {

			//split into left and right hand expressions
			lhe = cond.split(">=")[0];
			rhe = cond.split(">=")[1];
			compop = "jgt";

		} else if (cond.contains("<=")) {

			//split into left and right hand expressions
			lhe = cond.split("<=")[0];
			rhe = cond.split("<=")[1];
			compop = "jlt";
		
		} else if (cond.contains(">")) {

			//split into left and right hand expressions
			lhe = cond.split(">")[0];
			rhe = cond.split(">")[1];
			compop = "jgt";

		} else if (cond.contains("<")) {
	
			//split into left and right hand expressions
			lhe = cond.split("<")[0];
			rhe = cond.split("<")[1];
			compop = "jlt";

		} else if (cond.contains("=")) {

			//split into left and right hand expressions
			lhe = cond.split("=")[0];
			rhe = cond.split("=")[1];
			compop = "jeq";

		}

		//add mov node(s)
		TinyNode t1node = new TinyNode("move", "?", "?");
		tinylist.add(t1node);
		
		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", "?", "?");
		tinylist.add(t2node);

		//add jump node
		TinyNode t3node = new TinyNode(compop, "", dowhilelabel);
		tinylist.add(t3node);
		
		
	}

	////////////////////////
	//
	//	addLiteral
	//
	//
	////////////////////////
    public static void addLiteral(String lit) {
		
		String op1 = lit;
		String res = newRegister();
		//String t_res = newTinyReg();

		//check optype
		String optype = SymbolHashStack.checkType(op1).split("")[1];

		//IR
		IRNode irnode = new IRNode("STORE"+optype, op1, res, ""); 
		irnode.printNode();

		//Tiny
		TinyNode tnode = new TinyNode("move", "?", "?"); 
		tinylist.add(tnode);	
		stack.push(res);
    }
	
	////////////////////////
	//
	//	printTinyList
	//
	//
	////////////////////////
	public static void printTinyList(){

		for (int i = 0; i < tinylist.size(); i++) {

			//check the indexes with the list, and print
			TinyNode node = tinylist.get(i);
			node.printNode();
		}
		System.out.println("sys halt");
    }

	////////////////////////
	//
	//	addRIdentifier
	//
	//
	////////////////////////
    public static void addRIdentifier(String id) {
		
		stack.push(id);
    }

	////////////////////////
	//
	//	addLIdentifier
	//
	//
	////////////////////////
    public static void addLIdentifier(String id) {
		
		stack.push(id);
    }

	////////////////////////
	//
	//	newRegister
	//
	//
	////////////////////////
    public static String newRegister() {

		return "$T"+Integer.toString(registercount++);
    }

	////////////////////////
	//
	//	newTinyReg
	//
	//
	////////////////////////
    public static String newTinyReg() {

		return "r"+Integer.toString((tinyregcount++)-1);
    }

	////////////////////////
	//
	//	newLocalIR
	//
	//
	////////////////////////
    public static String newLocalIR() {

		return "$L"+Integer.toString((localIRcount++));
    }

	////////////////////////
	//
	//	newParameter
	//
	//
	////////////////////////
    public static String newParameter() {

		return "$P"+Integer.toString((parametercount++));
    }



	////////////////////////
	//
	//	newLabel
	//
	//
	////////////////////////
	 public static String newLabel() {

		return "label"+Integer.toString(labelcount++);
    }

	////////////////////////
	//
	//	newTinyLabel
	//
	//
	////////////////////////
    public static String newTinyLabel() {

		return "label"+Integer.toString(tinylabelcount++);
    }

	////////////////////////
	//
	//	convertSymbolsIRtoTiny
	//
	//
	////////////////////////
    public static String convertSymbolsIRtoTiny(String k, int link) {
	
		//System.out.println(k + ":" + link);


		return "$" + (6 + link);
    }
	
	
}
