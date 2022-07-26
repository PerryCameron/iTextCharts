package chart;

import exception.DataIntegrityException;
import scaling.ChartColors;
import scaling.ChartScale;

import java.math.BigDecimal;
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


    int SINGLE_DATA_SET = 1;
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
        else if(number instanceof BigDecimal)
            return ((BigDecimal) number).floatValue();
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

    public final static class DataSet<X,Y>   {

        ArrayList<BarChart.Data<X,Y>> dataSet = new ArrayList<>();

        private String name = "";

        public DataSet(String name) {
            this.name = name;
        }

        public void add(BarChart.Data<X,Y> data) {
            dataSet.add(data);
        }

        public int size() {
            return dataSet.size();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Data<X, Y>> getDataSet() {
            return dataSet;
        }

        public void remove(int element) {
            dataSet.remove(element);
        }

        public void clear() {
            dataSet.clear();
        }

        public Data<X, Y> get(int element) {
            return dataSet.get(element);
        }

    }

    public final static class Series<X,Y> extends ArrayList<DataSet<X, Y>> {

        public Series() {
        }

        /**
         * Adds multiple sets to the ArrayList at the same time and makes a couple of checks to make sure the
         * data is good
         * @param args
         */
        public void addAll(DataSet... args)  {
            for(DataSet arg: args) {
                this.add(arg);
            }
            checkIntegrity();
        }

        /**
         * Calls two functions to make sure the data sets are of the same length and that the categories match.
         */
        public void checkIntegrity() {
            int size = get(0).getDataSet().size();
            for(DataSet d: this) {
                try {
                    checkDataSize(size, d);
                    checkCategoryMatch(d);
                } catch (DataIntegrityException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Checks to make sure categories for all the data sets put into the series match
         * @param d
         * @throws DataIntegrityException
         */
        private void checkCategoryMatch(DataSet d) throws DataIntegrityException{
            // prevents checking the first data set against itself
            if(!d.equals(this.get(0)))
            for(int i = 0; i < this.get(0).getDataSet().size(); i++) {
                if(!(this.get(0).get(i).getX().equals(d.get(i).getX())))
                    throw new DataIntegrityException("Data Set " + d.getName() + " categories do not match data set "
                        + this.get(0).getName() + " in this series");
                else System.out.println("match");
            }
        }

        /**
         * Checks to make sure all the data sets are of the same length
         * @param dataSize
         * @param dataSet
         * @throws DataIntegrityException
         */
        private void checkDataSize(int dataSize, DataSet dataSet) throws DataIntegrityException {
            if(dataSize != dataSet.size())
                throw new DataIntegrityException("Data Set " + dataSet.getName() + " is a different size than data set "
                        + this.get(0).getName() + " in this series");
        }
    }
}
