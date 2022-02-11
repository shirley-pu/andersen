/*
 * Created on Jul 20, 2004
 * Last modified on Feb 10, 2022
 */
package edu.oa.curvature;

import edu.oa.curvature.bessel.BesselkJINI;
import edu.oa.curvature.utils.Resources;
import JSci.maths.Complex;

/**
 * Program that calculates and plots the energetics of inclusion-induced bilayer deformations.
 * 
 * Build on paper: (*) Energetics of Inclusion-Induced Bilayer Deformations,
 *                     Claus Nielsen, Mark Goulian and Olaf S. Andersen
 *             and
 *                 (#) Simplified treatment of the effects of curvature (CN & OSA)     
 *                
 * This program uses a properties file (curvature.properties) which has to be in class path. 
 * 
 * This class takes care of the main calculations and calls the external Bessel function
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 * @author Shirley Pu, shp4017@med.cornell.edu
 */
public class CurvatureSimplifiedTreatment {

    // Parameter to convert between units (not in paper)
    private static final double changeParams = 4.11E-11;
    // If imag. part of complex result is higher then this threshold then report error
    private double imagErrorThreshold =  Resources.getDouble(Resources.IMAGERRORTHRESHOLD);

    private double deltaGdef;
    private double d0;
    private double u0;
    private double r0;
    private double c0;
    private double ka;
    private double kc;
    private double s;
    private double alpha; // Warning not the same alpha as in Nielsen, Goulian and Andersen
    private double landa; 
    private Complex a1;
    private Complex a2;
    private Complex a3;
    
    /**
     * Set up all relevant parameters for curvature calculations and 
     * calculate the deformation free energy.
     * Ref paper: * Energetics of Inclusion-Induced Bilayer Deformations,
     *              Claus Nielsen, Mark Goulian and Olaf S. Andersen
     *            # Simplified treatment of the effects of curvature (CN & OSA)
     *            $ Inclusion-Induced Bilayer Deformations: Effects of Monolayer Equilibrium Curvature 
     *              Clause Nielsen and Olaf S. Andersen
     * @param d0
     * @param u0
     * @param r0
     * @param c0
     * @param ka
     * @param kc
     * @param s  A value of Double.NaN, means calculate smin 
     * @throws Exception if Bessel function fails or free energy has a non zero imag. part
     */
    public CurvatureSimplifiedTreatment(double d0, double u0, double r0, double c0, double ka, double kc, double s) throws Exception {
        this.d0 = d0;
        this.u0 = u0;
        this.r0 = r0;
        this.c0 = c0;
        this.ka = ka;
        this.kc = kc;
        this.s = s;
    	
    	// Formula (4)#
        landa = Math.pow(( (this.d0 * this.d0 * this.kc) / this.ka), (1.0 / 4.0));

        // Formula (5)#, Warning here alpha is alpha in # not in *
        alpha = 2 * Math.PI * this.r0 * this.kc;

        // Define temp vars
        Complex temp1 = null;
        Complex temp2 = null;
        
        // Calculate f1, f2 and f3, Formula (25)#, (26)# and (27)#
        double r0landa = this.r0 / landa;
        double multFact = 2 * Math.PI * r0landa;  
        Complex ipsqrt = Complex.I.sqrt();
        Complex insqrt = Complex.I.multiply(-1).sqrt();
        Complex argp = ipsqrt.multiply(this.r0).divide(landa);
        Complex argn = insqrt.multiply(this.r0).divide(landa);
        Complex[] Kargp = BesselkJINI.getBesselK(argp);
		Complex[] Kargn = BesselkJINI.getBesselK(argn);
		
		temp1 = Kargp[0].multiply(Kargn[1]);
		temp2 = Kargn[0].multiply(Kargp[1]).multiply(Complex.I);
		Complex div = temp1.subtract(temp2);
		Complex f1 = (insqrt.multiply(Kargp[1]).multiply(Kargn[1])).divide(div);
		f1 = f1.multiply(multFact * r0landa * r0landa);
		temp1 = Kargn[0].multiply(Kargp[1]);
		temp2 = Kargp[0].multiply(Kargn[1]).multiply(Complex.I);
		Complex f2 = (temp1.subtract(temp2)).divide(div);
		f2 = f2.multiply(multFact * r0landa);
		Complex f3 = (insqrt.multiply(Kargp[0]).multiply(Kargn[0])).divide(div);
		f3 = f3.multiply(multFact);
		
		// Calculate a1, a2 and a3, Formula (3)#
		a1 = f1.multiply(this.kc / (this.r0 * this.r0));
		a2 = f2.multiply(this.kc / this.r0);
		a3 = f3.multiply(this.kc);
		
		// use Smin. Calculate Smin from Formula (6)#
		if (Double.isNaN(s)) {
			this.s = calcSmin();
		}
    }

