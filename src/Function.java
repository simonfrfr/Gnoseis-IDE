import java.io.File;


public class Function {
	String function;
	String x;
	String call;
	//private char[] opperatorChars = {'+','-','*','%','/','(',')'};
	//inputs: "x^2+2x+7", "x"
	public Function(String function, String x,String call) {
		this.function = function;
		this.x = x;
		this.call = call;
	}
	public String getFunction(){
		return function;
	}
	public String getX(){
		return x;
	}
	public String call(){
		return call;
	}
	public String getY(String x){
		String y = "";
		Parser k = new Parser(function);
		k.values.add(x);
		k.names.add(this.x);
		double g = k.parse();
		if (k.Error == false) {
			y = g+"";
		}
		return y;
	}
	//inputs: File (For lagrange processing)
	public Function(File file) {
	}
}
