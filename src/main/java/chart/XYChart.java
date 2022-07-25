package chart;

import scaling.ChartColors;
import scaling.ChartScale;

import java.util.ArrayList;
import java.util.List;

public abstract class XYChart<X, Y> {

//    private final List<XYChart.Data<X, Y>> data = new ArrayList();
    private final List<ArrayList<XYChart.Data<X,Y>>> series = new ArrayList<>();
    // object to handle all colors
    ChartColors chartColors = new ChartColors();
    // object to handle scaling
    ChartScale chartScale = new ChartScale();
    //    the ratio the bars and spaces between them use in comparison to the width of the chart
    float BAR_CHART_RATIO = 0.95f;


    int SINGLE_SERIES_CHART = 1;
    int LEFT = 0;
    int CENTER = 1;
    int RIGHT = 2;
    int LARGE_TO_SMALL = 3;
    int SMALL_TO_LARGE = 4;
    int STANDARD = 5;

    boolean VERTICAL = true;

    boolean HORIZONTAL = false;



    /**
     * This method is called when you want to draw the chart. All variables you wish to implement must be
     * set before this method is called.
     */
    abstract void stroke();

    /**
     * Sets the (x,y) coordinate for the start location of the chart. This is the bottom left corner
     * @param x
     * @param y
     */
    abstract void setStartPoint(float x, float y);

    /**
     * Sets the size of the chart
     * @param width
     * @param height
     */
    abstract void setChartSize(float width, float height);

//    void setXSeriesData(String[] xSeriesData);
//
//    void setYSeriesData(float[] ySeriesData);

    abstract void setVerticalStart(float y);

    abstract void setHorizontalStart(float x);

    abstract void setChartWidth(float chartWidth);

    abstract void setChartHeight(float chartHeight);

    abstract void setTitle(String title);

    abstract void setTitleFontSize(float titleFontSize);

    abstract void setShowBorder(boolean showBorder);

    abstract void setShowYScale(boolean showYScale);

    abstract void setShowXScale(boolean showXScale);

    /**
     * Returns chart color object which has methods to manipulate the color scheme
     * @return
     */

    abstract void setGridLinesVisable(boolean gridLinesVisable);

    public List<ArrayList<Data<X, Y>>> getSeries() {
        return series;
    }

    public ChartColors getChartColors() {
        return chartColors;
    }

     <N> float convertToFloat(N number)  {
        if(number instanceof Float)
            return (float) number;
        else if(number instanceof Integer)
            return  ((Integer) number).floatValue();
        else if(number instanceof Double)
            return  ((Double) number).floatValue();
        else if(number instanceof Long)
            return  ((Long) number).floatValue();
        else if(number instanceof Short)
            return ((Short) number).floatValue();
        return 0;
    }

    public final static class Data<X,Y> {
        X x;
        Y y;
        public Data(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public X getX() {
            return x;
        }

        private void setX(X x) {
            this.x = x;
        }

        public  Y getY() {
            return y;
        }

        public void setY(Y y) {
            this.y = y;
        }
    }

    public final static class DataSet<X,Y> {
        private ArrayList<BarChart.Data<X,Y>> set;

        private String name;

        public DataSet() {
            this.set = new ArrayList<>();
        }

        public void add(BarChart.Data<X,Y> data) {
            set.add(data);
        }

        public ArrayList<BarChart.Data<X,Y>> getSet() {
            return set;
        }

        public int size() {
            return getSet().size();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public final static class Series<X,Y> {
        String name = "";
        private ArrayList<ArrayList<Data<X, Y>>> series;


        public Series() {
            this.series = new ArrayList<>();
        }

        public void setDataSet(ArrayList<Data<X, Y>> series) {
            this.series.add(series);
        }


        public void clear() {
            series.clear();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<ArrayList<Data<X, Y>>> getSeries() {
            return series;
        }
    }

}
