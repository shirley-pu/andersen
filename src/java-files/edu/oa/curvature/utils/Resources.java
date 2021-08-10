/*
 * Created on Aug 20, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.oa.curvature.utils;

import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author Helgi I. Ingolfsson
 */
public class Resources {

	public static final String IMAGERRORTHRESHOLD = "imagErrorThreshold";
	
    public static final String X_LABEL = "_label";
    public static final String X_UNIT = "_unit";
    public static final String X_TOOLTIP = "_tooltip";
    public static final String X_FORMAT = "_format";
    public static final String X_DEFAULT = "_default";
    public static final String X_OFFDEFAULT = "_offdefault";
    public static final String X_MIN = "_min";
    public static final String X_MAX = "_max";
    public static final String X_NAME = "_name";
	public static final String X_CHARTNAME = "_chartName";
	public static final String X_XAXESUNIT = "_xAxesUnit";
	public static final String X_XAXESMIN = "_xAxesMin";
	public static final String X_XAXESMAX = "_xAxesMax";
	public static final String X_YAXESUNIT = "_yAxesUnit";
	public static final String X_YAXESMIN = "_yAxesMin";
	public static final String X_YAXESMAX = "_yAxesMax";
	public static final String X_COORDSFORMAT = "_coordsFormat";
	public static final String X_INFOBOXSIZE = "_infoBoxSize";
	public static final String X_DELTAGCENAME = "_deltaGceName";
	public static final String X_DELTAGSDNAME = "_deltaGsdName";
	public static final String X_DELTAGSTNAME = "_deltaGstName";
	public static final String X_DELTAGMECNAME = "_deltaGmecName";
	public static final String X_DELTAGDEFNAME = "_deltaGdefName";
	public static final String X_COORDPANELLABEL = "_coordPanelLable";
	public static final String X_COORDPANELTOOLTIP = "_coordPanelToolTip";
	public static final String X_INFOPANELLABEL = "_infoPanelLable";
	public static final String X_DELTAGCETOOLTIP = "_deltaGceToolTip";
	public static final String X_DELTAGSDTOOLTIP = "_deltaGsdToolTip";
	public static final String X_DELTAGSTTOOLTIP = "_deltaGstToolTip";
	public static final String X_DELTAGDEFTOOLTIP = "_deltaGdefToolTip";
	public static final String X_DELTAGMECTOOLTIP = "_deltaGmecToolTip";
	public static final String X_PERTRUBATIONNAME = "_pertrubationName";
	public static final String X_MONOLAYERNAME = "_monolayerName";
	public static final String X_INCLUSIONNAME = "_inclusionName";
	
    private static final String propertiesFileName = "curvature.properties";
    private static Properties properties = null;
    
    static {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(propertiesFileName));
        } catch (Exception e) {
            System.out.println("Error, cant load properties file "+propertiesFileName);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static String getOptionalString(String key) {    
        return properties.getProperty(key);
    }
    
    public static String getString(String key) {
        String ret = properties.getProperty(key);
        if (ret == null) {
            System.out.println("Error, param "+key+" is missing");
            System.exit(1);
        }
        return ret;
    }
    
    public static double getDouble(String key) {
        double ret = 0.0;
        String prop = properties.getProperty(key);
        if (prop.equalsIgnoreCase("min")) {
            ret = Double.MIN_VALUE;
        } else if (prop.equalsIgnoreCase("-min")) {
            ret = -Double.MIN_VALUE;
        } else if (prop.equalsIgnoreCase("max")) {
            ret = Double.MAX_VALUE;
        } else if (prop.equalsIgnoreCase("-max")) { 
            ret = -Double.MAX_VALUE;
    	} else {
	        try {
	            ret = Double.parseDouble(prop);
	        } catch (Exception e) {
	            System.out.println("Error, param "+key+" is either missing or is not a valid number");
	            e.printStackTrace();
	            System.exit(1);
	        }
    	}
        return ret;
    }
    
}
