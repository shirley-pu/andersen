/*
 * Created on Aug 18, 2004
 */
package edu.oa.curvature.resultViews;

import javax.swing.JPanel;

import edu.oa.curvature.utils.Resources;

/**
 * Program that calculates and plots the energetics of inclusion-induced bilayer deformations.
 * 
 * Build on paper: Energerics of Inclusion-Induced Bileayer Deformation,
 *                 Claus Nielsen, Mark Goulian and Olaf S. Andersen
 *                
 * This program uses a properties file (curvature.properties) which has to be in class path. 
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public abstract class ResultView {

	protected String panelName = null;
	protected String title = null;
	protected String info = null;

	public ResultView(String panelName) {
		this.panelName = panelName;
		this.title = Resources.getString(panelName + Resources.X_LABEL);
		this.info = Resources.getString(panelName + Resources.X_TOOLTIP);
	}

	/**
	 * Get View main panel
	 */
	public abstract JPanel getPanel();

	/**
	 * Get view title
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get view name
	 * @return the name
	 */
	public String getInfo() {
		return info;
	}

}
