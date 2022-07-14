package chart;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import scaling.ChartColors;

public class BarChart implements XYChart {
    // test data


    private String[] xSeriesData;
    private float[] ySeriesData;

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
    private boolean gridLinesVisable = true;
    // allows choice of bar colors (there is no limit)
    private boolean multiColoredBars = false;
    // outline bars with a different color
    private boolean outLineBars = false;
    // chart will focus on data-points instead of starting xAxis at 0
    private boolean autoScale = true;
    // if chart has border
    private boolean showBorder = false;
    // shows y scale
    private boolean showYScale = true;
    // shows x scale
    private boolean showXScale = true;
    // standard margin sizes Microsoft Word uses
    private float pageMarginWidthSizeRatio = 0.15095f;
    // x-coordinate that the bars start on
    private float barStartPoint;
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

    private float titleFontSize = 20;

    public BarChart(PdfPage page) {
        this.pdfCanvas = new PdfCanvas(page);
    }

    /**
     * This method is called when you want to draw the chart. All variables you wish to implement must be
     * set before this method is called.
     */
    @Override
    public void stroke() {
        setVariables();
        drawBackground();
//        printNumScaleValues();
        drawGridLines();
        drawXScale();
        drawYScale();
        setTitle();
//        printValues();
        drawBars();
        drawFrame();
    }

    /**
     *  When stroke() is called on chart this method calculates values needed to create the chart
     */
    private void setVariables() {
        getPDFSize();
        if (chartWidth == 0) setChartWidth();
        if (xStart == 0) getXStart();
        if (yStart == 0) getYStart();
        getTicSpacing();
        getGridLineDistance();
        calculateBarSize();
        calculateBarStartPoint();
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
        System.out.println("Bar start point= " + barStartPoint);
        System.out.println(("pageMarginWidthSizeRatio: " + pageMarginWidthSizeRatio));
        System.out.println("BarWidth " + barWidth);
        System.out.println("SpacerWidth= " + spacerWidth);
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
        chartWidth = pdfPageWidth - (pdfPageWidth * pageMarginWidthSizeRatio) * 2;
    }

    /**
     * Creates a default x starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    private void getXStart() {
        xStart = pdfPageWidth * pageMarginWidthSizeRatio;
    }

    /**
     * Creates a default y starting point for bottom-left corner of chart if it is not specified before calling stroke()
     */
    private void getYStart() {
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
     * Calculates where to start drawing bars
     */
    private void calculateBarStartPoint() {
        float bWidth = getNumberOfBars() * barWidth;
        float sWidth = (getNumberOfBars() - 1) * spacerWidth;
        float total = bWidth + sWidth;
        barStartPoint = xStart + yScaleOffset() + ((chartWidth - yScaleOffset() - total) * 0.5f);
    }

    /**
     * Draws the X scale, calls setXScaleMiniTics() method as well as setXScaleLabels()
     */
    private void drawXScale() {
        if(showXScale) {
            pdfCanvas.setStrokeColor(chartColors.getScaleColor());
            drawLine(xStart, yStart,xStart + chartWidth, yStart);
            drawXScaleMiniTics();
            drawXScaleLabels();
        }
    }

    private void drawXScaleMiniTics() {
        float startPoint = barStartPoint + (barWidth * 0.5f);
        for(int i = 0; i < getNumberOfBars(); i++) {
            pdfCanvas.setStrokeColor(chartColors.getScaleColor());
            pdfCanvas.moveTo(startPoint, yStart);
            pdfCanvas.lineTo(startPoint, yStart - (chartHeight * 0.02f));
            pdfCanvas.closePathStroke();
            startPoint = startPoint + barWidth + spacerWidth;
        }
    }

    private void drawXScaleLabels() {
            float rectangleWidth = 24;
            float rectangleHeight = 80;
            float xAxisStartPoint = (barStartPoint + (barWidth * 0.5f)) - (rectangleWidth * 0.5f) + 1;
            float yAxisStartPoint = yStart - rectangleHeight -(chartHeight * 0.035f);
        for(int i = 0; i < getNumberOfBars(); i++) {
            Rectangle rectangle = new Rectangle(xAxisStartPoint, yAxisStartPoint, rectangleWidth, rectangleHeight);
//            pdfCanvas.rectangle(rectangle);
//            pdfCanvas.stroke();
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(xSeriesData[i])
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(chartColors.getScaleColor())
                            .setFontSize(9)
                    .setRotationAngle(4.71));
            canvas.close();
            xAxisStartPoint = xAxisStartPoint + barWidth + spacerWidth;
        }
    }

