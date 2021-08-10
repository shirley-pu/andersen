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
public class EnergyCoordVeiw extends ResultView {

	private JTextArea ceTextArea = new JTextArea();
	private JTextArea sdTextArea = new JTextArea();
	private JTextArea stTextArea = new JTextArea();
	private JTextArea mecTextArea = new JTextArea();
	private JScrollPane ceScrollPane = new JScrollPane(ceTextArea);
	private JScrollPane sdScrollPane = new JScrollPane(sdTextArea);
	private JScrollPane stScrollPane = new JScrollPane(stTextArea);
	private JScrollPane mecScrollPane = new JScrollPane(mecTextArea);
	private JPanel corrdinatesPanel = new JPanel(new GridBagLayout());
	private DecimalFormat format = null;

	public EnergyCoordVeiw() {
		super("EneCoords");
		format = new DecimalFormat(Resources.getString(panelName + Resources.X_COORDSFORMAT));

		ceTextArea.setEditable(false);
		sdTextArea.setEditable(false);
		stTextArea.setEditable(false);
		mecTextArea.setEditable(false);
		ceTextArea.setToolTipText(Resources.getString(panelName + Resources.X_DELTAGCETOOLTIP));
		sdTextArea.setToolTipText(Resources.getString(panelName + Resources.X_DELTAGSDTOOLTIP));
		stTextArea.setToolTipText(Resources.getString(panelName + Resources.X_DELTAGSTTOOLTIP));
		mecTextArea.setToolTipText(Resources.getString(panelName + Resources.X_DELTAGMECTOOLTIP));
		ceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ceScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_DELTAGCENAME)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
		sdScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sdScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sdScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_DELTAGSDNAME)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
		stScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		stScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		stScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_DELTAGSTNAME)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));
		mecScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mecScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mecScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Resources.getString(panelName
				+ Resources.X_DELTAGMECNAME)
				+ " "), BorderFactory.createEmptyBorder(0, 5, 5, 5)));

		corrdinatesPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		corrdinatesPanel.add(ceScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		corrdinatesPanel.add(sdScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		corrdinatesPanel.add(stScrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		corrdinatesPanel.add(mecScrollPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Get View main panel
	 */
	public JPanel getPanel() {
		return corrdinatesPanel;
	}

	public void setCoordData(double[][] ceValues, double[][] sdValues, double[][] stValues, double[][] mecValues, double[][] defValues) {
		String ceCords = new String("");
		String sdCords = new String("");
		String stCords = new String("");
		String mecCords = new String("");
		String defCords = new String("");
		for (int i = 0; i < ceValues[0].length; i++) {
			if (i != 0) {
				ceCords += "\n";
				sdCords += "\n";
				stCords += "\n";
				mecCords += "\n";
				defCords += "\n";
			}
			ceCords += format.format(ceValues[0][i]) + "\t" + format.format(ceValues[1][i]);
			sdCords += format.format(sdValues[0][i]) + "\t" + format.format(sdValues[1][i]);
			stCords += format.format(stValues[0][i]) + "\t" + format.format(stValues[1][i]);
			mecCords += format.format(mecValues[0][i]) + "\t" + format.format(mecValues[1][i]);
			defCords += format.format(defValues[0][i]) + "\t" + format.format(defValues[1][i]);
		}
		ceTextArea.setText(ceCords);
		sdTextArea.setText(sdCords);
		stTextArea.setText(stCords);
		mecTextArea.setText(mecCords);
		ceScrollPane.setPreferredSize(new Dimension(5, 5));
		sdScrollPane.setPreferredSize(new Dimension(5, 5));
		stScrollPane.setPreferredSize(new Dimension(5, 5));
		mecScrollPane.setPreferredSize(new Dimension(5, 5));
	}

	public void clearData() {
		ceTextArea.setText("");
		sdTextArea.setText("");
		stTextArea.setText("");
		mecTextArea.setText("");
	}

}
