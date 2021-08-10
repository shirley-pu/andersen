/*
 * Created on Jul 9, 2004
 */
package edu.oa.curvature.bessel;

import JSci.maths.Complex;

/**
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class AsymptoticExpansions {
	
	/**
	 * Calculates the modified Bessel function of the second kind, using 
	 * asymptotic Expansions for Large Arguments.
	 * Warning only works for large value of 'z' and |arg z| < 3/2pi 
	 * Ref: Handbook of Mathematical Functions ..., Ed. Milton Abramowitz
	 * and Irene A. Stegun, pages:377-378
	 * 
	 * @param z 	Value to evaluate function for
	 * @param v		Order of function
	 * @param err 	Error bound (MAX error of result) 
	 * @param maxIt	Max number of iterations, if reaches this number returns error
	 * @return
	 * @throws Exception
	 */
	public static Complex kv(Complex z, int v, double err, int maxIt) throws Exception {	
		double maxApro = (3.0 / 2.0) * Math.PI;
		if (Math.abs(z.arg()) >= maxApro) {
			throw new Exception("Argument |arg z| = "+Math.abs(z.arg())+" but should be less than "+maxApro);
		}	
		double mu = 4 * v * v; 
		Complex z8 = z.multiply(8);
		Complex sum = new Complex(1,0);
		Complex current = new Complex(mu - 1,0).divide(z8);
		System.out.println("current1 real = "+current.real()+" img = "+current.imag());
		sum = sum.add(current);
		int n = 2;
		while (Math.abs(current.real()) > err || Math.abs(current.imag()) > err) {
			int fact = (2 * n) - 1;
			fact *= fact;  // fact = (2n-1)^2
			current = current.multiply( (mu - fact) / n ).divide(z8);
			System.out.println("current"+n+" real = "+current.real()+" img = "+current.imag());
			sum = sum.add(current);
			if (n >= maxIt) {
				throw new Exception("MAX iteration reached and error still over bound.");
			}
			n++;
		}
		Complex factor = new Complex(Math.PI,0);
		factor = factor.divide(z.multiply(2));
		factor = factor.sqrt();
		factor = factor.multiply(Complex.exp(z.multiply(-1.0)));
		System.out.println("factor real = "+factor.real()+" img = "+factor.imag());
		return factor.multiply(sum);
	}
	
}
