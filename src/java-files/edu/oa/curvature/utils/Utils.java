/*
 * Created on Aug 22, 2004
 */
package edu.oa.curvature.utils;

/**
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class Utils {

	public static String setToHTML(String string) {
		return "<html>" + string + "</html>";
	}

    /**
     * Integrate the function under the curve to data where data[0] is x-axes
     * and data[1] is y-axes. 
     * @param data 
     * @param interval same as data[1][i]-data[1][i+1] 
     * @return the area under the curve
     */
	public static double integrate(double[][] data, double interval) {
		double temp = 0.0;
		for (int i = 0; i < data[1].length; i++) {
			temp += data[1][i];
		}
		return temp * interval;
	}
	
}
