public class TinyNode {

	private String opcode;
	private String first_operand;
	private String second_operand;
	
	public TinyNode(String opcode, String first_operand, String second_operand) {

		this.opcode = opcode;
		this.first_operand = first_operand;
		this.second_operand = second_operand;
	}
		
	public void printNode() {

		System.out.println(opcode + " " + first_operand + " " + second_operand);
	}
}
