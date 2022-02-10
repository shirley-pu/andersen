/*
 * Created on Jul 9, 2004
 */
package edu.oa.curvature;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * Wraps the Curvature program in a Frame
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class RunFrame {
	
	public static void main(String[] args) {
		
		// This is needed as the "curvature.properties" file is in US format.
	    Locale.setDefault(Locale.US);
		
		JFrame frame = new JFrame("Curvature");
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    CurvatureGUI curvatureGUI = new CurvatureGUI();
	    curvatureGUI.addGUItoPanel(frame.getContentPane());
	    frame.pack();
		frame.setVisible(true);
	}
	
}
