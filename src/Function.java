import java.util.HashMap;
import java.util.LinkedList;

//function class
public class Function {

		//counters
        public int localCount;
        public int parameterCount;
		//strings
        public String functionName;
		//data structures
        public LinkedList<IRNode> irNodeList = new LinkedList<IRNode>();
        public HashMap<String, String> parameterVariables = new HashMap<String, String>();
        
        public void FunctionName(String functionName) {
			this.functionName = functionName;
        }

		public void Visualize() {
			System.out.println("");
		}
}
