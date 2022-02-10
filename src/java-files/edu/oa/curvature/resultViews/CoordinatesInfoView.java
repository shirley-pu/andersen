/*
 * Created on Aug 18, 2004
 */
package edu.oa.curvature.resultViews;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;

import edu.oa.curvature.utils.Resources;

/**
 * Program that calculates and plots the energetics of inclusion-induced bilayer deformations.
 * 
 * Build on paper: Energerics of Inclusion-Induced Bileayer Deformation,
 *                 Claus Nielsen, Mark Goulian and Olaf S. Andersen
 *                
 * This program uses a properties file (curvature.properties) which has to be in class path. 
 * 
 * 
 * This class gives general information (displays all variables) and shows all the perturbation coordinates
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class CoordinatesInfoView extends ResultView {

	private JTextArea infoTextArea = new JTextArea();
	private JTextArea coordTextArea = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(coordTextArea);
	private JPanel corrdinatesPanel = new JPanel(new BorderLayout());
	private DecimalFormat format = null;
	private DecimalFormat coordsFormat = null;

	public CoordinatesInfoView() {
		super("PertCoords");
		format = new DecimalFormat(Resources.getString(panelName + Resources.X_FORMAT));
		coordsFormat = new DecimalFormat(Resources.getString(panelName + Resources.X_COORDSFORMAT));
		int infoBoxPixelSize = Integer.parseInt(Resources.getString(panelName + Resources.X_INFOBOXSIZE));

		infoTextArea.setEditable(false);
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBorder(BorderFactory.createTitledBorder(Resources.getString(panelName + Resources.X_INFOPANELLABEL) + " "));
		infoPanel.add(infoTextArea, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 5), 0, infoBoxPixelSize));

		coordTextArea.setEditable(false);
		coordTextArea.setToolTipText(Resources.getString(panelName + Resources.X_COORDPANELTOOLTIP));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_COORDPANELLABEL)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));

		corrdinatesPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		corrdinatesPanel.add(infoPanel, BorderLayout.NORTH);
		corrdinatesPanel.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Get View main panel
	 */
	public JPanel getPanel() {
		return corrdinatesPanel;
	}

	public void setInfoData(double d0, double u0, double l, double r0, double c0, double ka, double kc, double kg, 
			double alfa, double s, double deltaGdef, double deltaGcei, double deltaGsdi, double deltaGsti, double deltaGmec, double deltaGgc) {
		String infoString = new String("");
	
		infoString += "d0 = " + format.format(d0);
		infoString += ", u0 = " + format.format(u0);
		infoString += ", l = " + format.format(l);
		infoString += ", r0 = " + format.format(r0);
		infoString += ", c0 = " + format.format(c0);
		infoString += ",\nka = " + format.format(ka);
		infoString += ", kc = " + format.format(kc);
		infoString += ", kg = " + format.format(kg);
		infoString += ", alfa = " + format.format(alfa);
		infoString += ", s = " + format.format(s);
		infoString += "\nEnergy: Total deformation free energy = " + format.format(deltaGdef);
		infoString += "\nCE  = " + format.format(deltaGcei);
		infoString += ", SD  = " + format.format(deltaGsdi);
		infoString += ", ST  = " + format.format(deltaGsti);
		infoString += ", MEC = " + format.format(deltaGmec);
		infoString += ", GC  = " + format.format(deltaGgc);
		infoTextArea.setText(infoString);

		//Add new info if a second inclusion exists 
	}

	
	// Add gridValues_new to this view 
	//
	public void setCoordData(double[][] gridValues) {
		String cords = new String("");
		for (int i = 0; i < gridValues[0].length; i++) {
			if (i != 0) {
				cords += "\n";
			}
			cords += coordsFormat.format(gridValues[0][i]) + "\t" + coordsFormat.format(gridValues[1][i]);
		}
		coordTextArea.setText(cords);
		scrollPane.setPreferredSize(new Dimension(5, 5));
	}

	public void clearData() {
		coordTextArea.setText("");
		infoTextArea.setText("");
	}

}
