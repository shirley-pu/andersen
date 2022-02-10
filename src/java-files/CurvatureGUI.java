/*
 * Originally created on Aug 13, 2004
 */
package edu.oa.curvature;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import edu.oa.curvature.resultViews.*;
import edu.oa.curvature.utils.*;

/**
 * Program that calculates and plots the energetics of inclusion-induced bilayer deformations.
 * 
 * Build on paper: Energetics of Inclusion-Induced Bilayer Deformation,
 *                 Claus Nielsen, Mark Goulian and Olaf S. Andersen
 *                
 * This program uses a properties file (curvature.properties) which has to be in the class path. 
 *                
 * This class contains all GUI elements and uses Curvature.java to calculate the energies 
 *                 
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu 
 */
public class CurvatureGUI {

	// Settings
	private static final int paramBoxPixelSize = 180;

	// Local varibales
	private DoubleParam d0Param = null;
	private DoubleParam lParam = null;
	private DoubleParam r0Param = null;
	private DoubleParam kaParam = null;
	private DoubleParam kcParam = null;
	private DoubleParam kgParam = null;
	private DoubleParam alphaParam = null;
	private DoubleParam rheadParam = null;
	private DoubleParam sParam = null;
	private DoubleParam c0Param = null;
	private DoubleParam energyParam = null;
	private JRadioButton sRelaxedButton = null;
	private JRadioButton sConstrainedButton = null;
	private JRadioButton sManualButton = null;
	private JCheckBox onOffCheckBox=null;
	private JCheckBox lipidBox=null;
	private PerturbationChartView perturbationChartView = new PerturbationChartView();
	private CoordinatesInfoView coordinatesInfoView = new CoordinatesInfoView();
	private EnergyChartView energyChartView = new EnergyChartView();
	private EnergyCoordView energyCoordView = new EnergyCoordView();
	private JPanel centerPanel = null;
	private JPanel titlePanel = null;

	//new parameters 
	private DoubleParam lParam_new=null;
	private DoubleParam energyParam_new=null;
	private DoubleParam energyParam_bilayer=null;

	private DoubleParam HbParam = null;
	private DoubleParam HxParam = null;
	private DoubleParam HcParam = null;

