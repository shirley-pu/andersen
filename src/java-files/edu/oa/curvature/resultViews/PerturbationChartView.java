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
 * This class plots the perturbation deformations to a chart.
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class PerturbationChartView extends ResultView {

    private ChartPanel chartPanel = null;
    private XYSeriesCollection collection = null;
    private JFreeChart chart = null;
    private String perturbationName = null;
    private String monolayerName = null;
    private String inclusionName = null;
    private double xAxesMin = 0.0;
    private double xAxesMax = 1.0;
    private double yAxesMin = 0.0;
    private double yAxesMax = 1.0;

    // Change these axes
    // Let it change based on the values of the paramters themselves?

    public PerturbationChartView() {
        super("PertChart");

        collection = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(Resources.getString(panelName + Resources.X_CHARTNAME),
											   Resources.getString(panelName + Resources.X_XAXESUNIT),
											   Resources.getString(panelName + Resources.X_YAXESUNIT), 
											   collection, 
											   PlotOrientation.VERTICAL, 
											   true, 
											   true, 
											   false);
        chart.setBackgroundPaint((new JPanel()).getBackground());
        chart.getLegend().setAnchor(Legend.EAST);
        chartPanel = new ChartPanel(chart);
        
        perturbationName = Resources.getString(panelName + Resources.X_PERTURBATIONNAME);
        monolayerName = Resources.getString(panelName + Resources.X_MONOLAYERNAME);
        inclusionName = Resources.getString(panelName + Resources.X_INCLUSIONNAME);
        xAxesMin = Double.parseDouble(Resources.getString(panelName + Resources.X_XAXESMIN));
        xAxesMax = Double.parseDouble(Resources.getString(panelName + Resources.X_XAXESMAX));
        yAxesMin = Double.parseDouble(Resources.getString(panelName + Resources.X_YAXESMIN));
        yAxesMax = Double.parseDouble(Resources.getString(panelName + Resources.X_YAXESMAX));
    }

//adjust the legend to separate l1 and l2 perturbation

    /**
     * Get View main panel
     */
    public JPanel getPanel() {
        return chartPanel;
    }

    /**
     * Sets data into chart
     * @param monolayerWidth
     * @param inclusionHalfHeight
     * @param r0
     * @param maxXvalue
     * @param gridValues
     * @param clear
     */

    public void setChartData(double monolayerWidth, double inclusionHalfHeight, double r0, double[][] gridValues, int clear) {
        // Remove series when method is used for length 1 
        if (clear == 1){
            collection.removeAllSeries();
        }

        if (clear==0){
            perturbationName = "Perturbation 2";
            inclusionName = "Inclusion 2";
        }
        
        XYSeries perturbationSeries = new XYSeries(perturbationName, false, true);
        for (int i = 0; i < gridValues[0].length; i++) {
            perturbationSeries.add(gridValues[0][i], monolayerWidth - gridValues[1][i]);
        }
        XYSeries monolayerSeries = new XYSeries(monolayerName, false, true);
        monolayerSeries.add(xAxesMin, monolayerWidth);
        monolayerSeries.add(xAxesMax, monolayerWidth);
        XYSeries inclusionSeries = new XYSeries(inclusionName, false, true);
        inclusionSeries.add(xAxesMin, inclusionHalfHeight);
        inclusionSeries.add(r0, inclusionHalfHeight);
        inclusionSeries.add(r0, 0);
        
        collection.addSeries(perturbationSeries);
        collection.addSeries(monolayerSeries);
        collection.addSeries(inclusionSeries);

        chart.getXYPlot().getRangeAxis().setRange(yAxesMin, yAxesMax);
        chart.getXYPlot().getDomainAxis().setRange(xAxesMin, xAxesMax);
    }

    public void clearData() {
        collection.removeAllSeries();
    }
    
}
