import java.io.*;
import java.util.*;

public class ExprStack {

	//call stack
    private static Stack<String> stack = new Stack<String>();
	//if and dowhile jump logic
    private static Stack<String> labelstack = new Stack<String>();
    private static Stack<String> dowhilestack = new Stack<String>();
	//function stack
	private static Stack<String> functionstack = new Stack<String>();
	//tiny node list
	private static ArrayList<TinyNode> tinylist = new ArrayList<TinyNode>();
	//counters
    private static int registercount = 1;
    private static int tinyregcount = 1;
    private static int labelcount = 1;
    private static int tinylabelcount = 1;
	private static int breaklabel = 1;
	private static int dolabel = 1;
	//if and dowhile labels
	private static String endiflabel = "";
	private static String dowhilelabel = "";

	////////////////////////
	//
	//	functionPush
	//
	//
	////////////////////////
	public static void functionPush(String id, String parameters_list) {
	
		functionstack.push("return_value");
		functionstack.push(parameters_list);
		TinyNode tnode1 = new TinyNode("jmp ", "", "main");		
		tinylist.add(tnode1);		
		//TinyNode tnode = new TinyNode("jsr", "", id); this goes when the function is called
		TinyNode tnode2 = new TinyNode("label ", "", id);		
		tinylist.add(tnode2);
	}

	////////////////////////
	//
	//	functionPop
	//
	//
	////////////////////////
	public static void functionPop() {
	
		functionstack.pop();
		String return_value = functionstack.pop();
		TinyNode tnode = new TinyNode("ret", "", "");		
		tinylist.add(tnode);
	}

	////////////////////////
	//
	//	addOperator
	//
	//
	////////////////////////
    public static void addOperator(String op) {

		String res = newRegister();
		String t_res = newTinyReg();
		String op2 = stack.pop();
		String op1 = stack.pop();
		String t_op = "";
		stack.push(res);
		String optype = "I";
		String tiny_ot = "i";
		String instruction = "";
		String tiny_instr = "";
		
		//replace $T with r
		op1 = op1.replace("$T","r");
		op2 = op2.replace("$T","r");

		//lower number by 1
		if (op1.contains("r")) {
		
			op1 = "r" + Integer.toString(op1.compareTo("r0")-1);
		}
		if (op2.contains("r")) {
		
			op2 = "r" + Integer.toString(op2.compareTo("r0")-1);
		}
		
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

		IRNode irnode = new IRNode(instruction+optype, op1, op2, res);
		irnode.printNode();
		TinyNode t1node = new TinyNode("move", op1, t_res);
		tinylist.add(t1node);		
		//t1node.printNode();
		TinyNode t2node = new TinyNode(tiny_instr+tiny_ot, op2, t_res);
		tinylist.add(t2node);		
		//t2node.printNode();
    }

    public static void evaluateExpr() {

		String res = stack.pop();
		String op1 = stack.pop();
		//replace $T with r
		op1 = op1.replace("$T","r");
		
		//lower number by 1
		if (op1.contains("r")) {
		
			op1 = "r" + Integer.toString(op1.compareTo("r0")-1);
		}
		String optype = "I";
		IRNode irnode = new IRNode("STORE"+optype, op1, "", res);
		irnode.printNode();
		TinyNode tnode = new TinyNode("move", op1, res);
		tinylist.add(tnode);		
		//tnode.printNode();
    }

	public static void evaluateWrite(String id_list) {

		String optype = "I";
		//split id_list args
		//String[] id_array = id_list.split(",");
		//loop through id_array 
		//for (int i = 0; i < id_list.length; i++) {
		IRNode irnode = new IRNode("WRITE"+optype, "", "", id_list);
		irnode.printNode();
		TinyNode tnode = new TinyNode("sys", "writei", id_list);
		tinylist.add(tnode);		
		//tnode.printNode();
		//}
    }
	
