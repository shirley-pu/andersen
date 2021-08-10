/*
 * Created on Aug 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.oa.curvature.resultViews;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.Legend;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.XYSeries;
import org.jfree.data.XYSeriesCollection;

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
 * This class plots on a chart all the diffrent energy contributions
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class EnergyChartView extends ResultView {

	private ChartPanel chartPanel = null;
	private XYSeriesCollection collection = null;
	private JFreeChart chart = null;
	private String deltaGceName = null;
	private String deltaGsdName = null;
	private String deltaGstName = null;
	private String deltaGmecName = null;
	private String deltaGdefName = null;
	private double xAxesMin;
	private double xAxesMax;

	public EnergyChartView() {
		super("EneChart");

		collection = new XYSeriesCollection();

		chart = ChartFactory.createXYLineChart(Resources.getString(panelName + Resources.X_CHARTNAME), Resources.getString(panelName
				+ Resources.X_XAXESUNIT), Resources.getString(panelName + Resources.X_YAXESUNIT), collection, PlotOrientation.VERTICAL,
			true, true, false);
		chart.setBackgroundPaint((new JPanel()).getBackground());
		chart.getLegend().setAnchor(Legend.EAST);
		chartPanel = new ChartPanel(chart);

		deltaGceName = Resources.getString(panelName + Resources.X_DELTAGCENAME);
		deltaGsdName = Resources.getString(panelName + Resources.X_DELTAGSDNAME);
		deltaGstName = Resources.getString(panelName + Resources.X_DELTAGSTNAME);
		deltaGmecName = Resources.getString(panelName + Resources.X_DELTAGMECNAME);
		deltaGdefName = Resources.getString(panelName + Resources.X_DELTAGDEFNAME);
		xAxesMin = Double.parseDouble(Resources.getString(panelName + Resources.X_XAXESMIN));
		xAxesMax = Double.parseDouble(Resources.getString(panelName + Resources.X_XAXESMAX));
	}

	/**
	 * Get View main panel
	 */
	public JPanel getPanel() {
		return chartPanel;
	}

	/**
	 * Set data into chart.
	 * @param ceValues
	 * @param sdValues
	 * @param stValues
	 * @param defValues
	 */
	public void setChartData(double[][] ceValues, double[][] sdValues, double[][] stValues, double[][] mecValues, double[][] defValues) {

		XYSeries deltaGceSeries = new XYSeries(deltaGceName, false, true);
		for (int i = 0; i < ceValues[0].length; i++) {
			deltaGceSeries.add(ceValues[0][i], ceValues[1][i]);
		}
		XYSeries deltaGsdSeries = new XYSeries(deltaGsdName, false, true);
		for (int i = 0; i < sdValues[0].length; i++) {
			deltaGsdSeries.add(sdValues[0][i], sdValues[1][i]);
		}
		XYSeries deltaGstSeries = new XYSeries(deltaGstName, false, true);
		for (int i = 0; i < stValues[0].length; i++) {
			deltaGstSeries.add(stValues[0][i], stValues[1][i]);
		}
		XYSeries deltaGmecSeries = new XYSeries(deltaGmecName, false, true);
		for (int i = 0; i < mecValues[0].length; i++) {
			deltaGmecSeries.add(mecValues[0][i], mecValues[1][i]);
		}
		XYSeries deltaGdefSeries = new XYSeries(deltaGdefName, false, true);
		for (int i = 0; i < defValues[0].length; i++) {
			deltaGdefSeries.add(defValues[0][i], defValues[1][i]);
		}

		collection.removeAllSeries();
		collection.addSeries(deltaGceSeries);
		collection.addSeries(deltaGsdSeries);
		collection.addSeries(deltaGstSeries);
		collection.addSeries(deltaGmecSeries);
		collection.addSeries(deltaGdefSeries);

		//chart.getXYPlot().getRangeAxis().setRange(0.0, 20.0);
		chart.getXYPlot().getDomainAxis().setRange(xAxesMin, xAxesMax);
	}

	public void clearData() {
		collection.removeAllSeries();
	}

}