	public CurvatureGUI() {

		d0Param = DoubleParam.createParamFromResources("d0");
		lParam = DoubleParam.createParamFromResources("l");
		r0Param = DoubleParam.createParamFromResources("r0");
		kaParam = DoubleParam.createParamFromResources("Ka");
		kcParam = DoubleParam.createParamFromResources("Kc");
		kgParam = DoubleParam.createParamFromResources("Kg");
		alphaParam = DoubleParam.createParamFromResources("alpha");
		rheadParam = DoubleParam.createParamFromResources("Rhead");
		sParam = DoubleParam.createParamFromResources("s");
		c0Param = DoubleParam.createParamFromResources("C0");
		energyParam = DoubleParam.createParamFromResources("deltaGdef");
		
		//The difference between the parameters 
		energyParam_new=DoubleParam.createParamFromResources("deltaGdef_new");
		energyParam_bilayer=DoubleParam.createParamFromResources("deltaGbilayer");

		//The coefficients
		HbParam=DoubleParam.createParamFromResources("Hb");
		HxParam=DoubleParam.createParamFromResources("Hx");
		HcParam=DoubleParam.createParamFromResources("Hc");

		//The new l value 
		lParam_new = DoubleParam.createParamFromResources("lnew");
		
		// Title
		JLabel titleLabel = new JLabel(Utils.setToHTML(Resources.getString("Title")));
		titlePanel = new JPanel(new GridBagLayout());
		titlePanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(5, 11, 5, 10), 0, 0));

		// Parameters
		//Don't allow the calculated delta_Gdeformation to be editable
		energyParam.textField.setEditable(false);
		energyParam_new.textField.setEditable(false);
		energyParam_bilayer.textField.setEditable(false);
		HbParam.textField.setEditable(false);
		HxParam.textField.setEditable(false);
		HcParam.textField.setEditable(false);

		// Init the Radio buttons
		sRelaxedButton = new JRadioButton(Utils.setToHTML(Resources.getString("relaxedButton" + Resources.X_NAME)));
		sRelaxedButton.setToolTipText(Utils.setToHTML(Resources.getString("relaxedButton" + Resources.X_TOOLTIP)));
		sRelaxedButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				sParam.textField.setEditable(false);
				sParam.setDefultOffValue();
			}
		});
		sConstrainedButton = new JRadioButton(Utils.setToHTML(Resources.getString("constrainedButton" + Resources.X_NAME)));
		sConstrainedButton.setToolTipText(Utils.setToHTML(Resources.getString("constrainedButton" + Resources.X_TOOLTIP)));
		sConstrainedButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				sParam.textField.setEditable(false);
				sParam.setDefultOffValue();
			}
		});
		sManualButton = new JRadioButton(Utils.setToHTML(Resources.getString("manualButton" + Resources.X_NAME)));
		sManualButton.setToolTipText(Utils.setToHTML(Resources.getString("manualButton" + Resources.X_TOOLTIP)));
		sManualButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				sParam.textField.setEditable(true);
				// Set to default value if value is none valid  
				try {
					sParam.getValue();
				} catch (Exception e) {
					sParam.setDefault();
				}
			}
		});

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(sRelaxedButton);
		group.add(sConstrainedButton);
		group.add(sManualButton);

		// Make the Manual Default selected
		sManualButton.setSelected(true);

		int yCord = 0;
		JPanel innerParamPanel = new JPanel(new GridBagLayout());
		d0Param.addFieldsToPanel(innerParamPanel, yCord++);
		lParam.addFieldsToPanel(innerParamPanel, yCord++);
		lParam_new.addFieldsToPanel(innerParamPanel, yCord++);
		r0Param.addFieldsToPanel(innerParamPanel, yCord++);
		kaParam.addFieldsToPanel(innerParamPanel, yCord++);
		kcParam.addFieldsToPanel(innerParamPanel, yCord++);
		kgParam.addFieldsToPanel(innerParamPanel, yCord++);
		alphaParam.addFieldsToPanel(innerParamPanel, yCord++);
		rheadParam.addFieldsToPanel(innerParamPanel, yCord++);
		c0Param.addFieldsToPanel(innerParamPanel, yCord++);

		//checkbox for changing r0;
		String checkBoxToolTip = Resources.getString("r0box_tooltip");
		String checkBoxName = Resources.getString("r0box_name");
		onOffCheckBox = new JCheckBox(Utils.setToHTML(checkBoxName));
		onOffCheckBox.setToolTipText(Utils.setToHTML(checkBoxToolTip));
		onOffCheckBox.setSelected(false);

		//checkbox for changing to 22:1 lipid;
		String lipid_checkBoxToolTip=Resources.getString("lipidbox_tooltip");
		String lipid_checkBoxName=Resources.getString("lipidbox_name");
		lipidBox= new JCheckBox(Utils.setToHTML(lipid_checkBoxName));
		lipidBox.setToolTipText(Utils.setToHTML(lipid_checkBoxToolTip));
		lipidBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {d0Param.setValue(Resources.getDouble("d0_switch"));}
				catch(Exception e){}
				try{kaParam.setValue(Resources.getDouble("Ka_switch"));}
				catch(Exception e){}
				try{kcParam.setValue(Resources.getDouble("Kc_switch"));}
				catch(Exception e){}
			}
		});
		lipidBox.setSelected(false);

		// Add buttons and checkBox
		innerParamPanel.add(lipidBox, new GridBagConstraints(0, yCord++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		innerParamPanel.add(onOffCheckBox, new GridBagConstraints(0, yCord++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
		GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		innerParamPanel.add(sRelaxedButton, new GridBagConstraints(0, yCord++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		innerParamPanel.add(sConstrainedButton, new GridBagConstraints(0, yCord++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
		innerParamPanel.add(sManualButton, new GridBagConstraints(0, yCord++, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));

		sParam.addFieldsToPanel(innerParamPanel, yCord++);

		JButton defaultButton = new JButton(Resources.getString("Default" + Resources.X_LABEL));
		defaultButton.setMnemonic(defaultButton.getText().charAt(0));
		defaultButton.setToolTipText(Resources.getString("Default" + Resources.X_LABEL));
		defaultButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				setDefault();
			}
		});
		JButton calcButton = new JButton(Resources.getString("Calculate" + Resources.X_LABEL));
		calcButton.setMnemonic(calcButton.getText().charAt(0));
		calcButton.setToolTipText(Resources.getString("Calculate" + Resources.X_TOOLTIP));
		calcButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				calcCurvature();
			}
		});
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.add(defaultButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 0, 5), 0, 0));
		buttonPanel.add(calcButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 10, 5), 0, 0));

		JPanel paramPanel = new JPanel(new GridBagLayout());
		paramPanel.setBorder(BorderFactory.createTitledBorder(Resources.getString("param" + Resources.X_LABEL) + " "));
		paramPanel.add(innerParamPanel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		paramPanel.add(Box.createVerticalStrut(0), new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), paramBoxPixelSize, 0));
		paramPanel.add(buttonPanel, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		energyParam.addFieldsToPanel(paramPanel, 3);
		paramPanel.add(Box.createHorizontalStrut(0), new GridBagConstraints(3, 0, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		//New Gdef 
		energyParam_new.addFieldsToPanel(paramPanel,4);

		// Gbilayer
		energyParam_bilayer.addFieldsToPanel(paramPanel,5);
	
		// Coefficients
		HbParam.addFieldsToPanel(paramPanel,6);
		HxParam.addFieldsToPanel(paramPanel,7);
		HcParam.addFieldsToPanel(paramPanel,8);

		// TabbedPane 
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(perturbationChartView.getTitle(), null, perturbationChartView.getPanel(), perturbationChartView.getInfo());
		tabbedPane.addTab(coordinatesInfoView.getTitle(), null, coordinatesInfoView.getPanel(), coordinatesInfoView.getInfo());
		tabbedPane.addTab(energyChartView.getTitle(), null, energyChartView.getPanel(), energyChartView.getInfo());
		tabbedPane.addTab(energyCoordView.getTitle(), null, energyCoordView.getPanel(), energyCoordView.getInfo());

		// Main Panel
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.add(paramPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 10, 10, 0), 0, 0));
		centerPanel.add(tabbedPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 10, 10, 10), 0, 0));

		// Calculate Curvature for default values 
		calcCurvature();
	}

		// Calculate deltaG_bilayer (difference between both delta_Gdef)
			// Get the difference in curvature
			//Make a method? 
		public double deltaG_bilayer() throws Exception{
			double d0 = d0Param.getValue();
			double l = lParam.getValue();
			double l_new=lParam_new.getValue();
			double r0 = r0Param.getValue();
			double r0_new = Double.NaN;
			double ka = kaParam.getValue();
			double kc = kcParam.getValue();
			double kg = kgParam.getValue();
			// Note gamma here is alfa in the Energetics of Inclusion-Induced Bilayer Deformation paper
			double alpha = alphaParam.getValue();
			// Monolayer deformation at inclusion-bilayer boundary, Formula (5c) in paper:
			// Energerics of Inclusion-Induced Bileayer Deformation, Claus Nielsen, Mark Goulian and Olaf S. Andersen
			
			//two different u0;
			double u0=(d0-l)/2.0;
			double u0_new=(d0-l_new)/2.0;
			double rhead = rheadParam.getValue();
			double c0 = c0Param.getValue();
			double s = Double.NaN;
			double s_new=Double.NaN;

			if (this.onOffCheckBox.isSelected()) {
				r0_new = (Math.pow(r0,2.0) * l_new) / l;
			  } else {
				r0_new = r0Param.getValue();
			  } 
		
			if (sRelaxedButton.isSelected()) {
				// Use s = Smin, get c0 to calc Smin, numerically finds s_min
				// Looks in range [-2,2] with error tol of 1E-5
				s = CurvatureFMin.calculateSmin(d0, u0, r0, ka, kc, kg, alpha, c0);
				s_new= CurvatureFMin.calculateSmin(d0, u0_new, r0_new, ka, kc, kg, alpha, c0);	
			} else if (sConstrainedButton.isSelected()) {
				// Use s = R(Head) * C0
				s = rhead * c0;
				s_new=s;
			} else {
				// Use user specified s
				s = sParam.getValue();
				s_new=s;
			}
			sParam.setValue(s);

			Curvature c1= new Curvature(d0,u0,r0,ka,kc,kg,alpha,s,c0);
			Curvature c2=new Curvature(d0,u0_new,r0,ka,kc,kg,alpha,s_new,c0);
				
			double deltaG= c1.getDeltaGdef() - c2.getDeltaGdef();

			return deltaG; 

		}


	/**
	 * Called by Parent to properly add this GUI to container
	 * @param container
	 */
	public void addGUItoPanel(Container container) {
		container.add(titlePanel, BorderLayout.NORTH);
		container.add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Get values for all params and updates all views, if error 
	 * show in dialog and clear views. 
	 */
	public void calcCurvature() {
		try {
			// Get all param's
			double d0 = d0Param.getValue();
			double l = lParam.getValue();
			double r0 = r0Param.getValue();
			double ka = kaParam.getValue();
			double kc = kcParam.getValue();
			double kg = kgParam.getValue();
			// Note gamma here is alfa in the Energetics of Inclusion-Induced Bilayer Deformation paper
			double alpha = alphaParam.getValue();
			// Monolayer deformation at inclusion-bilayer boundary, Formula (5c) in paper:
			// Energerics of Inclusion-Induced Bileayer Deformation, Claus Nielsen, Mark Goulian and Olaf S. Andersen
			double u0 = (d0 - l) / 2.0;
			double rhead = rheadParam.getValue();
			double c0 = c0Param.getValue();
			double s = Double.NaN;

			//values for l2 
			double l_new=lParam_new.getValue();
			double u0_new=(d0-l_new)/2.0;
			double s_new=Double.NaN;
			double r0_new=Double.NaN;

			if (onOffCheckBox.isSelected()) {
				r0_new = (Math.pow(r0,2.0) * l_new) / l;
			  } else {
				r0_new = r0Param.getValue();
			  } 

			if (sRelaxedButton.isSelected()) {
				// Use s = Smin, get c0 to calc Smin, numerically finds s_min
				// Looks in range [-2,2] with error tol of 1E-5
				s = CurvatureFMin.calculateSmin(d0, u0, r0, ka, kc, kg, alpha, c0);
				s_new=CurvatureFMin.calculateSmin(d0,u0_new,r0,ka,kc,kg,alpha,c0);
			} else if (sConstrainedButton.isSelected()) {
				// Use s = R(Head) * C0
				s = rhead * c0;
				s_new=s;
			} else {
				// Use user specified s
				s = sParam.getValue();
				s_new=s;
			}
			sParam.setValue(s);

			Curvature curvature = new Curvature(d0, u0, r0, ka, kc, kg, alpha, s, c0);
			double deltaGdef_old = curvature.getDeltaGdef_old();
			double deltaGmec = curvature.getDeltaGmec();
			double deltaGgc = curvature.getDeltaGgc();
			double deltaGdef = curvature.getDeltaGdef();
			double monolayerWidth = d0 / 2.0;
			double inclusionHalfHeight = l / 2.0;
			double maxXvalue = Double.parseDouble(Resources.getString("XAxesEndValue"));
			double graphInterval = Double.parseDouble(Resources.getString("SamplingInterval"));
			double[][] gridValues = curvature.getUforRange(r0, maxXvalue, graphInterval);


			Curvature curvature_new = new Curvature(d0, u0_new, r0_new, ka, kc, kg, alpha, s_new, c0);
			double deltaGdef_old_new = curvature_new.getDeltaGdef_old();
			double deltaGmec_new = curvature_new.getDeltaGmec();
			double deltaGgc_new = curvature_new.getDeltaGgc();
			double deltaGdef_new = curvature_new.getDeltaGdef();
			double monolayerWidth_new = d0 / 2.0;
			double inclusionHalfHeight_new = l_new / 2.0;
			double maxXvalue_new = Double.parseDouble(Resources.getString("XAxesEndValue"));
			double graphInterval_new = Double.parseDouble(Resources.getString("SamplingInterval"));
			double[][] gridValues_new = curvature_new.getUforRange(r0_new, maxXvalue, graphInterval);

			// Curvature curvature_new= new Curvature(d0,u0,r0,ka,kc,kg,alpha,s,c0)
			// Except r0 should be different 
			// Want r0 to adjust when l is adjusted for volume constant 
			// double deltaGdef=curvature_new.getDeltaGdef();

			// Curvature simplified for Hb, Hx, Hc
			CurvatureSimplifiedTreatment curvatureSimpl = new CurvatureSimplifiedTreatment(d0, u0, r0, c0, (ka * 4), kc, s);
			// alfa is different (but not used as input?)
			HbParam.setValue(curvatureSimpl.getHb());
			HxParam.setValue(curvatureSimpl.getHx());
			HcParam.setValue(curvatureSimpl.getHc());

			// Update energy label
			energyParam.setValue(deltaGdef);

			//Calculate difference in deltaG
			energyParam_new.setValue(deltaGdef_new);

			double deltaG_change=deltaG_bilayer();
			energyParam_bilayer.setValue(deltaG_change);

			int clear= 1;
			int noclear= 0;

			// Update perturbation chart pane
			perturbationChartView.setChartData(monolayerWidth, inclusionHalfHeight, r0, gridValues,clear);
			perturbationChartView.setChartData(monolayerWidth_new,inclusionHalfHeight_new,r0_new,gridValues_new,noclear);
			// Update it with the new r0 and gridValues (getUforRange)
			// Have another variable called 'gridValues_new'? 
			// But we want to see the old deformation and the new deformation
			
			// Update energy pane
			double[][] ceValues = curvature.getDeltaGceforRange(r0, maxXvalue, graphInterval);
			double[][] sdValues = curvature.getDeltaGsdforRange(r0, maxXvalue, graphInterval);
			double[][] stValues = curvature.getDeltaGstforRange(r0, maxXvalue, graphInterval);
			double[][] mecValues = curvature.getDeltaGmecforRange(r0, maxXvalue, graphInterval);
			double[][] defValues = curvature.getDeltaGdefforRange(ceValues, sdValues, stValues, mecValues);
			energyChartView.setChartData(ceValues, sdValues, stValues, mecValues, defValues);

			// Update energy coord pane
			energyCoordView.setCoordData(ceValues, sdValues, stValues, mecValues, defValues);

			// Update coordinates and info pane
			double deltaGcei = Utils.integrate(ceValues, graphInterval);
			double deltaGsdi = Utils.integrate(sdValues, graphInterval);
			double deltaGsti = Utils.integrate(stValues, graphInterval);
			coordinatesInfoView.setInfoData(d0, u0, l, r0, c0, ka, kc, kg, alpha, s, deltaGdef, deltaGcei, deltaGsdi, deltaGsti, deltaGmec,
				deltaGgc);
			coordinatesInfoView.setCoordData(gridValues);

			// Print stuff (to command line)
			if (true) {
				System.out.println(" ************************************** ");

				/*
				// init simplified treatment
				// !!! Need to coorect for mono-layer/dual-layer compression (not in paper) ka *= 4;
				CurvatureSimplifiedTreatment curvatureSimpl = new CurvatureSimplifiedTreatment(d0, u0, r0, c0, (ka * 4), kc, s);
				System.out.println("Hb = " + curvatureSimpl.getHb());
				System.out.println("Hc = " + curvatureSimpl.getHc());
				System.out.println("Hx = " + curvatureSimpl.getHx());
				System.out.println("a1 (ns)= " + curvatureSimpl.getA1()); // no scaling ??
				System.out.println("a2 (ns)= " + curvatureSimpl.getA2()); // no scaling ??
				System.out.println("a3 (ns)= " + curvatureSimpl.getA3()); // no scaling ??
				*/

				double dGdefi = Utils.integrate(defValues, graphInterval);
				double dGmeci = Utils.integrate(mecValues, graphInterval);

				System.out.println("delta G(def)  = " + deltaGdef);
				//System.out.println("delta G(def)s = " + curvatureSimpl.getDeltaGdef()); // no ST no GC
				System.out.println("delta G(def)o = " + deltaGdef_old + ", no MEC and no GC");
				System.out.println("delta G(def)i = " + dGdefi + ", no GC");
				System.out.println("delta G(ce)i  = " + deltaGcei);
				System.out.println("delta G(sd)i  = " + deltaGsdi);
				System.out.println("delta G(st)i  = " + deltaGsti);
				System.out.println("delta G(mec)i = " + dGmeci);
				System.out.println("delta G(mec)  = " + deltaGmec);
				System.out.println("delta G(gc)   = " + deltaGgc);
				System.out.println("delta G(bilayer) = " + deltaG_change);
			}

		} catch (Exception e) {
			// Display error
			JOptionPane.showMessageDialog(centerPanel.getParent(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			//*CHANGE THIS TO ADD IN ENERGyPARAM_NEW TOO!*

			// Clear Data
			try {
				energyParam.setValue(0.0);
				//if (onOffCheckBox.isSelected()) {
				//	sParam.setDefultOffValue();
				//} 
			} catch (Exception ex) {
				// Do nothing
			}
			perturbationChartView.clearData();
			coordinatesInfoView.clearData();
			energyChartView.clearData();
			energyCoordView.clearData();

			// Print error to standard error (remove if in applet) 
			e.printStackTrace();
		}
	}

	/**
	 *  Set all params to default value
	 */
	public void setDefault() {
		d0Param.setDefault();
		lParam.setDefault();
		//
		lParam_new.setDefault();
		//
		r0Param.setDefault();
		kaParam.setDefault();
		kcParam.setDefault();
		kgParam.setDefault();
		alphaParam.setDefault();
		rheadParam.setDefault();
		sParam.setDefault();
		c0Param.setDefault();
		sManualButton.setSelected(true);
		//setCheckBoxSelection(false);
	}

}
