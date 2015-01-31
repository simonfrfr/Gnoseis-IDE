import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Tab {
	private String Name;
	private File file;
	private String Type;
	private boolean Selected = false;
	private String[] parameters;
	public boolean initialized = false;
	private BufferedReader reader;
	public boolean edited = false;
	public Tab(String Name, String Type){
		this.Name = Name;
		this.Type = Type;
		if (Type.equals("γνω")) {
			parameters = new String[8];
			parameters[0] = "Intensity vs Wavelength"; // graph name
			parameters[1] = "nm"; // X name
			parameters[2] = "relative Intensity"; // Y name
			parameters[3] = "0"; // minX
			parameters[4] = "00.00"; // minY
			parameters[5] = "800"; // maxX
			parameters[6] = "4096"; // maxY
			parameters[7] = "Data.csv"; // dataFiles
		}
		if (Type.equals("σείς")) {
			parameters = new String[5];
			parameters[0] = "";
		}
		if (Type.equals("csv")) {
			parameters = new String[1];
			parameters[0] = "0,0";
		}
	}
	public Tab(File file) throws NumberFormatException, IOException {
		this.file = file;
		Name = file.getName().substring(0, file.getName().indexOf('.'));
		Type = file.getName().substring(file.getName().indexOf('.')+1);
		if (Type.equals("γνω")) {
			parameters = new String[8];
			reader = new BufferedReader(new FileReader(file));
	    	String line = null;
	    	int lncnt = 0;
	    	while ((line = reader.readLine()) != null) {
	    		parameters[lncnt] = line;
	    		lncnt++;
	    	}
		}
		if (Type.equals("σείς")) {
			parameters = new String[1];
			reader = new BufferedReader(new FileReader(file));
	    	String line = null;
	    	while ((line = reader.readLine()) != null) {
	    		parameters[0] += line+"\n";
	    	}
		}
		if (Type.equals("csv")) {
			reader = new BufferedReader(new FileReader(file));
	    	String line = null;
	    	parameters = new String[1];
	    	parameters[0] = "";
	    	while ((line = reader.readLine()) != null) {
	    		if (line.split(",").length >= 2) {
	    			parameters[0] += line+"\n";
	    		}
	    		else {
	    			System.out.println("INTERPRETATION ERROR 51");
	    			return;
	    		}
	    	}
	    	reader.close();
		}
	}
	public boolean equaled(Tab thing){
		if (Type.equals(thing.getType()) && Name.equals(thing.getName()) && parameters[0].equals(thing.getParameter(0))) return true;
		return false;
	}
	public String getName(){
		return Name;
	}
	public int getParameterlength(){
		return parameters.length;
	}
	public String getType(){
		return Type;
	}
	public String getParameter(int number){
		return parameters[number];
	}
	public void setParameter(int number, String Value){
		parameters[number] = Value;
	}
	public void setType(String Type){
		this.Type = Type;
	}
	public File getFile(){
		return file;
	}
	public boolean getSelected(){
		return Selected;
	}
	public void setName(String name){
		Name = name;
	}
	public void setSelected(boolean select){
		Selected = select;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
