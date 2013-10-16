//declare IRNode class 
public class IRNode {

	//declare IRNode properties
	private String opcode, first_operand, second_operand, result;
	
	//***********************
	//	methods
	//
	//***********************

	//the constructor	
	public IRNode(String opcode, String first_operand, String second_operand, String result) {

		this.opcode = opcode;
		this.first_operand = first_operand;
		this.second_operand = second_operand;
		this.result = result;
	}
	
	//checks the opcode
	public String checkOpcode() {

		return this.opcode;
	}

	//checks the first operand
	public String checkFirstOp() {

		return this.first_operand;
	}

	//checks the second operand
	public String checkSecondOp() {

		return this.second_operand;
	}

	//checks the result
	public String checkResult() {

		return this.result;
	}

	//stores the result
	public void storeResult(String result) {

		this.result = result;
	}

	//stores the second_operand
	public void storeFactor(String opcode, String second_operand) {

		this.opcode = opcode;
		this.second_operand = second_operand;
	}

	//prints the IRNode code
	public void printIRNode() {

		System.out.println("opcode " + this.opcode + " first_operand " + this.first_operand + " second_operand " + this.second_operand + " result " + this.result);
	}
	
	//prints the IRNode updated code
	public void printIRUpdate() {

		System.out.println("opcode " + this.opcode + " first_operand " + this.first_operand + " second_operand " + this.second_operand + " result " + this.result + " *Assignment");
	}
}
