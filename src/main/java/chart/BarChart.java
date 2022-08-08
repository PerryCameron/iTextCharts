package chart;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.IOException;
import java.util.ArrayList;

public class BarChart<X, Y> extends XYChart<X,Y> {

    private ArrayList<DataSet<X, Y>> series;
    // this is the height given for the chart, does not include legend or numbers (200 default)
    private float chartHeight = 200;
    // entire width of the chart
    private float chartWidth = 0;
    // bottom left x coordinate chart starts on
    private float xStart = 0;
    // bottom left y coordinate chart starts on
    private float yStart = 0;
    // distance between gridlines
    private float gridLineDistance;
    // determines if you want gridlines
    private boolean gridLinesVisible = true;
    // outline bars with a different color
    private boolean outLineBars = false;
    // chart will focus on data-points instead of starting xAxis at 0
    private boolean autoScale = true;
    // if chart has border
    private boolean showBorder = false;
    // shows y scale
    private boolean showYScale = true;
    // show legend
    private boolean legendVisible = false;
    // shows x scale
    private boolean showXScale = true;
    // do we have only one data set in our series?
    private boolean singleDataSet = false;
    // standard margin sizes Microsoft Word uses
    private float pageMarginWidthSizeRatio = 0.15095f;
    // canvas object
    private final PdfCanvas pdfCanvas;
    // width of each bar
    private float barWidth;
    // width of space between bars
    private float spacerWidth;
    // with of pdf page
    private float pdfPageWidth;
    // height of pdf page
    private float pdfPageHeight;
    // title
    private String title = "Title";
    // font size for title
    private float titleFontSize = 20;
    // all points for start of bars starting at 0, must still add offset for correct location
    private float [][]barPoints;
    // all points for start of category mini tics
    private float []categoryMiniTics;
    // font used for entire chart
    private float []legendPoints;
    private PdfFont font;

    private float largestCategoryStringSize;
    private float largestValueStringSize;

    // offset to the chart to draw scales chartWidth-yScaleOffset=actual chart width
    private float yScaleOffset;


    public BarChart(PdfPage page) {
        this.pdfCanvas = new PdfCanvas(page);
    }

    public BarChart(PdfPage page, float chartHeight, float chartWidth, float xStart, float yStart) {
        this.pdfCanvas = new PdfCanvas(page);
        this.chartHeight = chartHeight;
        this.chartWidth = chartWidth;
        this.xStart = xStart;
        this.yStart = yStart;
    }


    /**
     * This method is called when you want to draw the chart. All variables you wish to implement must be
     * set before this method is called.
     */

    public void stroke() {
        setVariables();
        drawBackground();
//        printNumScaleValues();
        drawGridLines();
        drawCategoryScale();
        drawValueScale();
        setTitle();
//        printValues();
        drawBars();
        drawFrame();
        drawLegend();
    }

    /**
     *  When stroke() is called on chart this method collects or calculates values needed to create the chart
     */
    private void setVariables() {
        getPDFSize();
        setFont();
        setNumberOfDataSets();
        setChartWidth();
        calculateYScaleOffset();
        getXStart();
        getYStart();
        getTicSpacing();
        getGridLineDistance();
        calculateBarSize();
        calculateBarStartPoints();
    }

    private void setNumberOfDataSets() {
        // if there is only one element in our series array this is a single series chart
        if(series.size() == SINGLE_DATA_SET) {
            singleDataSet = true;
            // we have more than one data set, so this feature is off
            // because it uses different colors for different data sets
        } else {
            getChartColors().setMultiColoredBars(false);
        }
    }

