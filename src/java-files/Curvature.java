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
 * Built on paper: (#) Claus Nielsen, Mark Goulian and Olaf S. Andersen. (1998)
 *                     Energetics of Inclusion-Induced Bilayer Deformations.
 *                     Biophysical Journal, 74:1966-1983 
 *             and    
 *                 ($) Claus Nielsen and Olaf S. Andersen. (2000)
 *                     Inclusion-Induced Bilayer Deformations: Effects of Monolayer Equilibrium Curvature
 *                     Biophysical Journal, 79:2583-2604
 * We also use corrections to the (#) paper  (1999) Biophysical Journal, 76:2317, which has changes to 
 * a few of the formulas (ka = 4ka etc)
 * 
 * Definitions:
 *  GC  := Gaussian curvature
 *  MEC := Monolayer equilibrium curvature
 *  DEF := Deformation
 *  CE  := Compression-expansion
 *  SD  := Splay-distortion
 *  ST  := Surface tension
 * 
 * - This program uses a properties file (curvature.properties) which has to be in the class path. 
 * - This class takes care of the main calculations and calls the external Bessel function
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class Curvature {

    // Parameter to convert between units (not in paper)
    public static final double changeParams = 4.114;
    // If imag. part of complex result is higher then this threshold then report error
    private double imagErrorThreshold =  Resources.getDouble(Resources.IMAGERRORTHRESHOLD);

    private Complex kp; // k+
    private Complex kn; // k-
    private Complex kp2; // k+^2
    private Complex kn2; // k-^2
    private Complex Ap; // A+
    private Complex An; // A-
    private double deltaGdef_old;
    private double d0;
    private double u0;
    private double r0;
    private double ka;
    private double kc;
    private double kg;
    private double s;
    private double c0;
    private double alpha;
    private double gamma;
    private double beta;

    /**
     * Set up all relevant parameters for curvature calculations and 
     * calculate the deformation free energy.
     * @param d0
     * @param u0
     * @param r0
     * @param c0
     * @param ka
     * @param kc
     * @param kg
     * @param alpha
     * @param s	
     * @param c0
     * @throws Exception if Bessel function fails or free energy has a non zero imag. part
     */
    public Curvature(double d0, double u0, double r0, double ka, double kc, double kg, double alpha, double s, double c0) throws Exception {
        this.d0 = d0;
        this.u0 = u0;
        this.r0 = r0;
        this.ka = ka;
        this.kc = kc;
        this.kg = kg;
        this.alpha = alpha;
        this.s = s;
        this.c0 = c0;

        // Formula #(7)
        // Note gamma here is alpha in the Energetics of Inclusion-Induced Bilayer Deformation paper
        gamma = this.alpha / this.kc;
        beta = (4 * this.ka) / (this.d0 * this.d0 * this.kc); // Note correction ka = 4ka

        /*
         // Print params to standard out
         System.out.println("Values d0 = "+d0+", l = "+l+", r0 = "+r0+", c0 = "+c0+", ka = "+ka+", kc = "+kc
         +", alpha = "+alpha+", gamma = "+gamma+", beta = "+beta+", u0 = "+u0+", s = "+s+", c0 = "+c0);
         */

        // Calc k+ and k- , Formula #(9)
        Complex temp = new Complex((gamma * gamma) - (4 * beta), 0).sqrt();
        kp2 = new Complex(gamma, 0).add(temp).divide(2);
        kn2 = new Complex(gamma, 0).subtract(temp).divide(2);
        kp = kp2.sqrt();
        kn = kn2.sqrt();

        // Calc A+ and A- , Formulas #(12a) and #(12b)
        Complex[] Kkpr0 = BesselkJINI.getBesselK(kp.multiply(r0));
        Complex[] Kknr0 = BesselkJINI.getBesselK(kn.multiply(r0));
        Complex div = (kn.multiply(Kkpr0[0].multiply(Kknr0[1]))).subtract(kp.multiply(Kknr0[0].multiply(Kkpr0[1])));
        Ap = ((kn.multiply(Kknr0[1].multiply(u0))).add(Kknr0[0].multiply(this.s))).divide(div);
        An = ((kp.multiply(Kkpr0[1].multiply(-u0))).subtract(Kkpr0[0].multiply(this.s))).divide(div);

        // Calc deformation free energy from (#), using Formula #(13)
        // Node: if c0 is not 0 this is not the correct energy, that is it's missing the MEC term ($)
        //       Additionally this energy dose not contain the GC term.  
        temp = ((Ap.multiply(kp2).multiply(Kkpr0[0])).add(An.multiply(kn2).multiply(Kknr0[0]))).multiply(this.s);
        temp = temp.add(((Ap.multiply(kp.multiply(kp2)).multiply(Kkpr0[1])).add(An.multiply(kn.multiply(kn2)).multiply(Kknr0[1]))).multiply(u0));
        temp = temp.addReal(gamma * this.u0 * this.s);
        Complex res = temp.multiply(-Math.PI * this.r0 * this.kc);
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Error the free energy has a non zero imag. part of = " + res.imag()); }
        deltaGdef_old = res.real() / changeParams;
    }   

    /**
     * Calculate u(r)
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param r Radical distance from inclusion symmetry axis
     * @return Monolayer deformation at distance r from inclusion symmetry axis
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getU(double r) throws Exception {
        // Formula #(10)
        Complex[] kPosArray = BesselkJINI.getBesselK(kp.multiply(r));
        Complex[] kNegArray = BesselkJINI.getBesselK(kn.multiply(r));
        Complex res = (Ap.multiply(kPosArray[0])).add(An.multiply(kNegArray[0]));
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Warning u(" + r + ") has a non zero imag. part of = " + res.imag()); }
        return res.real();
    }

    /**
     * Calculate u'(r)
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param r Radical distance from inclusion symmetry axis
     * @return Monolayer deformation at distance r from inclusion symmetry axis
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getdU(double r) throws Exception {
        // Formula #(11)b
        Complex[] kPosArray = BesselkJINI.getBesselK(kp.multiply(r));
        Complex[] kNegArray = BesselkJINI.getBesselK(kn.multiply(r));
        Complex res = (Ap.multiply(kp).multiply(kPosArray[1])).add(An.multiply(kn).multiply(kNegArray[1]));
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Warning u(" + r + ") has a non zero imag. part of = " + res.imag()); }
        return -1 * res.real();
    }

    
    /**
     * Calculate u(r) for a range starting at "startR" and ending at "stopR" with interval "interval"
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param startR Start value
     * @param startR Stop value
     * @param interval Interval value
     * @return an array of [x, u(x)] values
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double[][] getUforRange(double startR, double stopR, double interval) throws Exception {
        if (startR > stopR) { throw new Exception("startR must be smaller than stopR"); }
        int size = (int) Math.floor((stopR - startR) / interval);
        double[][] retArray = new double[2][size];
        for (int i = 0; i < retArray[0].length; i++) {
            if (i == 0) {
                retArray[0][i] = startR;
            } else {
                retArray[0][i] = retArray[0][i - 1] + interval;
            }
            retArray[1][i] = getU(retArray[0][i]);
        }
        return retArray;
    }

    /**
     * Calculate deltaGce(r)
     * Ref paper: Energerics of Inclusion-Induced Bilayer Deformation,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param r Radical distance from inclusion symmetry axis
     * @return energy component corresponding to the compression-expansion
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getDeltaGce(double r) throws Exception {
        // Formula #(16a) and #(10)
    	double u = getU(r);      
        return (u * u * (Math.PI * r * 4 * ka) / (d0 * d0)) / changeParams;
    }

    /**
     * Calculate deltaGce(r) for a range starting at "startR" and ending at "stopR" with interval "interval"
     * Ref paper: Energerics of Inclusion-Induced Bilayer Deformation,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param startR Start value
     * @param startR Stop value
     * @param interval Interval value
     * @return an array of [x, deltaGce(x)] values
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double[][] getDeltaGceforRange(double startR, double stopR, double interval) throws Exception {
        if (startR > stopR) { throw new Exception("startR must be smaller than stopR"); }
        int size = (int) Math.floor((stopR - startR) / interval);
        double[][] retArray = new double[2][size];
        for (int i = 0; i < retArray[0].length; i++) {
            if (i == 0) {
                retArray[0][i] = startR;
            } else {
                retArray[0][i] = retArray[0][i - 1] + interval;
            }
            retArray[1][i] = getDeltaGce(retArray[0][i]);
        }
        return retArray;
    }

    /**
     * Calculate deltaGsd(r) ( bad aproximation )
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformation,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param r Radical distance from inclusion symmetry axis
     * @return energy component corresponding to the splay-distortion
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getDeltaGsd(double r) throws Exception {
        // Formula #(16b), #(8) and #(A13)
        Complex[] kPosArray = BesselkJINI.getBesselK(kp.multiply(r));
        Complex[] kNegArray = BesselkJINI.getBesselK(kn.multiply(r));
        Complex temp1 = kp2.multiply(Ap.multiply(kPosArray[0]));
        Complex temp2 = kn2.multiply(An.multiply(kNegArray[0]));
        Complex res = (temp1.add(temp2)).pow(2.0).multiply(Math.PI * r * kc);
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Warning deltaGsd(" + r + ") has a non zero imag. part of = " + res.imag()); }
        return res.real() / changeParams;
    }
    
    /**
     * Calculate deltaGsd(r) for a range starting at "startR" and ending at "stopR" with interval "interval"
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformation,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param startR Start value
     * @param startR Stop value
     * @param interval Interval value
     * @return an array of [x, deltaGsd(x)] values
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double[][] getDeltaGsdforRange(double startR, double stopR, double interval) throws Exception {
        if (startR > stopR) { throw new Exception("startR must be smaller than stopR"); }
        int size = (int) Math.floor((stopR - startR) / interval);
        double[][] retArray = new double[2][size];
        for (int i = 0; i < retArray[0].length; i++) {
            if (i == 0) {
                retArray[0][i] = startR;
            } else {
                retArray[0][i] = retArray[0][i - 1] + interval;
            }
            retArray[1][i] = getDeltaGsd(retArray[0][i]);
        }
        return retArray;
    }

    /**
     * Calculate deltaGst(r)
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param r Radical distance from inclusion symmetry axis
     * @return energy component corresponding to the surface tension
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getDeltaGst(double r) throws Exception {
        // Formula #(16c) and #(8)
        Complex[] kPosArray = BesselkJINI.getBesselK(kp.multiply(r));
        Complex[] kNegArray = BesselkJINI.getBesselK(kn.multiply(r));
        Complex temp1 = kp.multiply(Ap.multiply(kPosArray[1]));
        Complex temp2 = kn.multiply(An.multiply(kNegArray[1]));
        Complex res = (temp1.add(temp2)).pow(2.0).multiply(Math.PI * r * alpha);
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Warning deltaGst(" + r + ") has a non zero imag. part of = " + res.imag()); }
        return res.real() / changeParams;
    }

    /**
     * Calculate deltaGst(r) for a range starting at "startR" and ending at "stopR" with interval "interval"
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param startR Start value
     * @param startR Stop value
     * @param interval Interval value
     * @return an array of [x, deltaGst(x)] values
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double[][] getDeltaGstforRange(double startR, double stopR, double interval) throws Exception {
        if (startR > stopR) { throw new Exception("startR must be smaller than stopR"); }
        int size = (int) Math.floor((stopR - startR) / interval);
        double[][] retArray = new double[2][size];
        for (int i = 0; i < retArray[0].length; i++) {
            if (i == 0) {
                retArray[0][i] = startR;
            } else {
                retArray[0][i] = retArray[0][i - 1] + interval;
            }
            retArray[1][i] = getDeltaGst(retArray[0][i]);
        }
        return retArray;
    }

    /**
     * Calculate deltaGdef(r) as the sum of the partial energies ce, sd, st, and mec
     * Ref paper: Energetics of Inclusion-Induced Bilayer Deformations,
     *            Claus Nielsen, Mark Goulian and Olaf S. Andersen
     * @param ceValues deltaGce energies
     * @param sdValues deltaGsd energies
     * @param stValues deltaGst energies
     * @return an array of [x, deltaGdef(x)] values
     */
    public double[][] getDeltaGdefforRange(double[][] ceValues, double[][] sdValues, double[][] stValues, double[][] mecValues) {
        double[][] retArray = new double[2][ceValues[0].length];
        for (int i = 0; i < retArray[0].length; i++) {
            retArray[0][i] = ceValues[0][i];
            retArray[1][i] = ceValues[1][i] + sdValues[1][i] + stValues[1][i] + mecValues[1][i];
        }
        return retArray;
    }
    
    /**
     * Calculate deltaGmec(r) for a range starting at "startR" and ending at "stopR" with interval "interval"
     * @param startR Start value
     * @param startR Stop value
     * @param interval Interval value
     * @return an array of [x, deltaGmec(x)] values
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double[][] getDeltaGmecforRange(double startR, double stopR, double interval) throws Exception {
        if (startR > stopR) { throw new Exception("startR must be smaller than stopR"); }
        int size = (int) Math.floor((stopR - startR) / interval);
        double[][] retArray = new double[2][size];
        for (int i = 0; i < retArray[0].length; i++) {
            if (i == 0) {
                retArray[0][i] = startR;
            } else {
                retArray[0][i] = retArray[0][i - 1] + interval;
            }
            retArray[1][i] = getDeltaGmec(retArray[0][i]);
        }
        return retArray;
    }
    
    /**
     * Calculate deltaGmec(r)
     * @param r Radical distance from inclusion symmetry axis
     * @return energy component corresponding to the c0 dependent term of the deformation free energy
     * @throws Exception if Bessel function fails or result has a non zero imag part.
     */
    public double getDeltaGmec(double r) throws Exception {
        // Formula $(7) and #(A13)
        Complex[] kPosArray = BesselkJINI.getBesselK(kp.multiply(r));
        Complex[] kNegArray = BesselkJINI.getBesselK(kn.multiply(r));
        Complex temp1 = kp2.multiply(Ap.multiply(kPosArray[0]));
        Complex temp2 = kn2.multiply(An.multiply(kNegArray[0]));
        Complex res = (temp1.add(temp2)).multiply(-2 * Math.PI * kc * c0 * r);
        if (Math.abs(res.imag()) > imagErrorThreshold) { throw new Exception("Warning deltaGmec(" + r + ") has a non zero imag. part of = " + res.imag()); }
        return res.real() / changeParams;
    }

    /**
     * Calclate deltaG_MEC (Monolayer equilibrium curvature), formula $(7)
     * Get the c0 dependent term of the deformation free energy
     * @return Returns the deltaGmec.
     */
    public double getDeltaGmec() {
    	// Eq. $(7) 
		double deltaGmec = (2 *  Math.PI * kc * c0 * r0 * s) / changeParams; 
		return deltaGmec;
    }
    
    /**
     * Calclate deltaG_GC (Gaussian curvature), formula $(A1) 
     * @return Returns the deltaGgc.
     */
    public double getDeltaGgc() {
    	// Eq. $(A1), Note that kg is the Mean splay-distortion modulus
    	// and kg/kc is extimated to be less than 0.05 ($ see appendix)
		double deltaGgc = ((Math.PI / 2) * kg * (s * s / (1 + (s * s)))) / changeParams; 
		return deltaGgc;
    }
    
    /**
     * Get the deformation free energy
     * Node: this is only the CE, SD and ST parts (the one that depend on u(r))
     *       missing the MEC and GC
     * @return Returns the deltaGdef_old.
     */
    public double getDeltaGdef_old() {
    	// change
        return deltaGdef_old;
    }
    
    /**
     * Get the total deformation free energy
     * @return Returns the deltaGdef.
     */
    public double getDeltaGdef() {
        return getDeltaGdef_old() + getDeltaGmec() + getDeltaGgc();
    }

}
