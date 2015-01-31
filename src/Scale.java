
public class Scale {
	private double x0,x1,y0,y1,sx,sy,ssy,ssx;
	public Scale(double x0, double y0, double x1, double y1, double screenX, double screenY) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.sx = screenX;
		this.sy = screenY;
		ssy = 0;
		ssx = 0;
	}
	public Scale(double x0, double y0, double x1, double y1, double screenStartX, double screenStartY, double screenEndX, double screenEndY) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.ssx = screenStartX;
		this.ssy = screenStartY;
		this.sx = screenEndX;
		this.sy = screenEndY;
	}
	public double realX(double vx){
		return ((vx/(x1-x0))*(sx-ssx))+ssx - ((sx-ssx)*(Math.abs(x0)/(x1-x0)));
	}
	public double realY(double vy){
		return (-((vy/(y1-y0))*(sy-ssy))+sy + ((sy-ssy)*(Math.abs(y0)/(y1-y0))));
	}
	//((vx/(x1-x0))*(sx-ssx))
	//
	public double X(double vx){
		return -1*(x1-x0)*(ssx - ((sx-ssx)*(Math.abs(x0)/(x1-x0)))-vx)/(sx-ssx);
	}
	//-((vy/(y1-y0))*(sy-ssy))
	public double Y(double vy){
		return (y1-y0)*(sy + ((sy-ssy)*(Math.abs(y0)/(y1-y0)))-vy)/(sy-ssy);
	}
	public double getXmax() {
		return x1;
	}
	public double getXmin() {
		return x0;
	}
	public double getYmax() {
		return y1;
	}
	public double getYmin() {
		return y0;
	}
	public double getRealXmax() {
		return sx;
	}
	public double getRealXmin() {
		return ssx;
	}
	public double getRealYmax() {
		return sy;
	}
	public double getRealYmin() {
		return ssy;
	}
}
