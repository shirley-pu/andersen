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
 * This class plots the petrubation deformations to a chart.
 * 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class PetrubationChartView extends ResultView {

    private ChartPanel chartPanel = null;
    private XYSeriesCollection collection = null;
    private JFreeChart chart = null;
    private String pertrubationName = null;
    private String monolayerName = null;
    private String inclusionName = null;
    private double xAxesMin = 0.0;
    private double xAxesMax = 1.0;
    private double yAxesMin = 0.0;
    private double yAxesMax = 1.0;

    // Change these axes
    // Let it change based on the values of the paramters themselves?

    public PetrubationChartView() {
        super("PetChart");

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
        
        pertrubationName = Resources.getString(panelName + Resources.X_PERTRUBATIONNAME);
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
     * @param monolayerWith
     * @param inclusionHalfHeight
     * @param r0
     * @param maxXvalue
     * @param gridValues
     * @param clear
     */

     //Use a new XYSeries for the second perturbation
     // XY Series perturbationSeries_new = new XYSeries(perturbationName, false, true);
     // Can we use the same pertrubationName?
     // pertrubationSeries_new.add(gridValues_new[0][i],monolayerWidth-gridValues[1][i])

    public void setChartData(double monolayerWith, double inclusionHalfHeight, double r0, double[][] gridValues, int clear) {
        XYSeries pertrubationSeries = new XYSeries(pertrubationName, false, true);
        for (int i = 0; i < gridValues[0].length; i++) {
            pertrubationSeries.add(gridValues[0][i], monolayerWith - gridValues[1][i]);
        }
        XYSeries monolayerSeries = new XYSeries(monolayerName, false, true);
        monolayerSeries.add(xAxesMin, monolayerWith);
        monolayerSeries.add(xAxesMax, monolayerWith);
        XYSeries inclusionSeries = new XYSeries(inclusionName, false, true);
        inclusionSeries.add(xAxesMin, inclusionHalfHeight);
        inclusionSeries.add(r0, inclusionHalfHeight);
        inclusionSeries.add(r0, 0);

        if (clear == 1){
            collection.removeAllSeries();
        }
        
        collection.addSeries(pertrubationSeries);
        collection.addSeries(monolayerSeries);
        collection.addSeries(inclusionSeries);

        chart.getXYPlot().getRangeAxis().setRange(yAxesMin, yAxesMax);
        chart.getXYPlot().getDomainAxis().setRange(xAxesMin, xAxesMax);
    }

    public void clearData() {
        collection.removeAllSeries();
    }
    
}
