package edu.oa.curvature;

import fs.fed.us.optimization.Fmin;
import fs.fed.us.optimization.Fmin_methods;

public class CurvatureFMin implements Fmin_methods {

	private double d0;
	private double u0;
	private double r0;
	private double ka;
	private double kc;
	private double kg;
	private double c0;
	private double alfa;

	public CurvatureFMin(double d0, double u0, double r0, double ka, double kc, double kg, double alfa, double c0) {
		this.d0 = d0;
		this.u0 = u0;
		this.r0 = r0;
		this.ka = ka;
		this.kc = kc;
		this.kg = kg;
		this.alfa = alfa;
		this.c0 = c0;
	}

	public double f_to_minimize(double x) {
		double new_s = x;
		double ret = Double.NaN;
		try {
			Curvature curvature = new Curvature(d0, u0, r0, ka, kc, kg, alfa, new_s, c0);
			ret = curvature.getDeltaGdef();
		} catch (Exception e) {
			// No nothing, we report a NaN in ret
		}
		return ret;
	}

	/**
	 * Calculates Smin, numerically 
	 */
	public static double calculateSmin(double d0, double u0, double r0, double ka, double kc, double kg, double alfa, double c0) {
		CurvatureFMin curvatureFMin = new CurvatureFMin(d0, u0, r0, ka, kc, kg, alfa, c0);
		double smin = Fmin.fmin(-2, 2, curvatureFMin, 0.00001);
		return smin;
	}

}
