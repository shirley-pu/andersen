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
 * This class gives the coordinates of all the diffrent energy contributions
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class EnergyCoordView extends ResultView {

	private JTextArea curvatureTextArea = new JTextArea();
	private JScrollPane curvatureScrollPane = new JScrollPane(ceTextArea);
	private JPanel coordinatesPanel = new JPanel(new GridBagLayout());
	private DecimalFormat format = null;

	public CurvatureCoordView() {
		super("CurvatureCoords");
		format = new DecimalFormat(Resources.getString(panelName + Resources.X_COORDSFORMAT));

		curvatureTextArea.setEditable(false);
		curvatureTextArea.setToolTipText(Resources.getString(panelName + Resources.X_CURVTOOLTIP));
		curvatureScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		curvatureScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		curvatureScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_CURVNAME)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
		coordinatesPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		coordinatesPanel.add(curvatureScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Get View main panel
	 */
	public JPanel getPanel() {
		return coordinatesPanel;
	}

	public void setCoordData(double[][] curvValues) {
		String curvCoords = new String("");
		for (int i = 0; i < curvValues[0].length; i++) {
			if (i != 0) {
				curvCoords += "\n";
			}
			curvCoords += format.format(curvValues[0][i]) + "\t" + format.format(curvValues[1][i]);
		}
		curvatureTextArea.setText(curvCords);
		curvatureScrollPane.setPreferredSize(new Dimension(5, 5));
	}

	public void clearData() {
		curvatureTextArea.setText("");
	}

}
diff --git a/src/java-files/Curvature.java b/src/java-files/Curvature.java
