package chart;

import scaling.ChartColors;
import scaling.ChartScale;

public interface BarChart {
    // object to handle all colors
    ChartColors chartColors = new ChartColors();
    // object to handle scaling
    ChartScale chartScale = new ChartScale();
    //    the ratio the bars and spaces between them use in comparison to the width of the chart
    float BAR_CHART_RATIO = 0.95f;

    /**
     *  When stroke() is called on chart this method calculates values needed to create the chart
     */
    void setVariables();

    void getGridLineDistance();

    /**
     * This method is called when you want to draw the chart. All variables you wish to implement must be
     * set before this method is called.
     */
    void stroke();

    /**
     * Creates a default width (margin to margin) for the chart if the width is not specified
     * before calling stroke()
     */
    void setChartWidth();

    /**
     * Creates a default x starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    void getXStart();

    /**
     * Creates a default y starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    void getYStart();
}