    private void setFont() {
        try {
            this.font = PdfFontFactory.createRegisteredFont("helvetica");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines distance between gridlines also known as tics
     */
    private void getGridLineDistance() {
        float numTics = (float) chartScale.getNumberOfTics();
        if(autoScale)
            numTics = (float) chartScale.getNiceTics();
        gridLineDistance = chartHeight / numTics ;
    }

    /**
     * Temporary method for testing values
     */
    private void printValues() {
        System.out.println("<-----------------------Chart-------------------->");
        System.out.println("pdfPageWidth: " + pdfPageWidth);
        System.out.println("pdfPageHeight: " + pdfPageHeight);
        System.out.println("chartWidth: " + chartWidth);
        System.out.println("chartHeight: " + chartHeight);
        System.out.println("xStart: " + xStart);
        System.out.println("yStart: " + yStart);
        System.out.println("gridLineDistance: " + gridLineDistance);
        System.out.println(("pageMarginWidthSizeRatio: " + pageMarginWidthSizeRatio));
        System.out.println("BarWidth " + barWidth);
        System.out.println("SpacerWidth= " + spacerWidth);
        System.out.println("largestCategoryStringSize=" + largestCategoryStringSize);
        System.out.println("largestValueStringSize=" + largestValueStringSize);
    }

    private void printNumScaleValues() {
        System.out.println("<-----------numScale--------------->");
        System.out.println("minPoint= " + chartScale.getMinPoint());
        System.out.println("maxPoint= " + chartScale.getMaxPoint());
        System.out.println("tickSpacing= " + chartScale.getTickSpacing());
        System.out.println("range= " + chartScale.getRange());
        System.out.println("niceMax= " + chartScale.getNiceMax());
        System.out.println("niceMin= " + chartScale.getNiceMin());
        System.out.println("numberOfTics= " + chartScale.getNumberOfTics());
        System.out.println("numberOfNicTics= " + chartScale.getNiceTics());
    }

    private void setTitle() {
        Rectangle rectangle = new Rectangle(xStart, yStart + chartHeight, chartWidth, titleFontSize + 20);
        Canvas canvas = new Canvas(pdfCanvas, rectangle);
        canvas.add(new Paragraph(String.valueOf(title))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(chartColors.getScaleColor())
                .setFontSize(titleFontSize));
        canvas.close();
    }

    /**
     * Creates a default width (margin to margin) for the chart if the width is not specified
     * before calling stroke()
     */
    private void setChartWidth() {
        if(chartWidth == 0)
        chartWidth = pdfPageWidth - (pdfPageWidth * pageMarginWidthSizeRatio) * 2;
    }

    /**
     * Creates a default x starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    private void getXStart() {
        if (xStart == 0)
            xStart = pdfPageWidth * pageMarginWidthSizeRatio;
    }

    /**
     * Creates a default y starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    private void getYStart() {
        if (yStart == 0)
            yStart = pdfPageHeight - chartHeight - (pdfPageHeight * 0.15f);
    }

    /**
     * Returns the size of the PDF page
     */
    private void getPDFSize() {
        Rectangle thisPage = pdfCanvas.getDocument().getPage(1).getPageSize();
        this.pdfPageHeight = thisPage.getHeight();
        this.pdfPageWidth = thisPage.getWidth();
    }

    /**
     * Draws the X scale, calls setXScaleMiniTics() method as well as setXScaleLabels()
     */
    private void drawCategoryScale() {
        if(showXScale) {
            pdfCanvas.setStrokeColor(chartColors.getScaleColor());
            drawLine(xStart, yStart,xStart + chartWidth, yStart);
            calculateMiniTicLocation();
            drawCategoryScaleMiniTics();
            drawCategoryScaleLabels();
        }
    }

    private void drawCategoryScaleMiniTics() {
        for (float categoryMiniTic : categoryMiniTics) {
            pdfCanvas.setStrokeColor(chartColors.getScaleColor());
            pdfCanvas.moveTo(categoryMiniTic, yStart);
            pdfCanvas.lineTo(categoryMiniTic, yStart - (chartHeight * 0.02f));
            pdfCanvas.closePathStroke();
        }
    }

    private void calculateMiniTicLocation() {
        float offset = calculateOffset();
        // create a new float array of the length of our data
        categoryMiniTics = new float[barPoints[0].length];
        // gets the halfway point for the bar or group of bars we intend to label
        float halfBarWidth = (barWidth * barPoints.length) / 2;
        // adds offset and halfway point
        for(int i = 0; i < barPoints[0].length; i++)
            categoryMiniTics[i] = barPoints[0][i] + halfBarWidth + offset;
    }

    private void drawCategoryScaleLabels() {
            float rectangleWidth = 24;
            float rectangleHeight = 80;
            float yAxisStartPoint = yStart - rectangleHeight -(chartHeight * 0.035f);
        for(int i = 0; i < categoryMiniTics.length; i++) {
            Rectangle rectangle = new Rectangle(categoryMiniTics[i] -11, yAxisStartPoint, rectangleWidth, rectangleHeight);
//            pdfCanvas.rectangle(rectangle);
//            pdfCanvas.stroke();
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            // sets the longest category string length for placing the legend
            setLargestCategoryStringSize(String.valueOf(series.get(0).getDataSet().get(i).getX()));
               Paragraph paragraph = new Paragraph(String.valueOf(series.get(0).getDataSet().get(i).getX()))
                       .setTextAlignment(TextAlignment.CENTER)
                       .setFontColor(chartColors.getScaleColor())
                       .setFontSize(9)
                       .setFont(font)
                       .setRotationAngle(4.71);
            canvas.add(paragraph);
            canvas.close();
        }
    }

    private void setLargestCategoryStringSize(String string) {
        float stringLength = getStringLength(string);
        if(stringLength > largestCategoryStringSize)
            this.largestCategoryStringSize = stringLength;
    }

    /**
     * increases value if new value is larger
     * @param string A string to find the length of
     */
    public void setLargestValueStringSize(String string) {
        float stringLength = getStringLength(string);
        if(stringLength > largestValueStringSize)
        this.largestValueStringSize = stringLength;
    }

    /**
     * inputs a string and outputs the amount of space it takes
     * @param string
     * @return
     */
    private float getStringLength(String string) {
        char[] stringArray = string.toCharArray();
        float length = 0;
        for(char c: stringArray) {
        length += font.getWidth(c,12); // TODO make font size changeable
        }
        return length;
    }


    private void drawValueScale() {
        if(showYScale) {
            drawLine(xStart + yScaleOffset,yStart,xStart + yScaleOffset,yStart + chartHeight);
            drawValueScaleMiniTics();
            drawValueScaleLabels();
        }
    }

    /**
     * Draws the short ticks along the value scale
     */
    private void drawValueScaleMiniTics() {
        float xAxisStartPoint = xStart + yScaleOffset;
        float miniTicSize = gridLineDistance / 5;
        float yMiniTicStart = yStart;
        for (int i = 0; i < getNumberOfTics() * 5 + 1; i++) {
            // starts cursor at bottom left corner of chart
            float multiple = 0.015f;
            if(i % 5 ==0)
                multiple = 0.025f;
            drawLine(xAxisStartPoint, yMiniTicStart,xAxisStartPoint - chartWidth * multiple, yMiniTicStart);
            yMiniTicStart += miniTicSize;
        }
    }

    /**
     * Writes labels along y scale to interpret data value
     */
    private void drawValueScaleLabels() {
        int increment = 0;
        float numTics = getNumberOfTics();
        if(autoScale)
            increment = (int) chartScale.getNiceMin();
        float yCoordinate = yStart - 12;
        for (int i = 0; i < numTics + 1; i++) {
            Rectangle rectangle = new Rectangle(xStart -54, yCoordinate, 50, 24);
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            // gets largest value size for placement of Y axis label
            setLargestValueStringSize(String.valueOf(increment));
            Paragraph paragraph = new Paragraph(String.valueOf(increment))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFont(font)
                    .setFontColor(chartColors.getScaleColor());
            canvas.add(paragraph);
            canvas.close();
            increment += chartScale.getTickSpacing();
            yCoordinate += gridLineDistance;
        }
    }

    /**
     * Returns number of tics depending on value of autoScale
     */
    private float getNumberOfTics() {
        if(autoScale) {
            return (float) chartScale.getNiceTics();
        }
        return (float) chartScale.getNumberOfTics();
    }

    /**
     * Draws bars on the chart
     */
    private void calculateBarStartPoints() {
        // array to hold points where each bar will start
        barPoints = new float[series.size()][series.get(0).size()];
        // point to create framework for bars
        float x = 0;
        // iterate though all data sets
        for(int i = 0; i < series.size(); i++) {
            // iterate though current data set
            for(int j = 0; j < series.get(i).size(); j++) {
                // capture the value of starting point
                barPoints[i][j] = x;
                System.out.print("[" + x + "]");
                // change point for next iteration
                x = getNewXStart(x);
            }
            System.out.println();
        // if there is more than one dataset this creates start point for next set
            x = (i + 1) * barWidth;
            // this corrects bar spacing if they don't have a border
            if(!outLineBars)
                x += 1 + i;
        }
    }

    /**
     * Determines the starting location for the next bar
     * @param x
     * @return
     */
    private float getNewXStart(float x) {
        if(series.size() == 1)
            x = x + (barWidth + spacerWidth);
        else {
            x = x + (barWidth * series.size()) + (spacerWidth * series.size());
        }
        return x;
    }

    private void drawBars() {
        float offset = calculateOffset();
        Rectangle rectangle = null;
        // iterate the series
        for(int i = 0; i < series.size(); i++) {
            // iterate though current data set
            for (int j = 0; j < series.get(i).size(); j++) {
                // stroke the bar
                pdfCanvas.setStrokeColor(getStrokeColor());
                rectangle = new Rectangle(barPoints[i][j] + offset, yStart, barWidth, calculateBarHeight(convertToFloat(series.get(i).get(j).getY())));
                pdfCanvas.rectangle(rectangle).setFillColor(chartColors.nextBarColor()).fillStroke();
            }
            // will match the color for the legend
            series.get(i).setColor(chartColors.getColorSelected());
            chartColors.forceNextColor();
        }
        chartColors.setToFirstColor();
    }

    private void drawLegend() {
        if(legendVisible) {
            float miniTicSize = chartHeight * 0.02f;
            float yAxisStartPoint = yStart - largestCategoryStringSize - (miniTicSize * 4);
            float xAxisStartPoint = calculateDataSetLegendXValue();
            LegendElement legendElement;
            for (int i = 0; i < series.size(); i++) {
                pdfCanvas.setStrokeColor(getStrokeColor());
                legendElement = new LegendElement(pdfCanvas);
                legendElement.setIconSize(getIconSize());
                legendElement.setFont(font);
                legendElement.setElementName(series.get(i).getName());
                legendElement.setIconColor(chartColors.getColorByElement(series.get(i).getColor()));
                legendElement.setStart(xAxisStartPoint + legendPoints[i],yAxisStartPoint);
                legendElement.setFontColor(chartColors.getScaleColor());
                legendElement.stroke();
            }
        }
    }


    // TODO
    private float calculateDataSetLegendXValue() {
        System.out.println("yScaleOffset= " + yScaleOffset);
        float effectiveChartWidth = chartWidth - yScaleOffset;
        float iconSize = getIconSize();
        float totalSize = 0;
        // starts with length needed for first element
        float longestSize = getStringLength(series.get(0).getName()) + (iconSize * 2) + 5;
        float currentSize = 0;
        legendPoints = new float[series.size()];
        legendPoints[0] = 0;
        // gets the largest of all of them
        for(int i = 1; i < series.size(); i++) {
            currentSize = getStringLength(series.get(i).getName()) + (iconSize * 2) + 5;
            if(currentSize > longestSize) longestSize = currentSize;
        }
        // this loop must be second because you need to make sure you have the largest currentSize
        for(int i = 1; i < series.size(); i++) {
            legendPoints[i] = legendPoints[i-1] + currentSize;
        }
        // puts the width of the first one in, since it was left out of for loop
        totalSize = longestSize * series.size();
        float chartMiddle = yScaleOffset + xStart + ((chartWidth - yScaleOffset) / 2);
        System.out.println("The start of the chart is " + (xStart - yScaleOffset));
        System.out.println("The middle of the chart is " + chartMiddle);
        System.out.println("chart Width= " + chartWidth);
        System.out.println("effective chart Width= " + effectiveChartWidth);
        System.out.println("Total needed Legend size=" + totalSize);
        System.out.println("xAxisStartPoint= " + (chartMiddle - (totalSize * 0.5f)));
        return chartMiddle - (totalSize * 0.5f);
    }

    /**
     * sizes the legend icon depending on the size of the bars
     * @return
     */
    private float getIconSize() {
        float size;
        if(barWidth > 12)
            size = 12;
        else
            size = barWidth;
        return size;
    }

    /**
     * This calculation added to the values in barPoints determines exactly
     * where to place each bar
     * @return
     */
    private float calculateOffset() {
        int row = barPoints.length;
        int col = barPoints[0].length;
        // first get the last number in barPoints array
        // Next add the width of 1 bar to that number, which gives the size of them all put together
        float barArrayWidth = barPoints[row -1][col -1] + barWidth;
        // finally, calculate the offset using the starting point, the chart width, and the yScaleOffset
        // which is the width of the y scale
        return xStart + yScaleOffset + ((chartWidth - yScaleOffset - barArrayWidth) / 2);
    }

    /**
     * Sets the background color. Default is white
     */
    private void drawBackground() {
        float width = chartWidth - yScaleOffset;
        pdfCanvas.setStrokeColor(chartColors.getBackgroundColor());
        Rectangle rectangle = new Rectangle(xStart + yScaleOffset, yStart, width, chartHeight);
        pdfCanvas.rectangle(rectangle).setFillColor(chartColors.getBackgroundColor()).fillStroke();
    }

    /**
     * The space needed for the value scale
     * @return
     */
    private void calculateYScaleOffset() {
        this.yScaleOffset = (chartWidth * ((1 - BAR_CHART_RATIO) / 2));
    }

    private void drawFrame() {
        if(showBorder) {
            pdfCanvas.setStrokeColor(chartColors.getBorderColor());
            // top
            drawLine(xStart + yScaleOffset,yStart + chartHeight,xStart + chartWidth,yStart + chartHeight);
            // right
            drawLine(xStart + chartWidth,yStart + chartHeight,xStart + chartWidth,yStart);
            // bottom
            drawLine(xStart + chartWidth,yStart,xStart + yScaleOffset,yStart);
            // left
            drawLine(xStart + yScaleOffset,yStart,xStart + yScaleOffset,yStart + chartHeight);
        }
    }

    private void drawLine(float xStart,float yStart, float xStop, float yStop) {
        pdfCanvas.moveTo(xStart, yStart);
        pdfCanvas.lineTo(xStop,yStop);
        pdfCanvas.closePathStroke();
    }

    /**
     * Changes outline of bar color if outLineBars is set to true
     * @return
     */
    private DeviceCmyk getStrokeColor() {
        if(outLineBars) return chartColors.getScaleColor();
        return chartColors.currentBarColor();
    }

    /**
     * Calculates the height of a given bar
     * @param height original data
     * @return
     */
    private float calculateBarHeight(float height) {
        float barHeight;
        float totalBarSize = (float) chartScale.getNiceMax();
        if(autoScale) {
            totalBarSize = (float) chartScale.getNiceMinMaxDiff();
            height -= chartScale.getNiceMin();
        }
        float barPart = chartHeight / totalBarSize;
        barHeight = height * barPart;
        return barHeight;
    }

    /**
     * Draws gridlines if enabled
     */
    private void drawGridLines() {
        if (gridLinesVisible) {
            float scaleHeight = yStart;
            for (int i = 0; i < getNumberOfTics(); i++) {
                pdfCanvas.setStrokeColor(chartColors.getGridLineColor());
                scaleHeight = scaleHeight + gridLineDistance;
                drawLine(xStart + yScaleOffset, scaleHeight,xStart + chartWidth, scaleHeight);
            }
        }
    }

    /**
     * returns number of elements in data array used on x-axis
     * @return
     */
    private float getNumberOfBars() {
        float numberOfBars = 0;
        for(DataSet ds: series) {
            numberOfBars += ds.size();
        }
        return numberOfBars;
    }

    /**
     * Calculates the width of a bar, and the width of the space between bars
     */
    private void calculateBarSize() {
        // divide 90% of chartWidth by the number of bars
        float barsAndSpacers = (chartWidth * BAR_CHART_RATIO) / (getNumberOfBars());
        // this will give the width of space between bars
        // ratio of the space between the bars to the size of the bars
        float barSpaceRatio = 0.3f;
        this.spacerWidth = barsAndSpacers * barSpaceRatio;
        // this gives the width of a bar
        this.barWidth = barsAndSpacers * (1 - barSpaceRatio);
    }

    /**
     * Returns the smallest and largest values from data array
     * @return
     */
    private float[] getMinMaxStats() {
        // starts them in the correct range
//        float maxSize = convertToFloat(series.get(0).get(0).getY()), minSize = convertToFloat(series.get(0).get(0).getY());
        float maxSize = convertToFloat(series.get(0).getDataSet().get(0).getY()), minSize = convertToFloat(series.get(0).getDataSet().get(0).getY());
        float[] result = new float[2];
        int i;
        for(i = 0; i < series.size(); i++){
            for (Data d : series.get(i).getDataSet()) {
                float number = convertToFloat(d.getY());
                if (number > maxSize)
                    maxSize = number;
                if (number < minSize)
                    minSize = number;
            }
        }
        result[0] = minSize;
        result[1] = maxSize;
        return result;
    }


    /**
     *
     */
    private void getTicSpacing() {
        float[] minmax = getMinMaxStats();
        chartScale.setMinMaxPoints(minmax[0], minmax[1]);
    }


    /**
     * Sets page margins that the chart will respect if chart width is not set
     * @param pageMarginWidthSizeRatio
     */
    public void setPageMarginWidthSizeRatio(float pageMarginWidthSizeRatio) {
        this.pageMarginWidthSizeRatio = pageMarginWidthSizeRatio;
    }

    /**
     * Sets the (x,y) coordinate for the start location of the chart. This is the bottom left corner
     * @param x
     * @param y
     */
    public void setStartPoint(float x, float y) {
        this.xStart = x;
        this.yStart = y;
    }

    /**
     * Sets the y coordinate for the start location of the chart. This is the bottom left corner
     * @param y
     */
    public void setVerticalStart(float y) {
        this.yStart = y;
    }

    /**
     * Sets the x coordinate for the start location of the chart. This is the bottom left corner
     * @param x
     */
    public void setHorizontalStart(float x) {
        this.xStart = x;
    }

    /**
     * Sets the width of the chart
     * @param chartWidth
     */
    public void setChartWidth(float chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Sets the height of the chart
     * @param chartHeight
     */
    public void setChartHeight(float chartHeight) {
        this.chartHeight = chartHeight;
    }

    /**
     * Sets the size of the chart
     * @param width
     * @param height
     */
    public void setChartSize(float width, float height) {
        this.chartWidth = width;
        this.chartHeight = height;
    }

    /**
     * Sets title of chart
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets font size for chart title
     * @param titleFontSize
     */
    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    /**
     * When true grid lines are shown in chart
     * @param gridLinesVisible
     */
    public void setGridLinesVisible(boolean gridLinesVisible) {
        this.gridLinesVisible = gridLinesVisible;
    }

    /**
     * Scales chart to make data easier to read
     * @param autoScale
     */
    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    /**
     * Outlines the bars if outLineBars is set to true, the color will be the same as scale color
     * @param outLineBars
     */
    public void setOutLineBars(boolean outLineBars) {
        this.outLineBars = outLineBars;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public void setShowYScale(boolean showYScale) {
        this.showYScale = showYScale;
    }

    public void setShowXScale(boolean showXScale) {
        this.showXScale = showXScale;
    }

    public void setSeries(ArrayList<DataSet<X, Y>> series) {
        this.series = series;
    }

    public boolean isSingleDataSet() {
        return singleDataSet;
    }

    public void setLegendVisible(boolean legendVisible) {
        this.legendVisible = legendVisible;
    }

    public static class BarChartBuilder {
        private PdfPage nestedPage;
        private float nestedChartHeight;
        private float nestedChartWidth;
        private float nestedXStart;
        private float nestedYStart;

        public BarChartBuilder nestedPage(PdfPage newPage) {
            this.nestedPage = newPage;
            return this;
        }

        public BarChartBuilder nestedChartHeight(float newChartHeight) {
        this.nestedChartHeight = newChartHeight;
        return this;
        }

        public BarChartBuilder nestedChartWidth(float newChartWidth) {
            this.nestedChartWidth = newChartWidth;
            return this;
        }

        public BarChartBuilder nestedXStart(float newXStart) {
            this.nestedXStart = newXStart;
            return this;
        }

        public BarChartBuilder nestedYStart(float newYStart) {
            this.nestedYStart = newYStart;
            return this;
        }

        public BarChart createBarChart() {
            return new BarChart(nestedPage,nestedChartHeight,nestedChartWidth,nestedXStart,nestedYStart);
        }

    }
}