	public static void evaluateRead(String id_list) {

		String optype = "I";
		//split id_list args
		//String[] id_array = id_list.split(",");
		//loop through id_array 
		//for (int i = 0; i < id_list.length; i++) {
		IRNode irnode = new IRNode("READ"+optype, "", "", id_list);
		irnode.printNode();
		TinyNode tnode = new TinyNode("sys", "readi", id_list);
		tinylist.add(tnode);		
		//tnode.printNode();
		//}
    }

	public static void evaluateIf(String cond) {

		String label = newLabel();
		String t_label = newTinyLabel();
		
		String t_res = newTinyReg();
	
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
		TinyNode t1node = new TinyNode("move", rhe, t_res);
		tinylist.add(t1node);

		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", lhe, t_res);
		tinylist.add(t2node);

		//add jne node
		TinyNode t3node = new TinyNode(compop, "", "label"+tinylabelcount);
		tinylist.add(t3node);
		
		
	}

	public static void evaluateElseIf(String cond) {

		String label = newLabel();
		String t_label = newTinyLabel();

		String t_res = newTinyReg();

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
		TinyNode t1node = new TinyNode("move", rhe, t_res);
		tinylist.add(t1node);
		
		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", lhe, t_res);
		tinylist.add(t2node);

		//add compop node
		TinyNode t3node = new TinyNode(compop, "", "label"+tinylabelcount);
		tinylist.add(t3node);
		
	}

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

	public static void evaluateDoWhile(String cond) {

		String t_res = newTinyReg();
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
		TinyNode t1node = new TinyNode("move", rhe, t_res);
		tinylist.add(t1node);
		
		//add cmpi node
		TinyNode t2node = new TinyNode("cmpi", lhe, t_res);
		tinylist.add(t2node);

		//add jump node
		TinyNode t3node = new TinyNode(compop, "", dowhilelabel);
		tinylist.add(t3node);

				
		//add jmp node
		//TinyNode t4node = new TinyNode("jmp", "", "label"+dolabel);
		//tinylist.add(t4node);

		//add label node
		//IRNode irnode = new IRNode("LABEL", "", "", dowhilelabel);
		//irnode.printNode();
		//TinyNode tnode = new TinyNode("label", "", dowhilelabel);
		//tinylist.add(tnode);
		
		
	}

	public static void breakOut() {	
		

		//add jmp node
		IRNode irnode = new IRNode("JMP", "", "", "label"+breaklabel);
		irnode.printNode();
		TinyNode tnode = new TinyNode("jmp", "", "label"+breaklabel);
		tinylist.add(tnode);
	}

    public static void addLiteral(String lit) {
		
		String op1 = lit;
		String res = newRegister();
		String t_res = newTinyReg();

		//replace $T with r
		op1 = op1.replace("$T","r");
		
		//lower number by 1
		if (op1.contains("r")) {
		
			op1 = "r" + Integer.toString(op1.compareTo("r0")-1);
		}

		String optype = "I";

		IRNode irnode = new IRNode("STORE"+optype, op1, "", res); 
		irnode.printNode();
		TinyNode tnode = new TinyNode("move", op1, res); 
		tinylist.add(tnode);		
		//tnode.printNode();
		stack.push(res);
    }
	
	public static void printTinyList(){

		for (int i = 0; i < tinylist.size(); i++) {

			//check the indexes with the list, and print
			TinyNode node = tinylist.get(i);
			node.printNode();
		}
		System.out.println("sys halt");
    }

    public static void addRIdentifier(String id) {
		
		stack.push(id);
    }

    public static void addLIdentifier(String id) {
		
		stack.push(id);
    }

    public static String newRegister() {

		return "$T"+Integer.toString(registercount++);
    }

    public static String newTinyReg() {

		return "r"+Integer.toString((tinyregcount++)-1);
    }

	 public static String newLabel() {

		return "label"+Integer.toString(labelcount++);
    }

    public static String newTinyLabel() {

		return "label"+Integer.toString(tinylabelcount++);
    }

	public static String newTinySRLabel() {
	
		breaklabel = tinylabelcount;
		return "label"+Integer.toString(tinylabelcount++);
    }
	
}
