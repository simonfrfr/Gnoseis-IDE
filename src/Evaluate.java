import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

public class Evaluate {
	 public static double findAreaBewtweenCurves(double f1xValues[], double f1yValues[], double f2xValues[], double f2yValues[]) 
	 {
		 double f3xValues[];
		 double f3yValues[];
		 double minX = f1xValues[0];
		 double maxX = f1xValues[0];
		 String x1intercept = "";
		 String x2intercept = "";
		 String miden = ""; 
		 String combinedx = "";
		 String combinedy = "";
		 for (int i = 0; i < f1xValues.length; i++){
			 combinedy += (f1yValues[i] - PolynomialFunctionLagrangeForm.evaluate(f2xValues, f1yValues, f1xValues[i])) + ",";
			 combinedx += f1xValues[i] + ",";
		 }
		 for (int i = 0; i < f1xValues.length; i++) {
			 if (f1xValues[i] > maxX) maxX = f1xValues[i];
			 if (f1xValues[i] > minX) minX = f1xValues[i];
		 }
		 for (int i = 0; i < f2xValues.length; i++) {
			 if (f2xValues[i] > maxX) maxX = f1xValues[i];
			 if (f2xValues[i] > minX) minX = f1xValues[i];
		 }
		 for (double x = minX; x < maxX; x += 0.01){
			 if ((Math.round(PolynomialFunctionLagrangeForm.evaluate(f1xValues, f1yValues, x) * 1000)/1000)-(Math.round(PolynomialFunctionLagrangeForm.evaluate(f1xValues, f1yValues, x) * 1000)/1000) == 0){
				 miden += x +",";
			 }
			 if ((Math.round(PolynomialFunctionLagrangeForm.evaluate(f1xValues, f1yValues, x) * 1000)/1000) == 0){
				 x1intercept += x +",";
			 }
			 if ((Math.round(PolynomialFunctionLagrangeForm.evaluate(f2xValues, f1yValues, x) * 1000)/1000) == 0){
				 x2intercept += x +",";
			 }
		 }
		 String[] zeros = miden.split(",");
		 double[] mideniko = new double[zeros.length];
		 for (int i = 0; i<zeros.length; i++) mideniko[i] = Double.valueOf(zeros[i]);
		 
		 String[] comx = combinedx.split(",");
		 f3xValues = new double[comx.length];
		 for (int i = 0; i<comx.length; i++) f3xValues[i] = Double.valueOf(comx[i]);
		 
		 String[] comy = combinedy.split(",");
		 f3yValues = new double[zeros.length];
		 for (int i = 0; i<comy.length; i++) f3yValues[i] = Double.valueOf(comy[i]);
		 
		 String[] x1int = x1intercept.split(",");
		 double[] x1intercepts = new double[x1int.length];
		 for (int i = 0; i<x1int.length; i++) x1intercepts[i] = Double.valueOf(x1int[i]);
		 
		 String[] x2int = x2intercept.split(",");
		 double[] x2intercepts = new double[x2int.length];
		 for (int i = 0; i<x2int.length; i++) x2intercepts[i] = Double.valueOf(x2int[i]);
		 double ektasi = 0;
		 //TODO LETS HOPE IT WORKS!
		 if (mideniko.length >= 2) {
			 for (int j = 1; j < mideniko.length;j++){
				 double prevX = mideniko[j-1];
				 for (int i = 0; i < x2intercepts.length;i++){
					 if (x2intercepts[i] < mideniko[j] && x2intercepts[i] > mideniko[j-1]) {
						 ektasi += Math.abs(olokliroma(f3xValues,f3yValues,prevX,x2intercepts[i],1000));
						 prevX = x2intercepts[i];
					 }
				 }
				 ektasi += Math.abs(olokliroma(f3xValues,f3yValues,prevX,mideniko[j],1000));
			 }
		 }
		 
		 return ektasi;
	 }
//	 static getFunction(){
	//	 PolynomialFunctionLagrangeForm.
	 //}
	 static double olokliroma(double x[], double y[], double a, double b, int N) { //Simpsons rule
		      double h = (b - a) / (N - 1);     // step size
		 
		      // 1/3 terms
		      double sum = 1.0 / 3.0 * (PolynomialFunctionLagrangeForm.evaluate(x,y,a) + PolynomialFunctionLagrangeForm.evaluate(x,y,b));

		      // 4/3 terms
		      for (int i = 1; i < N - 1; i += 2) {
		         double x2 = a + h * i;
		         sum += 4.0 / 3.0 * PolynomialFunctionLagrangeForm.evaluate(x,y,x2);
		      }

		      // 2/3 terms
		      for (int i = 2; i < N - 1; i += 2) {
		         double x2 = a + h * i;
		         sum += 2.0 / 3.0 * PolynomialFunctionLagrangeForm.evaluate(x,y,x2);
		      }

		      return sum * h;
		   }
}
