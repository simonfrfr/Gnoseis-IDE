import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;


public class Graph {
	Scale scale;
	private boolean ticks = true;
	private int tick = 4;
	private int tickSize = 20;
	private String Name = "";
	private String unitX = "";
	private String unitY = "";
	private Color graphColor = Color.BLUE;
	private Color boxColor = Color.BLACK;
	private Color tickColor = Color.BLACK;
	private Color textColor = Color.BLACK;
	private Color negativeColor = Color.RED;
	private boolean NegColor = true;
	public Graph(double startX, double startY, double endX, double endY, double minX, double minY, double maxX, double maxY) {
		scale = new Scale(minX,minY,maxX,maxY,startX,startY,endX,endY);
	}
	public Graph(double endX, double endY, double minX, double minY, double maxX, double maxY) {
		scale = new Scale(minX,minY,maxX,maxY,0,0,endX,endY);
	}
	public void GraphColor(Color k) {
		graphColor = k;
	}
	public void BoxColor(Color k) {
		boxColor = k;
	}
	public void TickColor(Color k) {
		tickColor = k;
	}
	public void TextColor(Color k) {
		textColor = k;
	}
	public void NegativeColor(Color k) {
		negativeColor = k;
	}
	public void NegativeColor(boolean k) {
		NegColor = k;
	}
	public void ticks(boolean k) {
		ticks = k;
	}
	public void ticks(int k) {
		tick = k;
	}
	public void setTickSize(int k) {
		tickSize = k;
	}
	public JPanel g2D(Graphics2D g) {
		Color old = g.getColor();
		JPanel graph = new JPanel();
		g.setColor(textColor);
		Font stringFont = new Font( "SansSerif", Font.PLAIN, 18 );
		g.setFont(stringFont);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		AffineTransform at = new AffineTransform();
		int txt0 = metrics.stringWidth(Name);
        at.setToRotation(Math.toRadians(90), (float) scale.getRealXmax()+(tickSize/4)+(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))-(txt0/2));
        g.setTransform(at);
        g.drawString(Name, (float) scale.getRealXmax()+(tickSize/2)+(metrics.getHeight()/4), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))-(txt0/2));
        at = new AffineTransform();
        at.setToRotation(Math.toRadians(270), (float) scale.getRealXmin()-(tickSize/2)-(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))+(txt0/2));
        g.setTransform(at);
        txt0 = metrics.stringWidth(unitY);
        g.drawString(unitY, (float) scale.getRealXmin()-(tickSize/2)-(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))+(txt0/2));
        at = new AffineTransform();
        at.setToRotation(0, 0, 0);
        g.setTransform(at);
        txt0 = metrics.stringWidth(unitX);
        g.drawString(unitX, (float) ((float) ((float) scale.getRealXmin())+((scale.getRealXmax()-scale.getRealXmin())/2))-(txt0/2), (float) scale.getRealYmax()+(tickSize)+(metrics.getHeight()));
        stringFont = new Font( "SansSerif", Font.PLAIN, 10 );
        g.setFont(stringFont);
		metrics = g.getFontMetrics(g.getFont());
		g.setColor(boxColor);
		g.draw(new Rectangle2D.Double(scale.getRealXmin(),scale.getRealYmin(),scale.getRealXmax()-scale.getRealXmin(),scale.getRealYmax()-scale.getRealYmin()));
		if (ticks == true) {
			for (int i = 0; i <= tick; i++){
				g.setColor(tickColor);
				metrics = g.getFontMetrics(g.getFont());
				double xLocUpper = scale.getRealXmin()+(((double)i/tick)*(scale.getRealXmax()-scale.getRealXmin()));
				int txt1 = metrics.stringWidth(String.format("%.2f", scale.X(xLocUpper)));
				g.draw(new Line2D.Double(xLocUpper,scale.getRealYmax()+(tickSize/2),xLocUpper,scale.getRealYmax()));
				//System.out.println(scale.Y(300));
				double yLocUpper = scale.getRealYmin()+(((double)i/tick)*(scale.getRealYmax()-scale.getRealYmin()));
				int txt2 = metrics.stringWidth(String.format("%.2f", scale.Y(yLocUpper)));
				g.draw(new Line2D.Double(scale.getRealXmin(),yLocUpper,scale.getRealXmin()-(tickSize/2),yLocUpper));
				g.setColor(textColor);
				if (scale.Y(yLocUpper) < 0 && NegColor == true) {
					g.setColor(negativeColor);
				}
				g.drawString(String.format("%.2f", scale.Y(yLocUpper)), (float) scale.getRealXmin()-(tickSize/2)-txt2-1, (float) yLocUpper + (metrics.getHeight()/4));
				g.setColor(textColor);
				if (scale.X(xLocUpper) < 0 && NegColor == true) {
					g.setColor(negativeColor);
				}
				g.drawString(String.format("%.2f", scale.X(xLocUpper)), (float) xLocUpper - (txt1/2), (float) scale.getRealYmax()+(tickSize/2)+(metrics.getHeight())-1);
			}
		}
		g.setColor(old);
		return graph;
	}
	public JPanel g2D(double[] x, double[] y, Graphics2D g) {
		Color old = g.getColor();
		JPanel graph = new JPanel();
		g.setColor(textColor);
		Font stringFont = new Font( "SansSerif", Font.PLAIN, 18 );
		g.setFont(stringFont);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		AffineTransform at = new AffineTransform();
		int txt0 = metrics.stringWidth(Name);
        at.setToRotation(Math.toRadians(90), (float) scale.getRealXmax()+(tickSize/4)+(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))-(txt0/2));
        g.setTransform(at);
        g.drawString(Name, (float) scale.getRealXmax()+(tickSize/2)+(metrics.getHeight()/4), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))-(txt0/2));
        at = new AffineTransform();
        at.setToRotation(Math.toRadians(270), (float) scale.getRealXmin()-(tickSize/2)-(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))+(txt0/2));
        g.setTransform(at);
        txt0 = metrics.stringWidth(unitY);
        g.drawString(unitY, (float) scale.getRealXmin()-(tickSize/2)-(metrics.getHeight()/2), (float) ((float) ((float) scale.getRealYmin())+((scale.getRealYmax()-scale.getRealYmin())/2))+(txt0/2));
        at = new AffineTransform();
        at.setToRotation(0, 0, 0);
        g.setTransform(at);
        txt0 = metrics.stringWidth(unitX);
        g.drawString(unitX, (float) ((float) ((float) scale.getRealXmin())+((scale.getRealXmax()-scale.getRealXmin())/2))-(txt0/2), (float) scale.getRealYmax()+(tickSize)+(metrics.getHeight()));
        stringFont = new Font( "SansSerif", Font.PLAIN, 10 );
        g.setFont(stringFont);
		metrics = g.getFontMetrics(g.getFont());
		g.setColor(boxColor);
		g.draw(new Rectangle2D.Double(scale.getRealXmin(),scale.getRealYmin(),scale.getRealXmax()-scale.getRealXmin(),scale.getRealYmax()-scale.getRealYmin()));
		if (ticks == true) {
			for (int i = 0; i <= tick; i++){
				g.setColor(tickColor);
				metrics = g.getFontMetrics(g.getFont());
				double xLocUpper = scale.getRealXmin()+(((double)i/tick)*(scale.getRealXmax()-scale.getRealXmin()));
				int txt1 = metrics.stringWidth(String.format("%.2f", scale.X(xLocUpper)));
				g.draw(new Line2D.Double(xLocUpper,scale.getRealYmax()+(tickSize/2),xLocUpper,scale.getRealYmax()));
				
				double yLocUpper = scale.getRealYmin()+(((double)i/tick)*(scale.getRealYmax()-scale.getRealYmin()));
				int txt2 = metrics.stringWidth(String.format("%.2f", scale.Y(yLocUpper)));
				g.draw(new Line2D.Double(scale.getRealXmin(),yLocUpper,scale.getRealXmin()-(tickSize/2),yLocUpper));
				g.setColor(textColor);
				if (scale.Y(yLocUpper) < 0 && NegColor == true) {
					g.setColor(negativeColor);
				}
				g.drawString(String.format("%.2f", scale.Y(yLocUpper)), (float) scale.getRealXmin()-(tickSize/2)-txt2-1, (float) yLocUpper + (metrics.getHeight()/4));
				g.setColor(textColor);
				if (scale.X(xLocUpper) < 0 && NegColor == true) {
					g.setColor(negativeColor);
				}
				g.drawString(String.format("%.2f", scale.X(xLocUpper)), (float) xLocUpper - (txt1/2), (float) scale.getRealYmax()+(tickSize/2)+(metrics.getHeight())-1);
			}
		}
		if (x.length == y.length){
			g.setColor(graphColor);
			for (int i = 1; i < x.length; i++) {
				if (scale.getXmax() >= x[i] && scale.getXmin() <= x[i] && scale.getYmax() >= y[i] && scale.getYmin() <= y[i] && scale.getXmax() >= x[i-1] && scale.getXmin() <= x[i-1] && scale.getYmax() >= y[i-1] && scale.getYmin() <= y[i-1]) {
				g.draw(new Line2D.Double(scale.realX(x[i]),scale.realY(y[i]),scale.realX(x[i-1]),scale.realY(y[i-1])));
				}
			}
		}
		g.setColor(old);
		return graph;
	}
	public void g2D(double[] x, double[] y, Color c, Graphics2D g) {
		Color tempC = g.getColor();
		if (x.length == y.length){
			g.setColor(c);
			for (int i = 1; i < x.length; i++) {
				if (scale.getXmax() >= x[i] && scale.getXmin() <= x[i] && scale.getYmax() >= y[i] && scale.getYmin() <= y[i] && scale.getXmax() >= x[i-1] && scale.getXmin() <= x[i-1] && scale.getYmax() >= y[i-1] && scale.getYmin() <= y[i-1]) {
				g.draw(new Line2D.Double(scale.realX(x[i]),scale.realY(y[i]),scale.realX(x[i-1]),scale.realY(y[i-1])));
				}
			}
		}
		g.setColor(tempC);
	}
	public String getLocation(int locX, int locY) {
		String loc;
		if (scale.getRealXmax() >= locX && scale.getRealXmin() <= locX && scale.getRealYmax() >= locY && scale.getRealYmin() <= locY) {
			loc = String.format("%.2f", scale.X(locX)) + "," + String.format("%.2f", scale.Y(locY));
		}
		else {
			loc = "";
		}
		return loc;
	}
	public void Name(String Name, String unitX, String unitY) {
		this.Name = Name;
		this.unitX = unitX;
		this.unitY = unitY;
	}
	
}