    private void drawYScale() {
        if(showYScale) {
            drawLine(xStart + yScaleOffset(),yStart,xStart + yScaleOffset(),yStart + chartHeight);
            drawYScaleMiniTics();
            drawYScaleLabels();
        }
    }

    private void drawYScaleMiniTics() {
        float xAxisStartPoint = xStart + yScaleOffset();
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
    private void drawYScaleLabels() {
        int increment = 0;
        float numTics = getNumberOfTics();
        if(autoScale)
            increment = (int) chartScale.getNiceMin();
        float ycordinate = yStart - 12;
        for (int i = 0; i < numTics + 1; i++) {
            Rectangle rectangle = new Rectangle(xStart -54, ycordinate, 50, 24);
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(String.valueOf(increment))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(chartColors.getScaleColor()));
            canvas.close();
            increment += chartScale.getTickSpacing();
            ycordinate += gridLineDistance;
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
    private void drawBars() {
        float x = barStartPoint;
        for (float height : ySeriesData) {
            pdfCanvas.setStrokeColor(getStrokeColor());
            Rectangle rectangle = new Rectangle(x, yStart, barWidth, calculateBarHeight(height));
            pdfCanvas.rectangle(rectangle).setFillColor(chartColors.nextBarColor()).fillStroke();
            x = x + barWidth + spacerWidth;
        }
        // resets the palette for the next chart
        getChartColors().setToFirstColor();
    }

    /**
     * Sets the background color. Default is white
     */
    private void drawBackground() {
        float width = chartWidth - yScaleOffset();
        pdfCanvas.setStrokeColor(chartColors.getBackgroundColor());
        Rectangle rectangle = new Rectangle(xStart + yScaleOffset(), yStart, width, chartHeight);
        pdfCanvas.rectangle(rectangle).setFillColor(chartColors.getBackgroundColor()).fillStroke();
    }

    private float yScaleOffset() {
        return (chartWidth * ((1 - BAR_CHART_RATIO) / 2));
    }

    private void drawFrame() {
        if(showBorder) {
            pdfCanvas.setStrokeColor(chartColors.getBorderColor());
            // top
            drawLine(xStart + yScaleOffset(),yStart + chartHeight,xStart + chartWidth,yStart + chartHeight);
            // right
            drawLine(xStart + chartWidth,yStart + chartHeight,xStart + chartWidth,yStart);
            // bottom
            drawLine(xStart + chartWidth,yStart,xStart + yScaleOffset(),yStart);
            // left
            drawLine(xStart + yScaleOffset(),yStart,xStart + yScaleOffset(),yStart + chartHeight);
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
     * @param height
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
        if (gridLinesVisable) {
            float scaleHeight = yStart;
            for (int i = 0; i < getNumberOfTics(); i++) {
                pdfCanvas.setStrokeColor(chartColors.getGridLineColor());
                scaleHeight = scaleHeight + gridLineDistance;
                drawLine(xStart + yScaleOffset(), scaleHeight,xStart + chartWidth, scaleHeight);
            }
        }
    }

    /**
     * returns number of elements in data array used on x-axis
     * @return
     */
    private float getNumberOfBars() {
        return xSeriesData.length;
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
        float maxSize = ySeriesData[0], minSize = ySeriesData[0];
        float[] result = new float[2];
        for (float height : ySeriesData) {
            if (height > maxSize)
                maxSize = height;
            if (height < minSize)
                minSize = height;
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
     * Chart Data
     * @param xSeriesData
     */
    public void setXSeriesData(String[] xSeriesData) {
        this.xSeriesData = xSeriesData;
    }

    /**
     * Data used to label the chart data
     * @param ySeriesData
     */
    public void setYSeriesData(float[] ySeriesData) {
        this.ySeriesData = ySeriesData;
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
     * @param gridLinesVisable
     */
    public void setGridLinesVisable(boolean gridLinesVisable) {
        this.gridLinesVisable = gridLinesVisable;
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

    public boolean isMultiColoredBars() {
        return multiColoredBars;
    }

    public void setMultiColoredBars(boolean multiColoredBars) {
        this.multiColoredBars = multiColoredBars;
    }
}
