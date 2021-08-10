/*
 * Created on Aug 12, 2004
 */
package edu.oa.curvature.utils;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author Helgi I. Ingolfsson, hii@cs.cornell.edu
 */
public class DoubleParam {

    //Why initialize these to null and not the private fields? 
    public JLabel frontLabel = null;
    public JFormattedTextField textField = null;
    public JLabel backLabel = null;

    private String name;
    private DecimalFormat format = null;
    private double defaultValue;
    private String defultOffValue;
    private double minValue;
    private double maxValue;

    public DoubleParam(String name, String front, String back, String toolTip, DecimalFormat format, double defaultValue, String defultOffValue, double minValue, double maxValue) {
        //The method is called DoubleParam, don't shadow variables so use this
        this.name = name;
        this.defaultValue = defaultValue;
        this.defultOffValue = defultOffValue;
        this.format = format;
        this.minValue = minValue;
        this.maxValue = maxValue;

        String htmlFront = Utils.setToHTML(front);
        String htmlBack = Utils.setToHTML(back);
        String htmlToolTip = Utils.setToHTML(name + ", " + toolTip);
        frontLabel = new JLabel(htmlFront);
        frontLabel.setToolTipText(htmlToolTip);
        textField = new JFormattedTextField(format);
        textField.setToolTipText(htmlToolTip);
        textField.setText(format.format(defaultValue));
        //Back label is probably the units 
        backLabel = new JLabel(htmlBack);
        backLabel.setToolTipText(htmlToolTip);
    }


    // The GridBagConstraints or GridLayout need to be changed to show two rows of parameters 

    public void addFieldsToPanel(JPanel panel, int yCord) {
        panel.add(frontLabel, new GridBagConstraints(0, yCord, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        panel.add(textField, new GridBagConstraints(1, yCord, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
        panel.add(backLabel, new GridBagConstraints(2, yCord, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 0, 0));
    }

    public void setValue(double value) throws Exception {
        checkValue(value);
        textField.setText(format.format(value));
    }

    public double getValue() throws Exception {
        double ret = Double.NaN;
        try {
            ret = Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
            throw new Exception("Value " + textField.getText() + " in param " + name + " is a none valid number");
        }
        checkValue(ret);
        return ret;
    }

    public void setDefault() {
        textField.setText(format.format(defaultValue));
    }
    
    public void setDefultOffValue() {
        textField.setText(defultOffValue);
    }

    public void checkValue(double value) throws Exception {
        if (value > maxValue) {
            throw new Exception("Value " + value + " is bigger than max value " + maxValue + "\nfor param " + name);
        } else if (value < minValue) { throw new Exception("Value " + value + " is smaller than min value " + minValue + "\nfor param " + name); }
    }

    public static DoubleParam createParamFromResources(String name) {
        String front = Resources.getString(name + Resources.X_LABEL);
        String back = Resources.getString(name + Resources.X_UNIT);
        String toolTip = Resources.getString(name + Resources.X_TOOLTIP);
        DecimalFormat format = new DecimalFormat(Resources.getString(name + Resources.X_FORMAT));
        double defaultValue = Resources.getDouble(name + Resources.X_DEFAULT);
        double minValue = Resources.getDouble(name + Resources.X_MIN);
        double maxValue = Resources.getDouble(name + Resources.X_MAX);
        String defaultOffValue = Resources.getOptionalString(name + Resources.X_OFFDEFAULT);
        return new DoubleParam(name, front, back, toolTip, format, defaultValue, defaultOffValue, minValue, maxValue);
    }

}
