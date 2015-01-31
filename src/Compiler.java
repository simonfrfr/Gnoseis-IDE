import java.util.ArrayList;


public class Compiler {
	static String[] commands = {"f(x)", "graph", "integrate","averagefunction","abs","derive","areaundercurve","areabetweencurves"};
	static ArrayList <String> values = new ArrayList<>();
	static ArrayList <String> names = new ArrayList<>();
	static ArrayList <Function> functions = new ArrayList<>();
	private static String Compileln(String code, int ln) {
		String complier = code;
		if (code.equals(null) || code.equals("")) return "Where is your code?";
		if (code.indexOf("--") > -1) {
			return Compileln(code.substring(0, code.indexOf("--")), ln);
		}
		Parser k = new Parser(code);
		k.values = values;
		k.names = names;
		double g = k.parse();
		if (k.Error == false) {
			names.add(code);
			values.add(g+"");
			complier += "\n===>"+g;
		}
		else if (code.indexOf("=") > -1) {
			int EQLoc = code.indexOf("=");
			k = new Parser(code.substring(code.indexOf("=")+1, code.length()));
			k.values = values;
			k.names = names;
			g = k.parse();
			if (k.Error == false) {
				names.add(code.substring(0,code.indexOf("=")));
				values.add(g+"");
				complier += "\n===>"+g;
			}
			else if (code.indexOf('(') >= 0 && code.indexOf(')') >= 0) {
				complier += "\nParsing Function...";
				//code.indexOf('(')
				if (runOrSet(code,code.indexOf('('), code.indexOf(')')) == true) {
					for (int i = 0; i < functions.size(); i++){
						int eqIndex = functions.get(i).call().indexOf('(');
						if (code.indexOf(functions.get(i).call().substring(0,eqIndex)) > -1)  {
							complier += "\n@X=\""+code.substring(eqIndex+1, code.indexOf(')'))+"\"\n===>Y=\""+functions.get(i).getY(code.substring(eqIndex+1, code.indexOf(')')))+"\"";
						
						}
					}
				}
				else {
					functions.add(new Function(code.substring(EQLoc+1),code.substring(code.indexOf('(')+1, code.indexOf(')')), code.substring(0,code.indexOf('='))));
					complier += "\nFunction: \""+functions.get(functions.size()-1).getFunction()+"\"\nX=\""+functions.get(functions.size()-1).getX()+"\"";
				}
				
				
			}
			else return "Syntax Error! Unknown Command: \""+code+"\"\nException on Line "+(ln+1);
		}
		else {
			return "Syntax Error! Unknown Command: \""+code+"\"\nException on Line "+(ln+1);
		}
		return complier;
	}
	private static boolean runOrSet(String code, int left, int right) {
		String z = code.substring(left+1,right);
		if (z.charAt(0) == '0' || z.charAt(0) == '1' || z.charAt(0) == '2' || z.charAt(0) == '3' || z.charAt(0) == '4' || z.charAt(0) == '5' || z.charAt(0) == '6' || z.charAt(0) == '7' || z.charAt(0) == '8' || z.charAt(0) == '9') {
			
			return true;
		}
		return false;
	}
	public static String Compile(String code) {
		String complier = "";
		if (code.indexOf("\n")>-1) {
		String[] j = code.split("\n");
		values = new ArrayList<>();
		names = new ArrayList<>();
		for (int i = 0; i < j.length; i++) {complier += Compileln(j[i],i)+"\n"; Run.terminalArea.setText(complier);}
		}
		else {
			complier += Compileln(code,1)+"\n"; Run.terminalArea.setText(complier);
		}
		return complier;
	}
	@SuppressWarnings("unused")
	private static int matchParenthesis(String command, String code) {
		int CharLoc = -1;
		int firstPos = -1;
		
		for (int i = 0; i < code.length(); i++){
			if (code.substring(i,i+1).equals("(")) CharLoc--;
			if (code.substring(i,i+1).equals(")")) CharLoc++;
			if (code.substring(i,i+1).equals(")") && CharLoc == -1) firstPos = i;
		}
		return firstPos;
	}

	    
	
}

