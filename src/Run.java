import gnu.io.CommPortIdentifier;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
//interpretation error 51: there is an unequal number of X and Y Values (Load).
//interpretation error 52: there is an unequal number of X and Y Values (Save).
//Load Error 50: could not load file.
//Print Error 49: not able to print document


public class Run  extends Frame implements MouseMotionListener, MouseListener, KeyListener, Runnable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1563809341591403043L;
	static Frame f;
	static MenuItem Place = new MenuItem("Place");
    static MenuItem Wire = new MenuItem("Wire");
	static MenuBar menuBar = new MenuBar();
	static ArrayList <Tab> Tabs = new ArrayList<>();
	static int rows = 3; //You should decide the values for rows and cols variables  
    static int cols = 6; 
	static int chunks = rows * cols; 
	static String XtraText = "";
	static String XtraText2 = "";
	static BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
	static Graphics bufferGraphics;
	static JFileChooser fc;
	static BufferedImage buttonImages[] = new BufferedImage[7];
	static BufferedImage midTab[] = new BufferedImage[2];
	static BufferedImage leftTab[] = new BufferedImage[2];
	static BufferedImage rightTab[] = new BufferedImage[2];
	static DateFormat dateFormat = new SimpleDateFormat("MM-dd");
	static PopupMenu mouseTabMenu;
	static Date date = new Date();
	static int dotcnt = 0;
	static BufferedImage image;
	static JTextArea textArea = new JTextArea();
	static JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	static JTextArea terminalArea = new JTextArea();
	static JScrollPane terminalscroll = new JScrollPane (terminalArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private Thread clockThread = null;
	static Tab rawData = new Tab("Data-"+dateFormat.format(date), "csv");
	static USBComm comm;
	static TextField minX = new TextField("minX",20);
	static TextField minY = new TextField(20);
	static TextField maxX = new TextField(20);
	static TextField maxY = new TextField(20);
	static String Stream = "";
	static int mouseX = 0;
	static int mouseY = 0;
	static int rcMouseX = mouseX;
	static int rcMouseY = mouseY;
	static Color graphColors[] = {new Color(0,0,255), new Color(0,255,255), new Color(255,0,0) , new Color(255,255,0) , new Color(0,255,0) , new Color(255,0,255) , new Color(0,0,0)};
	Timer timer; 
	boolean b;   // for starting and stopping animation
	boolean c = false; //for starting and stopping animation
	boolean once = false;
	public static void loadCircuit(File file) throws IOException {
    	System.out.println(file.getName());//γνω, σείς
    	if (file.getName().indexOf('.') > 1 && file.getName().substring(file.getName().indexOf('.')).equals(".csv")) {
    	Tab lik = new Tab(file);
    	Tabs.add(lik);
    	}
    	if (file.getName().indexOf('.') > 1 && file.getName().substring(file.getName().indexOf('.')).equals(".σείς")) {
        	Tab lik = new Tab(file);
        	Tabs.add(lik);
        }
    	if (file.getName().indexOf('.') > 1 && file.getName().substring(file.getName().indexOf('.')).equals(".γνω")) {
        	Tab lik = new Tab(file);
        	Tabs.add(lik);
        }
    }
	/*
	 * 
	 * parameters[0] = "Intensity vs Wavelength"; // graph name
			parameters[1] = "nm"; // X name
			parameters[2] = "relative Intensity"; // Y name
			parameters[3] = "-600.00"; // minX
			parameters[4] = "0.00"; // minY
			parameters[5] = "600"; // maxX
			parameters[6] = "4095"; // maxY
			parameters[7] = "Data.csv"; // dataFiles
	 */
    public static double[] convertDoubles(String csv, int XorY)
    {
    	
        String[] ret = csv.split(",");
        //System.out.println(ret.length);
        double[] doubles = new double[ret.length];
        //System.out.println(doubles.length);
       /* if (XorY == 0){
        	try {
        	doubles[0] = Double.parseDouble(ret[0].replaceAll("[^\\d.]", ""));
        	}catch (NumberFormatException e) {
    			return new double[0];
    		}
        	}*/
        for(int i = 1; i < doubles.length; i++) {
        	String[] ret2 = ret[i].split("\n");
        	//System.out.println(ret2[0]);
        	if (XorY == 1){
        		try {
        			doubles[i] = Double.parseDouble(ret2[0].replaceAll("[^\\d.]", ""));
        		} catch (NumberFormatException e) {
        			System.out.println(e.getMessage());
        		}
        		
        	}
        	if (XorY == 0){
        		if (i < doubles.length-1) 
        			try {
        				doubles[i+1] = Double.parseDouble(ret2[1].replaceAll("[^\\d.]", ""));
            		} catch (NumberFormatException e) {
            			System.out.println(e.getMessage());
            		}
            	
            }
        }
        
        return doubles;
    }
    public static void PrintItOUT(){
    	image = new BufferedImage(f.getWidth(), f.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		f.printAll(g);
		System.out.println(image.getWidth() + "x" + image.getHeight());

		PrinterJob pj = PrinterJob.getPrinterJob();
		if (pj.printDialog()) {
		    PageFormat pf = pj.defaultPage();
		    Paper paper = pf.getPaper();
//                        86X54mm
		    double width = fromCMToPPI(21.0);
		    double height = fromCMToPPI(27.0);
		    paper.setSize(width, height);
		    paper.setImageableArea(
		                    fromCMToPPI(0.1),
		                    fromCMToPPI(0.1),
		                    width - fromCMToPPI(0.1),
		                    height - fromCMToPPI(0.1));
		    pf.setOrientation(PageFormat.PORTRAIT);
		    pf.setPaper(paper);
		    PageFormat validatePage = pj.validatePage(pf);
		    System.out.println("Valid- " + dump(validatePage));
		    pj.setPrintable(new PrintTest02.MyPrintable(), validatePage);
		    try {
		        pj.print();
		    } catch (PrinterException ex) {
		        ex.printStackTrace();
		    }
		}
    }
    protected static double fromPPItoCM(double dpi) {
        return dpi / 72 / 0.393700787;
    }

    protected static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    protected static String dump(Paper paper) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
                        .append("/").append(paper.getImageableX()).append("x").
                        append(paper.getImageableY()).append(" - ").append(paper
                        .getImageableWidth()).append("x").append(paper.getImageableHeight());
        return sb.toString();
    }

    protected static String dump(PageFormat pf) {
        Paper paper = pf.getPaper();
        return dump(paper);
    }
    @SuppressWarnings("deprecation")
	public static void dropMenu() {
        // Add the menubar to the frame
        f.setMenuBar(menuBar);
        
        JFileChooser fc;
    	JLabel p = new JLabel();
    	f.add(minX);
    	f.add(minY);
    	f.add(maxX);
    	f.add(maxY);
    	minX.setVisible(true);
    	minX.setFont(f.getFont());
    	minY.setVisible(true);
    	minY.setFont(f.getFont());
    	maxX.setVisible(true);
    	maxX.setFont(f.getFont());
    	maxY.setVisible(true);
    	maxY.setFont(f.getFont());
    	FontMetrics l = f.getFontMetrics(f.getFont());
    	minX.resize(l.getMaxAdvance()*4,l.getHeight()+10);
    	minY.resize(l.getMaxAdvance()*4,l.getHeight()+10);
    	maxX.resize(l.getMaxAdvance()*4,l.getHeight()+10);
    	maxY.resize(l.getMaxAdvance()*4,l.getHeight()+10);
    	System.out.println(minX.toString());
    	//TODO ADD in text editor...
    	Tabs.add(rawData);
    	terminalscroll.setLocation(0, f.getHeight()-28-74);
    	terminalscroll.setVisible(false);
    	terminalArea.setEditable(false);
    	f.add(terminalscroll);
    	scroll.setLocation(8, 116);
    	f.add(scroll);
    	mouseTabMenu = new PopupMenu();
		MenuItem selectTab = new MenuItem("Select Tab");
		MenuItem closeTab = new MenuItem("Close Tab");
		MenuItem tabSave = new MenuItem("Save Tab");
		mouseTabMenu.add(selectTab);
		mouseTabMenu.add(closeTab);
		mouseTabMenu.add(tabSave);
		f.add(mouseTabMenu);
        //Create a file chooser
        fc = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("γνω, σείς, and csv","γνω","σείς","csv");
        fc.setFileFilter(filter);
        // Define and add two drop down menu to the menubar
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");
        Menu toolsMenu = new Menu("Tools");
        Menu helpMenu = new Menu("Help");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        // Create and add simple menu item to one of the drop down menu
        MenuItem newAction = new MenuItem("New");
        MenuItem openAction = new MenuItem("Open");
        MenuItem printAction = new MenuItem("Print");
        MenuItem SaveAction = new MenuItem("Save");
        MenuItem SaveAsAction = new MenuItem("Save As");
        MenuItem SaveAllAction = new MenuItem("Save All");
        MenuItem exitAction = new MenuItem("Exit");
        MenuItem VCCMenu = new MenuItem("VCC Value");
        MenuItem FilterMenu = new MenuItem("Filter Values");
        //JMenuItem pasteAction = new JMenuItem("Paste");
        
        //Tools Menu:
        MenuItem autoformat = new MenuItem("Auto Format");
        autoformat.setShortcut(new MenuShortcut(KeyEvent.VK_T, false));
        MenuItem Archive = new MenuItem("Archive Sketch");
        MenuItem fixandReload = new MenuItem("Fix Encoding & Reload");
        MenuItem GraphMonitor = new MenuItem("Graph Realtime");
        MenuItem RefreshPorts = new MenuItem("Refresh Ports");
        GraphMonitor.setShortcut(new MenuShortcut(KeyEvent.VK_M, true));
        Menu Board = new Menu("Board");
        MenuItem Ena = new CheckboxMenuItem("Γνωσεις (Gnoseis) ΕΝΑ", true);
        Board.add(Ena);
        Menu PortInterface = new Menu("Interface Port");
        //addSerialPorts(PortInterface);
        addSerialPorts(PortInterface);
       
		
        MenuItem FirmwareUpdate = new MenuItem("Update Firmware");
        toolsMenu.add(autoformat);
        toolsMenu.add(Archive);
        toolsMenu.add(fixandReload);
        toolsMenu.add(GraphMonitor);
        toolsMenu.addSeparator();
        toolsMenu.add(PortInterface);
        toolsMenu.add(Board);
        toolsMenu.add(RefreshPorts);
        toolsMenu.addSeparator();
        toolsMenu.add(FirmwareUpdate);
        //Help Menu:
        MenuItem newbies = new MenuItem("Getting Started");
        MenuItem environment = new MenuItem("Environment");
        MenuItem reference = new MenuItem("Reference");
        MenuItem referenceFind = new MenuItem("Find in Reference");
        MenuItem FAQ = new MenuItem("Frequently Asked Questions");
        MenuItem web = new MenuItem("Visit gnoseis.com");
        //separatorGoesHere
        MenuItem aboutProject = new MenuItem("About Γνωσεις (Gnoseis)");
        helpMenu.add(newbies);
        //
        helpMenu.add(environment);
        helpMenu.add(reference);
        helpMenu.add(referenceFind);
        helpMenu.add(FAQ);
        helpMenu.add(web);
        helpMenu.addSeparator();
        helpMenu.add(aboutProject);
        // Create and add CheckButton as a menu item to one of the drop down
        // menu
        MenuItem checkAction = new CheckboxMenuItem("Hints?");
        // Create and add Radio Buttons as simple menu items to one of the drop
        // down menu
        
        //checkAction.setSelected(true);
        // Create a ButtonGroup and add both radio Button to it. Only one radio
        // button in a ButtonGroup can be selected at a time.
     // TODO Add More Menu Options
        fileMenu.add(newAction);
        newAction.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
        fileMenu.add(openAction);
        openAction.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
        fileMenu.add(checkAction);
        fileMenu.add(printAction);
        printAction.setShortcut(new MenuShortcut(KeyEvent.VK_P, false));
        fileMenu.add(SaveAction);
        fileMenu.add(SaveAsAction);
        fileMenu.add(SaveAllAction);
        SaveAllAction.setShortcut(new MenuShortcut(KeyEvent.VK_S, true));
        SaveAction.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        exitAction.setShortcut(new MenuShortcut(KeyEvent.VK_Q, false));
        editMenu.add(VCCMenu);
        editMenu.add(FilterMenu);
        //editMenu.add(pasteAction);
        editMenu.addSeparator();
        editMenu.add(Place);
        editMenu.add(Wire);
        // Add a listener to the New menu item. actionPerformed() method will
        // invoked, if user triggred this menu item
        VCCMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//VCCMakeGui();
            	System.out.println("VCCGui");
            }
        });
        web.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	try {
					open(new URI("http://www.gnoseis.com"));
				} catch (URISyntaxException e) {
					System.out.println("Unable to open desktop");
				}
            }
        });
        PortInterface.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	PortInterface.removeAll();
            	addSerialPorts(PortInterface);
            }
        });
         exitAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	f.dispose();
            }
        });
 		selectTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	if (rcMouseY > 83 && rcMouseY < 116) {
        			for (int i = 0; i < Tabs.size(); i++){
        			if (rcMouseX > 14+(i*107) && rcMouseX < 13+((i+1)*107)) {
        				for (int j = 0; j < Tabs.size(); j++){
        					//TODO DET HERE
        					if (Tabs.get(j).getSelected() == true){
        						if (Tabs.get(j).initialized == true) Tabs.get(j).setParameter(0, textArea.getText());
        						
        					Tabs.get(j).setSelected(false);
        					Tabs.get(i).setSelected(true);
        					Tabs.get(j).initialized = false;
        					}
        				}
        			}
        			}
        		}
            }
        });
 		closeTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	if (rcMouseY > 83 && rcMouseY < 116) {
        			for (int i = 0; i < Tabs.size(); i++){
        			if (rcMouseX > 14+(i*107) && rcMouseX < 13+((i+1)*107)) {
        				for (int j = 0; j < Tabs.size(); j++){
        					//TODO DET HERE
        					if (Tabs.get(j).getSelected() == true){
        						if (Tabs.get(j).initialized == true && !Tabs.get(j).getType().equals("γνω")) Tabs.get(j).setParameter(0, textArea.getText());
        						if (j == i && i != 0) 
        							Tabs.get(0).setSelected(true);
        						else if (j == i && i == 0 && Tabs.size() > 1) 
        							Tabs.get(1).setSelected(true);

        					Tabs.remove(i);
        					
        					}
        				}
        			}
        			}
        		}
            }
        });
 		RefreshPorts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	System.out.println("Searching For Serial Ports...");
            	PortInterface.removeAll();
            	addSerialPorts(PortInterface);
            }
        });
 		tabSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	if (rcMouseY > 83 && rcMouseY < 116) {
        			for (int i = 0; i < Tabs.size(); i++){
        			if (rcMouseX > 14+(i*107) && rcMouseX < 13+((i+1)*107)) {
        					if (Tabs.get(i).getFile() != null){
        						saveFunction(); // saveFile(Tabs.get(i).getFile(), i);
        					}
        					

        			}
        			}
        		}
            }
        });
        GraphMonitor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	if (buttonImages[5].equals(imgs[5])) {
    				buttonImages[5] = imgs[17];
    				endGnoseisStream();
    			}
    			else {
    			buttonImages[5] = imgs[5];
    			pollGnoseis();
    			}
            }
        });
        FirmwareUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	try {
					getNewFirmware();
				} catch (IOException e) {
					System.out.println("Error in connecting to host site");
				}
            }
        });
        FilterMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	System.out.println("FilterGui");
            }
        });
        openAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
        int returnVal = fc.showOpenDialog(f);
   	 
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println("Opening File: " + file.getName());
            try {
				loadCircuit(file);
			} catch (IOException e) {
				System.out.println("Load Error 50");
			}
            System.out.println("Opened File: " + file.getName());
        }
            }
        });
        Place.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//place = true;
    			//drawBox = false;
            	System.out.println("Place!");
            }
        });
        Wire.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//place = false;
            	System.out.println("Wire!");
            }
        });
        checkAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//hints = checkAction.isSelected();
            }
        });
        newAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//Deleteprompt = true;
    			//ndk = 1;
                newPrj();
            }
        });
        printAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//Deleteprompt = true;
    			//ndk = 1;
                System.out.println("Printing Graph!");
                PrintItOUT();
            }
        });
        SaveAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	saveFunction();
            }
        });
        SaveAsAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	
            	int index = -1;
 			   for (int i = 0; i < Tabs.size(); i++)
 				   if (Tabs.get(i).getSelected() == true) index = i;
 			   
            		fc.setSelectedFile(new File(Tabs.get(index).getName()));
            	int returnVal = fc.showSaveDialog(f);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //System.out.println(fc.getCurrentDirectory());
                    if (file.getName().length() < 5 || !file.getName().substring(file.getName().length()-4,file.getName().length()).equalsIgnoreCase("csv")) {
                    	
                    	file = new File(fc.getCurrentDirectory()+"\\"+file.getName()+"."+Tabs.get(index).getType());
                    }
                    else {
                    	new File(fc.getCurrentDirectory()+"\\"+file.getName());
                    }
                    saveFile(file, index);
                    System.out.println("File: "+ file.getName()+ " Saved!");
  
            	}
            }
        });
        SaveAllAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
     		   saveAll();
            }
        });
        f.add(p);
       Run.fc = fc;
    }
	  protected static void saveAll() {
		  for (int index = 0; index < Tabs.size(); index++) {
    		   
	           	if (index != -1 && Tabs.get(index).getFile() != null && Tabs.get(index).getFile().exists()) {
	           		saveFile(Tabs.get(index).getFile(), index);
	           	}
	           	else {
	           		fc.setSelectedFile(new File(Tabs.get(index).getName()));
	           	int returnVal = fc.showSaveDialog(f);
	               if (returnVal == JFileChooser.APPROVE_OPTION) {
	                   File file = fc.getSelectedFile();
	                   //System.out.println(fc.getCurrentDirectory());
	                   if (file.getName().length() < 5 || !file.getName().substring(file.getName().length()-4,file.getName().length()).equalsIgnoreCase("csv")) {
	                   	
	                   	file = new File(fc.getCurrentDirectory()+"\\"+file.getName()+"."+Tabs.get(index).getType());
	                   }
	                   else {
	                   	new File(fc.getCurrentDirectory()+"\\"+file.getName());
	                   }
	                   saveFile(file, index);
	                   System.out.println("File: "+ file.getName()+ " Saved!");
	               }

	           	}
	            }
	}
	protected static void saveFunction() {
		// TODO Auto-generated method stub

      int index = -1;
      if (Tabs.size() > 0) {
		   for (int i = 0; i < Tabs.size(); i++)
			   if (Tabs.get(i).getSelected() == true) index = i;
		   
      	if (index != -1 && Tabs.get(index).getFile() != null && Tabs.get(index).getFile().exists()) {
      		saveFile(Tabs.get(index).getFile(), index);
      	}
      	else {
      		fc.setSelectedFile(new File(Tabs.get(index).getName()));
      	int returnVal = fc.showSaveDialog(f);
          if (returnVal == JFileChooser.APPROVE_OPTION) {
              File file = fc.getSelectedFile();
              //System.out.println(fc.getCurrentDirectory());
              if (file.getName().length() < 5 || !file.getName().substring(file.getName().length()-4,file.getName().length()).equalsIgnoreCase("csv")) {
              	
              	file = new File(fc.getCurrentDirectory()+"\\"+file.getName()+"."+Tabs.get(index).getType());
              }
              else {
              	new File(fc.getCurrentDirectory()+"\\"+file.getName());
              }
              saveFile(file, index);
              System.out.println("File: "+ file.getName()+ " Saved!");
          }

      	}
      }
      else {
    	  System.out.println("NOTHING TO SAVE!");
      }
	}
	protected static void saveFile(File file, int index) {
		// TODO Auto-generated method stub
		  
		  try {
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pr = new PrintWriter(bw);
				//if (x.size() == y.size()) {
				//for (int i = 0; i < x.size(); i++) {
			    if (file.getName().indexOf('.') > 1 && (file.getName().substring(file.getName().indexOf('.')).equals(".σείς") || file.getName().substring(file.getName().indexOf('.')).equals(".csv"))) {
			    	if (Tabs.get(index).getSelected() == true) Tabs.get(index).setParameter(0, textArea.getText());
			    	
			    	pr.print(Tabs.get(index).getParameter(0));
				}
			    else {
			    	for (int i = 0;i<Tabs.get(index).getParameterlength(); i++)
			    	pr.println(Tabs.get(index).getParameter(i));
			    }
			    Tabs.get(index).edited = false;
				//}
				//}
				//else {
					//System.out.println("INTERPRETATION ERROR 52");
				//}
				pr.close();
				bw.close();
				fw.close();
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
		  Tabs.get(index).setName(file.getName().substring(0, file.getName().indexOf('.')));
	}
	public Run() {
		    super("Γνωσεις (Gnoseis) 0.00.01");
		    f = this;
		    setSize(500,600); 
		    setVisible(true);
		    ImageIcon img = new ImageIcon("favicons.png");
		    setIconImage(img.getImage());
		    dropMenu();
		    this.createBufferStrategy(2);
		    this.setMinimumSize(new Dimension(400,290));
		    addMouseMotionListener(this);
	        addMouseListener(this);
	        addKeyListener(this);
			gameLoop();
		    addWindowListener(new WindowAdapter() 
		      {public void windowClosing(WindowEvent e) 
		         {dispose(); System.exit(0);}  
		      }
		    );
		    
		  }
	  public static void newPrj(){
		  System.out.println("New Schematic!");
		  newMakeGui();
          
	  }
/*	  public void paint(Graphics g) {
	   System.out.println("repaint");
	   g.setColor(Color.WHITE);
	   g.fillRect(0, 0, f.getSize().width, f.getSize().height);
	   g.setColor(Color.BLUE);
	   //x.set(0, 0.0);
	   //y.set(0, 0.0);
	   //x.set(1, 0.0);
	   //y.set(1, 0.0);
	   Graphics2D g2d = (Graphics2D)g;
	   g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	   Graph graph = new Graph(50,80,350,350,0,-10,2*Math.PI,10);
	  // if (x.size() > 20) {
	   graph.g2D(convertDoubles(x), convertDoubles(y), g2d);
	   //bufferGraphics[0] = g;
	   //}

   }*/

   public static void main(final String[] args) throws IOException
   {
	   File file = new File("theme/buttons.jpg");   
       FileInputStream fis = new FileInputStream(file);  
       BufferedImage imaged = ImageIO.read(fis); //reading the image file  
       leftTab[0] = ImageIO.read(new FileInputStream(new File("theme/leftTab.PNG"))); 
       rightTab[0] = ImageIO.read(new FileInputStream(new File("theme/rightTab.PNG"))); 
       midTab[0] = ImageIO.read(new FileInputStream(new File("theme/midTab.PNG"))); 
       leftTab[1] = ImageIO.read(new FileInputStream(new File("theme/leftTabU.PNG"))); 
       rightTab[1] = ImageIO.read(new FileInputStream(new File("theme/rightTabU.PNG"))); 
       midTab[1] = ImageIO.read(new FileInputStream(new File("theme/midTabU.PNG"))); 
       int chunkWidth = imaged.getWidth() / cols; // determines the chunk width and height  
       int chunkHeight = imaged.getHeight() / rows;  
       int count = 0;  
         
       for (int x = 0; x < rows; x++) {  
           for (int y = 0; y < cols; y++) {  
               //Initialize the image array with image chunks  
               imgs[count] = new BufferedImage(chunkWidth-10, chunkHeight-10, imaged.getType());  
 
               // draws the image chunk  
               Graphics2D gr = imgs[count++].createGraphics();  
               gr.drawImage(imaged, 0, 0, chunkWidth-10, chunkHeight-10, (chunkWidth * y)+5, (chunkHeight * x)+5, (chunkWidth * y + chunkWidth)-5, (chunkHeight * x + chunkHeight)-5, null);  
               gr.dispose();  
           }  
       }  
   	buttonImages[0] = imgs[12];
   	buttonImages[1] = imgs[13];
   	buttonImages[2] = imgs[14];
   	buttonImages[3] = imgs[15];
   	buttonImages[4] = imgs[16];
   	buttonImages[5] = imgs[17];
   	Tabs.add(new Tab("Graph-"+dateFormat.format(date), "γνω"));
   	Tabs.add(new Tab("Program-"+dateFormat.format(date), "σείς"));
   	Tabs.get(0).setSelected(true);
   		comm = new USBComm();

	   new Run();
   }

	public void keyPressed(KeyEvent arg0) {
		//System.out.println(arg0.getKeyCode());
	}
	public void update(Graphics g) 
    { 
	 Graphics offgc;
	    Image offscreen = null;
	    @SuppressWarnings("deprecation")
		Dimension d = size();
	    // create the offscreen buffer and associated Graphics
	    offscreen = createImage(d.width, d.height);
	    offgc = offscreen.getGraphics();
	    // clear the exposed area
	    offgc.setColor(getBackground());
	    offgc.fillRect(0, 0, d.width, d.height);
	    offgc.setColor(getForeground());
	    // do normal redraw
	    paint(offgc);
	    // transfer offscreen to window
	    g.drawImage(offscreen, 0, 0, this);
    } 
	public void keyReleased(KeyEvent arg0) {
		//System.out.println(arg0.getKeyCode());
		
	}

	public void keyTyped(KeyEvent arg0) {
		//System.out.println(arg0.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
	private static void getNewFirmware() throws UnsupportedEncodingException, IOException{
		URL url = new URL("http://gnoseis.com/update.php");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
		    for (String line; (line = reader.readLine()) != null;) {
		        System.out.println(line);
		    }
		}
	}

	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() != 1) {
			//TODO
			if (mouseY > 83 && mouseY < 116) {
				for (int i = 0; i < Tabs.size(); i++){
				if (mouseX > 14+(i*107) && mouseX < 13+((i+1)*107)) {
					rcMouseX = mouseX;
					rcMouseY = mouseY;
					mouseTabMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
				}
				}
			}
			
			
		}
		if (arg0.getButton() == 1) {
		if (mouseY > 83 && mouseY < 116) {
			for (int i = 0; i < Tabs.size(); i++){
			if (mouseX > 14+(i*107) && mouseX < 13+((i+1)*107)) {
				for (int j = 0; j < Tabs.size(); j++){
					//TODO DET HERE
					if (Tabs.get(j).getSelected() == true){
						if (Tabs.get(j).initialized == true && !Tabs.get(j).getType().equals("γνω")) Tabs.get(j).setParameter(0, textArea.getText());
						
					Tabs.get(j).setSelected(false);
					Tabs.get(i).setSelected(true);
					Tabs.get(j).initialized = false;
					}
				}
			}
			}
		}
		if (mouseY > 57 && mouseY < 80) {
		if (mouseX > 13 && mouseX < 36){
			buttonImages[0] = imgs[0];
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			buttonImages[0] = imgs[12];
		}
		if (mouseX > 13+23+4 && mouseX < 13+23+4+23){
			buttonImages[1] = imgs[1];
			for (int i = 0; i < Tabs.size(); i++)
				if (Tabs.get(i).getType() == "σείς") {
					if (Tabs.get(i).getSelected() == true) Tabs.get(i).setParameter(0, textArea.getText());
					terminalArea.setVisible(true);
					terminalscroll.setVisible(true);
					terminalArea.setText(Compiler.Compile(Tabs.get(i).getParameter(0)));
					//Compiler.Compile(Tabs.get(i).getParameter(0));
					
				}
			buttonImages[1] = imgs[13];
		}
		if (mouseX > 13+23+23+4+11 && mouseX < 13+23+23+4+11+23){
			buttonImages[2] = imgs[2];
			newPrj();
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
            buttonImages[2] = imgs[14];
		}
		if (mouseX > 13+23+23+23+11+4+4 && mouseX < 13+23+23+23+11+4+4+23){
			buttonImages[3] = imgs[3]; //load
		        int returnVal = fc.showOpenDialog(f);
		   	 
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //This is where a real application would open the file.
		            System.out.println("Opening File: " + file.getName());
		            try {
						loadCircuit(file);
					} catch (IOException e) {
						System.out.println("Load Error 50");
					}
		            
		            System.out.println("Opened File: " + file.getName());
		        }
		        buttonImages[3] = imgs[15];
		}
		if (mouseX > 13+23+23+23+23+4+11+4+4 && mouseX < 13+23+23+23+23+4+11+4+4+23){
			buttonImages[4] = imgs[4]; //save

			saveFunction();
            buttonImages[4] = imgs[16];
		}
		if (mouseX > f.getWidth()-24-23 && mouseX < f.getWidth()-24){
			c = false;
			if (buttonImages[5].equals(imgs[5])) {
				buttonImages[5] = imgs[17];
				endGnoseisStream();
			}
			else {
			buttonImages[5] = imgs[5];
			pollGnoseis();
			}
		}
		}
		}
	}

	private static void endGnoseisStream() {
		// TODO THIS METHOD IS TO STOP DEVICE FROM SENDING DATA
		try
	    {
			comm.closeUSB();
	    }
	    catch ( Exception e )
	    {
	        // TODO Auto-generated catch block
	        System.out.println(e.getLocalizedMessage());
	    }
	}
	private static void startGnoseisStream(String Port) {
		// TODO THIS METHOD IS TO CONNECT TO A DEVICE
		try
	    {
	        comm = new USBComm(Port);
	    }
	    catch ( Exception e )
	    {
	        // TODO Auto-generated catch block
	        System.out.println(e.getLocalizedMessage());
	    }
	}
	public static String bytesToStringUTFCustom(byte[] bytes) {
		 char[] buffer = new char[bytes.length >> 1];
		 for(int i = 0; i < buffer.length; i++) {
		  int bpos = i << 1;
		  char c = (char)(((bytes[bpos]&0x00FF)<<8) + (bytes[bpos+1]&0x00FF));
		  buffer[i] = c;
		 }
		 return new String(buffer);
		}
	public static byte[] toByteArray(ArrayList<Byte> in) {
	    final int n = in.size();
	    byte ret[] = new byte[n];
	    for (int i = 0; i < n; i++) {
	        ret[i] = in.get(i);
	    }
	    return ret;
	}
	private static void refreshGnoseisStream() {
		if (comm.started == true) {
		ArrayList<Byte> Quere = new ArrayList<>();
		comm.receivedBytes.drainTo(Quere);
		for (int z = 0; z < Tabs.size(); z++) {
			if (Tabs.get(z).equaled(rawData)){
				rawData.setParameter(0, Stream);
				Tabs.get(z).setParameter(0, Stream);
			}
		}
		String j = new String(toByteArray(Quere));
			System.out.print(j);
			if (j.indexOf("$") > -1) {
				for (int z = 0; z < Tabs.size(); z++) {
					if (Tabs.get(z).equaled(rawData)){
						System.out.println("got $!");
						System.out.println(Stream);
						rawData.setParameter(0, Stream);
						Tabs.get(z).setParameter(0, Stream);
						Stream = "";
						pollGnoseis(); //Restart stream after Char given
					}
				}
				
				
				
			}
			else {
				Stream += j;
			}
		}
	}
	private static void addSerialPorts(Menu j){
		 @SuppressWarnings("rawtypes")
			Enumeration ports = CommPortIdentifier.getPortIdentifiers();
			while (ports.hasMoreElements()) {
			    CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
			    CheckboxMenuItem box = new CheckboxMenuItem(port.getName());
			    System.out.println(port.getName());
			    box.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						if (box.getState() == true) {
						System.out.println("Connecting to "+box.getLabel());
		            	startGnoseisStream(box.getLabel());
						}
					}
		        });
			    j.add(box);
			} 
	}
	private static void newMakeGui() {
	    JFrame frame;
 		frame = new JFrame("New");
 		frame.setAlwaysOnTop(true);
 		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

 		JPanel panel = new JPanel(new GridBagLayout());
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        JButton newProject = new JButton("New Project");
        JButton newProgram = new JButton("New Program");
        JButton newDataSet = new JButton("New Dataset");
        newProject.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
            	saveAll();
            	Tabs = new ArrayList<Tab>();
                Tabs.add(new Tab("Graph-"+dateFormat.format(date), "γνω"));
                Tabs.get(0).setSelected(true);
            	
                frame.dispose();
    			
            }
        }); 
        newProgram.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
                Tabs.add(new Tab("Program-"+dateFormat.format(date), "σείς"));
            	
                frame.dispose();
    			
            }
        }); 
        newDataSet.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
                Tabs.add(new Tab("Data-"+dateFormat.format(date), "csv"));
            	
                frame.dispose();
    			
            }
        }); 
        panel.add(newProgram,c);
        panel.add(newDataSet,c);
        panel.add(newProject,c);
        frame.add(panel);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        frame.pack();
 		frame.setVisible(true);
}
	private static void pollGnoseis() {
		// TODO THIS METHOD IS TO START SENDING DATA TO THE HOST
		comm.write("1".getBytes());
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
	mouseX = arg0.getX();
	mouseY = arg0.getY();
	boolean tyoe = false;
	/*
	  			g2d.drawImage(buttonImages[0], 13, 55,null);
			   g2d.drawImage(buttonImages[1], 13+23+4, 57,null);
			   g2d.drawImage(buttonImages[2], 13+23+23+4+11, 57,null);
			   g2d.drawImage(buttonImages[3], 13+23+23+23+11+4+4, 57,null);
			   g2d.drawImage(buttonImages[4], 13+23+23+23+23+4+11+4+4, 57,null);
			   g2d.drawImage(buttonImages[5], f.getWidth()-24-23, 57,null);
	 */
	XtraText = "";
	
	if (mouseY > 57 && mouseY < 80) {
		tyoe = true;
	}
	if (mouseX > 13 && mouseX < 36 && tyoe == true && !buttonImages[0].equals(imgs[0])){
		buttonImages[0] = imgs[6];
		XtraText = "Verify";
	}
	else if (!buttonImages[0].equals(imgs[0])){
		buttonImages[0] = imgs[12];
	}
	if (mouseX > 13+23+4 && mouseX < 13+23+4+23 && tyoe == true && !buttonImages[1].equals(imgs[1])){
		buttonImages[1] = imgs[7];
		XtraText = "Run";
	}
	else if (!buttonImages[1].equals(imgs[1])){
		buttonImages[1] = imgs[13];
	}
	if (mouseX > 13+23+23+4+11 && mouseX < 13+23+23+4+11+23 && tyoe == true && !buttonImages[2].equals(imgs[2])){
		buttonImages[2] = imgs[8];
		XtraText = "New";
	}
	else if (!buttonImages[2].equals(imgs[2])){
		buttonImages[2] = imgs[14];
	}
	if (mouseX > 13+23+23+23+11+4+4 && mouseX < 13+23+23+23+11+4+4+23 && tyoe == true && !buttonImages[3].equals(imgs[3])){
		buttonImages[3] = imgs[9];
		XtraText = "Open";
	}
	else if (!buttonImages[3].equals(imgs[3])){
		buttonImages[3] = imgs[15];
	}
	if (mouseX > 13+23+23+23+23+4+11+4+4 && mouseX < 13+23+23+23+23+4+11+4+4+23 && tyoe == true && !buttonImages[4].equals(imgs[4])){
		buttonImages[4] = imgs[10];
		XtraText = "Save";
	}
	else if (!buttonImages[4].equals(imgs[4])){
		buttonImages[4] = imgs[16];
	}
	if (mouseX > f.getWidth()-24-23 && mouseX < f.getWidth()-24 && tyoe == true && !buttonImages[5].equals(imgs[5])){
		buttonImages[5] = imgs[11];
		XtraText2 = "Graph Realtime";
	}
	else if (!buttonImages[5].equals(imgs[5])){
		buttonImages[5] = imgs[17];
		XtraText2 = "";
	}
	else if (buttonImages[5].equals(imgs[5]) && c == false) {
		c = true;
		XtraText2 = "Graphing";
	}
	}
	private void gameLoop() {
		// Your game logic goes here.
		
		drawStuff();
		refreshGnoseisStream();
		//TODO LOGIC
		if (scroll.getSize().getHeight()*scroll.getSize().getWidth() != (f.getWidth()-16)*(f.getHeight()-135-116)) {
			textArea.setSize(f.getWidth()-16, f.getHeight()-135-116);
			scroll.setSize(f.getWidth()-16, f.getHeight()-135-116);
			scroll.setLocation(8, 116);
			
			terminalArea.setSize(f.getWidth()-16,74);
			terminalscroll.setSize(f.getWidth()-16,74);
			terminalscroll.setLocation(8, f.getHeight()-28-74);
			minX.setLocation((int) (f.getWidth()-minX.getSize().getWidth())-10, 150);
			minY.setLocation((int) (f.getWidth()-minX.getSize().getWidth())-10, 200);
			maxX.setLocation((int) (f.getWidth()-minX.getSize().getWidth())-10, 250);
			maxY.setLocation((int) (f.getWidth()-minX.getSize().getWidth())-10, 300);
			//System.out.println(minX.getLocation());
			//   minY.setVisible(false);
			//   maxX.setVisible(false);
			//   maxY.setVisible(false);
			//terminalArea.setLocation(8, f.getHeight()-28-74);
		}
		start();
	}
	private void drawTab(Tab tab, int tabNumber, Graphics2D g2d){
		int l = 1;
		if (tab.getSelected() == true) l = 0;
		
		g2d.drawImage(leftTab[l], 14+(tabNumber*107), 83, null);
		   for (int i = 0; i < 25; i++){
			 g2d.drawImage(midTab[l], 14+(tabNumber*107)+(4*i)+4, 83, null);
		   }
		   g2d.drawImage(rightTab[l], 118+(tabNumber*107), 83, null);
		   g2d.setColor(Color.GRAY);
		   FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		   String edited = "";
		   if (tab.edited == true ) edited = "Ψ ";
		   String tabName = edited + tab.getName();
		   int lastPos = 0;
		   for (int i = 1; i <= tabName.length(); i++)
		   if (metrics.stringWidth(tabName.substring(0, i)) < 100) lastPos = i;
		   
		   
		   g2d.drawString(tabName.substring(0, lastPos),14+(tabNumber*107)+54-metrics.stringWidth(tabName.substring(0, lastPos))/2, 108);
	}
	private void drawStuff() {
		// Code for the drawing goes here.
		BufferStrategy bf = this.getBufferStrategy();
		Graphics g = null;
		try {
			g = bf.getDrawGraphics();
			   //System.out.println("repaint");
				Graphics2D g2d = (Graphics2D)g;
				g2d.setBackground(new Color(255,255,255,0));
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			   
			   
			   FontMetrics metrics;
			   int index = -1;
 			   for (int i = 0; i < Tabs.size(); i++)
 				   if (Tabs.get(i).getSelected() == true) index = i;

			   if (index != -1 && Tabs.get(index).getType().equals("γνω")) {
				   once = false;
				   if (Tabs.get(index).initialized == false) {
					   Tabs.get(index).initialized = true;
					   minX.setText(Tabs.get(index).getParameter(3));
					   minY.setText(Tabs.get(index).getParameter(4));
					   maxX.setText(Tabs.get(index).getParameter(5));
					   maxY.setText(Tabs.get(index).getParameter(6));
				   }
				   //System.out.println("gno");"
				   /*
				    * parameters[0] = "Intensity vs Wavelength"; // graph name
					* parameters[1] = "nm"; // X name
					* parameters[2] = "relative Intensity"; // Y name
					* parameters[3] = "0"; // minX
					* parameters[4] = "00.00"; // minY
					* parameters[5] = "800"; // maxX
					* parameters[6] = "4096"; // maxY
					* parameters[7] = "Data.csv"; // dataFiles
				    */
				   scroll.setVisible(false);
				   terminalArea.setVisible(false);
				   g.setColor(Color.WHITE);
				   g.fillRect(0, 0, f.getSize().width, f.getSize().height);
				   try {
				   Graph graph = new Graph(80,130,f.getSize().width-150,f.getSize().height-200,Double.parseDouble(Tabs.get(index).getParameter(3)),Double.parseDouble(Tabs.get(index).getParameter(4)),Double.parseDouble(Tabs.get(index).getParameter(5)),Double.parseDouble(Tabs.get(index).getParameter(6)));
				   graph.Name(Tabs.get(index).getParameter(0),Tabs.get(index).getParameter(1),Tabs.get(index).getParameter(2)); 
				   graph.GraphColor(new Color(13,100,65));
				   graph.BoxColor(new Color(52,156,112));
				   graph.g2D(g2d);
				   
				   
				   g.setColor(new Color(13,100,65));
				   g2d.drawString("Minimum X",(int) (f.getWidth()-minX.getSize().getWidth())-10, 145);
				   g2d.drawString("Minimum Y",(int) (f.getWidth()-minX.getSize().getWidth())-10, 195);
				   g2d.drawString("Maximum X",(int) (f.getWidth()-minX.getSize().getWidth())-10, 245);
				   g2d.drawString("Maximum Y",(int) (f.getWidth()-minX.getSize().getWidth())-10, 295);
				   int xxl = 0;
				   for (int i = 0; i < Tabs.size(); i++) {
					   try {
					   if (Tabs.size() > i && Tabs.get(i).getType().equals("csv")) {graph.g2D(convertDoubles(Tabs.get(i).getParameter(0),0), convertDoubles(Tabs.get(i).getParameter(0),1), graphColors[xxl], g2d); xxl++; }
					   
					   if (xxl == graphColors.length) xxl = 0;
					   }
					   catch (IndexOutOfBoundsException e) {}
				   }
				   g.setColor(Color.BLACK);
				   metrics = g.getFontMetrics(g.getFont());
				   g.drawString("Mouse Location",f.getSize().width-100-metrics.stringWidth("Mouse Location")/2,f.getSize().height-200-metrics.getHeight()/2);
				   g.drawString(graph.getLocation(mouseX,mouseY),f.getSize().width-100-metrics.stringWidth(graph.getLocation(mouseX,mouseY))/2,f.getSize().height-200+metrics.getHeight()/2);
				   } catch (NumberFormatException e) {
	        			System.out.println(e.getMessage());
	        		}
				   if (minX.getLocation().getY() +minX.getHeight() <= f.getHeight()-28-74-33) minX.setVisible(true); else minX.setVisible(false);
				   if (minY.getLocation().getY() +minY.getHeight() <= f.getHeight()-28-74-33) minY.setVisible(true); else minY.setVisible(false);
				   if (maxX.getLocation().getY() +maxX.getHeight() <= f.getHeight()-28-74-33) maxX.setVisible(true); else maxX.setVisible(false);
				   if (maxY.getLocation().getY() +maxY.getHeight() <= f.getHeight()-28-74-33) maxY.setVisible(true); else maxY.setVisible(false);
				  
				   Tabs.get(index).setParameter(3, minX.getText());
				   Tabs.get(index).setParameter(4, minY.getText());
				   Tabs.get(index).setParameter(5, maxX.getText());
				   Tabs.get(index).setParameter(6, maxY.getText());
				   }
			   else if (index != -1 && Tabs.get(index).getType().equals("σείς")) {
				   if (Tabs.get(index).initialized == false) textArea.setText(Tabs.get(index).getParameter(0)); Tabs.get(index).initialized = true;
				   if (!Tabs.get(index).getParameter(0).equals(textArea.getText())) Tabs.get(index).edited = true;
				   
				   scroll.setVisible(true);
				   minX.setVisible(false);
				   minY.setVisible(false);
				   maxX.setVisible(false);
				   maxY.setVisible(false);
				   terminalArea.setBackground(new Color(0,0,0));
				   terminalArea.setForeground(new Color(0,255,0));
			   }
			   else if (index != -1 && Tabs.get(index).getType().equals("csv")){
				   if (Tabs.get(index).initialized == false) textArea.setText(Tabs.get(index).getParameter(0)); Tabs.get(index).initialized = true;
				   if (!Tabs.get(index).getParameter(0).equals(textArea.getText())) Tabs.get(index).edited = true;
				   scroll.setVisible(true);
				   minX.setVisible(false);
				   minY.setVisible(false);
				   maxX.setVisible(false);
				   maxY.setVisible(false);
				   terminalArea.setVisible(false);
			   }
			   else {
				   if (Tabs.size() > 0) Tabs.get(0).setSelected(true);
				   scroll.setVisible(false);
				   terminalArea.setVisible(false);
				   minX.setVisible(false);
				   minY.setVisible(false);
				   maxX.setVisible(false);
				   maxY.setVisible(false);
				   g.setColor(Color.WHITE);
				   g.fillRect(0, 0, f.getSize().width, f.getSize().height);
			   }
			   
			   paint(g);
			   g.setColor(new Color(52,156,112));
			   g.fillRect(0,0,f.getWidth(),63+53);
			   g.setColor(new Color(13,100,65));
			   g.fillRect(0,20,f.getWidth(),63);
			   
			   //20pix for startbar
			   //32pix
			   //349pix space
			   g.setColor(new Color(52,156,112));
			   g.fillRect(0,f.getHeight()-28-74-33,f.getWidth(),33);
			   if (!terminalArea.isVisible()) {
			   g.setColor(new Color(0,0,0));
			   g.fillRect(0,f.getHeight()-28-74,f.getWidth(),74);
			   }
			   
			   g.setColor(new Color(13,100,65));
			   g.fillRect(0,f.getHeight()-28,f.getWidth(),20);
			   //g.fillRect(f.getWidth()-60, f.getHeight()-200,10,10);
			   //33 pix
			   //74 pix
			   //20 pix
			   //x.set(0, 0.0);
			   //y.set(0, 0.0);
			   //x.set(1, 0.0);
			   //y.set(1, 0.0);
			   Font stringFont = new Font( "SansSerif", Font.PLAIN, 12 );
			   g.setFont(stringFont);
			   metrics = g.getFontMetrics(g.getFont());
			   for (int i = 0; i < Tabs.size(); i++) {
			   drawTab(Tabs.get(i),i,g2d);
			   }
			   
			   g2d.drawImage(buttonImages[0], 13, 57,null);
			   g2d.drawImage(buttonImages[1], 13+23+4, 57,null);
			   g2d.drawImage(buttonImages[2], 13+23+23+4+11, 57,null);
			   g2d.drawImage(buttonImages[3], 13+23+23+23+11+4+4, 57,null);
			   g2d.drawImage(buttonImages[4], 13+23+23+23+23+4+11+4+4, 57,null);
			   g2d.drawImage(buttonImages[5], f.getWidth()-24-23, 57,null);
			   stringFont = new Font( "SansSerif", Font.PLAIN, 12 );
			   g.setFont(stringFont);
			   metrics = g.getFontMetrics(g.getFont());
			   g.setColor(Color.WHITE);
			   int localXtraText2width = metrics.stringWidth(XtraText2);
			   if (buttonImages[5].equals(imgs[5]) && XtraText2.length() > 7 && XtraText2.substring(0,8).equals("Graphing")) {
				    localXtraText2width = metrics.stringWidth("Graphing...");
				    if (dotcnt == 20)
					XtraText2 += ".";
					if (dotcnt == 40)
					XtraText2 += ".";
					if (dotcnt == 60)
					XtraText2 += ".";
					if (dotcnt == 80)
					XtraText2 = "Graphing..";
					if (dotcnt == 100)
					XtraText2 = "Graphing.";
					if (dotcnt == 120)
					{
					dotcnt = 0;
					XtraText2 = "Graphing";
					}
					
					dotcnt++;
				}
				   g2d.drawString(XtraText2, f.getWidth()-24-23-10-localXtraText2width, (int) ((metrics.getHeight()/4)+12+57));
				   g2d.drawString(XtraText, 13+23+23+23+23+4+11+4+4+10+23, (int) ((metrics.getHeight()/4)+12+57));
				   
			   //bufferGraphics[0] = g;
				   Container parent = scroll.getParent();
			        if (parent instanceof JComponent) {
			            ((JComponent)parent).revalidate();
			        }
			   //}
		} finally {
			// It is best to dispose() a Graphics object when done with it.
			//bufferGraphics = g;
			g.dispose();
		}
	 
		// Shows the contents of the backbuffer on the screen.
		bf.show();
	 
	        //Tell the System to do the Drawing now, otherwise it can take a few extra ms until 
	        //Drawing is done which looks very jerky
	        Toolkit.getDefaultToolkit().sync();	
	}
	private void start() {
        if (clockThread == null) {
            clockThread = new Thread(this, "Clock");
            clockThread.start();
        }
    }
	public void stop() {
        clockThread = null;
    }
	public void run() {
		Thread myThread = Thread.currentThread();
        while (clockThread == myThread) {
        	gameLoop();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
	}
}