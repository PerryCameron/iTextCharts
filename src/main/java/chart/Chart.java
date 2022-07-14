package chart;

import scaling.ChartColors;
import scaling.ChartScale;

public interface Chart {
    // object to handle all colors
    ChartColors chartColors = new ChartColors();
    // object to handle scaling
    ChartScale chartScale = new ChartScale();
    //    the ratio the bars and spaces between them use in comparison to the width of the chart
    float BAR_CHART_RATIO = 0.95f;

    int LEFT = 0;
    int CENTER = 1;
    int RIGHT = 2;

    boolean VERTICAL = true;
<<<<<<< HEAD
    boolean HORIZONTAL = false;
=======
>>>>>>> 27eb5872ee9a040301b367f4f0acbbd9a0fffe12


    /**
     * This method is called when you want to draw the chart. All variables you wish to implement must be
     * set before this method is called.
     */
    void stroke();

    /**
     * Sets the (x,y) coordinate for the start location of the chart. This is the bottom left corner
     * @param x
     * @param y
     */
    void setStartPoint(float x, float y);

    /**
     * Sets the size of the chart
     * @param width
     * @param height
     */
    void setChartSize(float width, float height);

    void setXSeriesData(String[] xSeriesData);

    void setYSeriesData(float[] ySeriesData);

    void setVerticalStart(float y);

    void setHorizontalStart(float x);

    void setChartWidth(float chartWidth);

    void setChartHeight(float chartHeight);

    void setTitle(String title);

    void setTitleFontSize(float titleFontSize);

    void setShowBorder(boolean showBorder);

    void setShowYScale(boolean showYScale);

    void setShowXScale(boolean showXScale);

    /**
     * Returns chart color object which has methods to manipulate the color scheme
     * @return
     */

    default ChartColors getChartColors() {
        return chartColors;
    }
<<<<<<< HEAD

    void setGridLinesVisable(boolean gridLinesVisable);
=======
>>>>>>> 27eb5872ee9a040301b367f4f0acbbd9a0fffe12
}
