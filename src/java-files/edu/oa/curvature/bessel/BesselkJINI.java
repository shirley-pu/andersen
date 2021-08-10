/*
 * Created on Jul 30, 2004
 */
package edu.oa.curvature.bessel;

import JSci.maths.Complex;

/**
 * This class is a wrapper to call a k-bessel function that is implemented
 * by AMOS, DONALD E., SANDIA NATIONAL LABORATORIES in Fortran. 
 * 
 * The class class uses java native methods to call a nother wrapper class 
 * implemented in C that then calls the Fortran function.
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class BesselkJINI {

	/**
	 * Calles K-Bessel function, Modified Bessel function of the second kind,
	 * Bessel function of the third kind.
	 * 
	 * This function calls C function "BesselkJINIimp.c" through JNI interface.
	 * the C function calls Fortran function "besselk.f" through C exter comand. 
	 * then the Fortran function calls "cbesk.f" function after Amos, Donald E. 
	 * with FNU = 0, KODE = 1, N = 2 and Z = (zreal, zimg). Which computes the 
	 * modified Bessel function of the second kind for order 0 and 1. 
	 *  
	 * From "besk.f"    (ret of [4])
	 *   IERR  - ERROR FLAG
	 *              IERR=0, NORMAL RETURN - COMPUTATION COMPLETED
	 *              IERR=1, INPUT ERROR   - NO COMPUTATION
	 *              IERR=2, OVERFLOW      - NO COMPUTATION, FNU+N-1 IS
	 *                      TOO LARGE OR CABS(Z) IS TOO SMALL OR BOTH
	 *              IERR=3, CABS(Z) OR FNU+N-1 LARGE - COMPUTATION DONE
	 *                      BUT LOSSES OF SIGNIFCANCE BY ARGUMENT
	 *                      REDUCTION PRODUCE LESS THAN HALF OF MACHINE
	 *                      ACCURACY
	 *              IERR=4, CABS(Z) OR FNU+N-1 TOO LARGE - NO COMPUTA-
	 *                      TION BECAUSE OF COMPLETE LOSSES OF SIGNIFI-
	 *                      CANCE BY ARGUMENT REDUCTION
	 *              IERR=5, ERROR              - NO COMPUTATION,
	 *                      ALGORITHM TERMINATION CONDITION NOT MET
	 *
	 * @param zreal Real part of complex input
	 * @param zimg Imaginari part of complex input
	 * @return double array = {real order 0, img order 0, real order 1, img order 1, error}
	 */
	public native double[] besselk(double zreal, double zimg);

	static {
		/*
		 * Our library is in a file called "besselk.dll"
		 */
		System.loadLibrary("besselk");
	}

	/**
	 * Wrapper for native method besselk
	 * 
	 * @param z imput
	 * @return complex[0] is besselk(z) of order 0, and complex[1] is besselk(z) of order 1
	 * @throws Exception if native method returns a none 0 error tag
	 */
	public static Complex[] getBesselK(Complex z) throws Exception {
		BesselkJINI besselk = new BesselkJINI();
		double[] array = besselk.besselk(z.real(), z.imag());
		if (array[4] != 0.0d) {
			throw new Exception("Error while calculatin BesselK for " + z + ", error code " + array[4]);
		}
		Complex[] retArray = new Complex[2];
		retArray[0] = new Complex(array[0], array[1]);
		retArray[1] = new Complex(array[2], array[3]);
		return retArray;
	}

	/**
	 * To Test function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BesselkJINI besselk = new BesselkJINI();
		double[] array = besselk.besselk(5.0f, 6.0f);
		System.out.println("Java:");
		System.out.println("  0 real:  " + array[0]);
		System.out.println("  0 img :  " + array[1]);
		System.out.println("  1 real:  " + array[2]);
		System.out.println("  1 img :  " + array[3]);
		System.out.println("  error :  " + array[4]);
	}

}