    private double calcSmin() throws Exception {
        // Define temp vars
    	Complex temp1 = null;
        Complex temp2 = null;
    	
        // Calculate Smin, Formula (6)#
    	temp1 = a2.multiply(u0).addReal(alpha * c0);
		temp2 = a3.multiply(-2.0);
		Complex comSmin = temp1.divide(temp2);
		if (Math.abs(comSmin.imag()) > imagErrorThreshold) {
			throw new Exception("Warning Smin has a non zero imag. part of = " + comSmin.imag());
		}
		return comSmin.real();
    }
    
    /**
     * Get the deformation free energy
     * @return Returns the deltaGdef.
     */
    public double getDeltaGdef() throws Exception {
    	// Define temp vars
        Complex temp1 = null;
        Complex temp2 = null;
        Complex temp3 = null;
    	
		// Calculate deltaGdef using given s, Formula (2)# and (15)$
		temp1 = a1.multiply(u0 * u0);
		temp2 = a2.multiply(s * u0);
		temp3 = a3.multiply(s * s);
	
		Complex res = temp1.add(temp2).add(temp3).addReal(alpha * s * c0);
		if (Math.abs(res.imag()) > imagErrorThreshold) {
			throw new Exception("Warning the free energy has a non zero imag. part of = " + res.imag());
		}
		deltaGdef = res.real() / changeParams;	
	    return deltaGdef;
    }

    /**
     * Get the c0 dependent term of the deformation free energy
     * @return Returns the deltaGmec.
     */
    public double getDeltaGmec() {
    	// Calculate the Gmec from eq. (7)$
		double deltaGmec = 2 *  Math.PI * kc * c0 * r0 * s / changeParams; 
		return deltaGmec;
    }
    
    /**
     * Get Hb
     * @return Returns the varHb.
     */
    public double getHb() throws Exception {
    	// Define temp vars
        Complex temp1 = null;
        Complex temp2 = null;
        
        // Calculate the Hb constants from eq. (17)$
        temp1 = a2.multiply(a2); 				// a2^2
        temp2 = temp1.divide(a3.multiply(4)); 	// a2^2 / (4 * a3)
        temp1 = a1.subtract(temp2); 			// a1 - a2^2 / (4 * a3)
        
        if (Math.abs(temp1.imag()) > imagErrorThreshold) {
			throw new Exception("Warning Hb has a non zero imag. part of = " + temp1.imag());
		}
        double varHb = temp1.real() / changeParams;
		return varHb;
    }
    
    /**
     * Get Hx
     * @return Returns the varHx.
     */
    public double getHx() throws Exception {
    	// Define temp vars
        Complex temp1 = null;
        Complex temp2 = null;
        
    	// Calculate the Hx constants from eq. (17)$
        temp1 = a2.multiply(Math.PI * kc * r0 * -1);  	// - a2 * pi * Kc * r0
        temp2 = temp1.divide(a3); 						// - a2 * pi * Kc * r0 / a3
 		
        if (Math.abs(temp2.imag()) > imagErrorThreshold) {
			throw new Exception("Warning Hx has a non zero imag. part of = " + temp2.imag());
		}
        double varHx = temp2.real() / changeParams;
		return varHx;
    }
    
    /**
     * Get Hc
     * @return Returns the varHc.
     */
    public double getHc() throws Exception {
    	// Define temp vars
        Complex temp1 = null;
        Complex temp2 = null;
        
    	// Calculate the Hc constants from eq. (17)$
        temp1 = new Complex(Math.pow((Math.PI * kc * r0),2.0) * -1, 0.0);  // - (pi * Kc * r0)^2 
        temp2 = temp1.divide(a3); 									 	   // - (pi * Kc * r0)^2 / a3
 	
        if (Math.abs(temp2.imag()) > imagErrorThreshold) {
			throw new Exception("Warning Hc has a non zero imag. part of = " + temp2.imag());
		}
        double varHc = temp2.real() / changeParams;
		return varHc;
    }
    
    /**
     * Get a1
     * @return Returns the a1 real part.
     */
    public double getA1() throws Exception {
         if (Math.abs(a1.imag()) > imagErrorThreshold) {
			throw new Exception("Warning a1 has a non zero imag. part of = " + a1.imag());
		}
		return a1.real();
	}
    
    /**
     * Get a2
     * @return Returns the a2 real part.
     */
    public double getA2() throws Exception {
         if (Math.abs(a2.imag()) > imagErrorThreshold) {
			throw new Exception("Warning a2 has a non zero imag. part of = " + a2.imag());
		}
		return a2.real();
	}
    
    /**
     * Get a3
     * @return Returns the a3 real part.
     */
    public double getA3() throws Exception {
         if (Math.abs(a3.imag()) > imagErrorThreshold) {
			throw new Exception("Warning a3 has a non zero imag. part of = " + a3.imag());
		}
		return a3.real();
	}
    
    /**
     * Get S 
     * @return Returns the previously calculated Smin
     */
    public double getS() {
        return s;
    }

}
