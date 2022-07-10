package charts;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class PDFXYChart {
    // test data
    private int[] xaxis = { 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011,
            2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022 };
    private  int[] yaxis = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
            23, 26, 15, 17, 28, 15, 7};


    private DeviceCmyk barColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);
    private final DeviceCmyk BLACK = new DeviceCmyk(0, 0, 0, 100);

    public void setChartHeight(float chartHeight) {
        this.chartHeight = chartHeight;
    }

    // this is the height given for the chart, does not include legend or numbers (200 default)
    private float chartHeight = 200;
    // entire width of the chart
    private float chartWidth = 0;
    // bottom left x coordinate chart starts on
    private float xStart = 0;
    // bottom left y coordinate chart starts on
    private float yStart = 500;
    // distance calculated for the size of the bars using given table height
    private float tableBarHeightSpacing;
    // distance between gridlines
    private float gridLineDistance;
    // size of the bars in relation to data, is a ratio
    private float multiple;
    // determines if you want gridlines
    private boolean gridLinesVisable = true;
    // chart takes entire space between margins
//    private boolean autoFitWidthToMargins = true;
//    // ratio of the space between the bars to the size of the bars
    private float barSpaceRatio = 0.3f;
    // ratio of all the bars to the entire width of the chart
    private float barChartRatio = 0.95f;
    // standard margin sizes microsoft word uses
    private float pageMarginWidthSizeRatio = 0.15095f;
    // xcordinate that the bars start on
    private float barStartPoint;


    // the increment values marked for each gridline
    private float scale;

    private float yIncrement;
    private PdfCanvas pdfCanvas;
    private float barWidth;
    private float spacerWidth;
    private float pdfPageWidth;
    private float pdfPageHeight;

    public PDFXYChart(PdfPage page) {
        this.pdfCanvas = new PdfCanvas(page);
    }

    public void stroke() {
        printValues();
        this.multiple = 6;
        this.scale = getScale(getLargestStat());
        this.tableBarHeightSpacing = scale * multiple;
        this.gridLineDistance = chartHeight / multiple;
        this.yIncrement = scale / multiple;
        if(chartWidth == 0) setChartWidth();
        if(xStart == 0) getXStart();
        calculateBarSize();
        calculateBarStartPoint();
        createXScale();
        if(gridLinesVisable)
        createGridLines();
        setYscaleText();
        createBars(pdfCanvas);
    }

    /**
     *  Temporary method for testing values
     */
    private void printValues() {
        getPDFSize();
        System.out.println("pdfPageWidth: " + pdfPageWidth);
        System.out.println("pdfPageHeight: " + pdfPageHeight);
        System.out.println("chartWidth: " + chartWidth);
        System.out.println("chartHeight: " + chartHeight);
        System.out.println("tableBarHeightSpacing: " + tableBarHeightSpacing);
        System.out.println("gridLineDistance: " + gridLineDistance);
        System.out.println("Bar start point= " + barStartPoint);
        System.out.println("scaleSize(5,10,50,100 etc.)= " + scale);
        System.out.println(("pageMarginWidthSizeRatio: " + pageMarginWidthSizeRatio));
    }

    private void setYscaleText() {
        int increment = 0;
        float ycordinate = yStart - 12;
        for(int i = 0; i < multiple + 1; i++) {
            Rectangle rectangle = new Rectangle(36,ycordinate , 50, 24);
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(String.valueOf(increment)).setTextAlignment(TextAlignment.RIGHT));
            canvas.close();
            increment += yIncrement;
            ycordinate += gridLineDistance;
        }
    }

    /**
     * overloaded method, but this on is private and only used internally
     */
    private void setChartWidth() {
        chartWidth = pdfPageWidth - (pdfPageWidth * pageMarginWidthSizeRatio) * 2;
    }

    private void getXStart() {
        xStart = pdfPageWidth * pageMarginWidthSizeRatio;
    }

    private void getPDFSize() {
        Rectangle thisPage = pdfCanvas.getDocument().getPage(1).getPageSize();
        this.pdfPageHeight = thisPage.getHeight();
        this.pdfPageWidth = thisPage.getWidth();
    }

    private float calculateBarStartPoint() {
        // starting point of the chart plus the amount you want to inset
        barStartPoint = xStart + (chartWidth * ((1 - barChartRatio) / 2));
        return barStartPoint;
    }

    private void createBars(PdfCanvas pdfCanvas) {
        float x = barStartPoint;
        for(float height: yaxis) {
            Rectangle rectangle = new Rectangle(x, yStart, barWidth, calculateBarHeight(height));
            pdfCanvas.rectangle(rectangle).setFillColor(barColor).fillStroke();
            x = x + barWidth + spacerWidth;
        }
        System.out.println("x= " + x);
    }

    private float calculateBarHeight(float height) {
        return height * ((chartHeight * multiple) / tableBarHeightSpacing);
    }

    private void createXScale() {
        pdfCanvas.moveTo(xStart, yStart -1);
        pdfCanvas.lineTo(xStart + chartWidth, yStart -1);
        pdfCanvas.closePathStroke();
    }

    private void createGridLines() {
        float scaleHeight = yStart;
        for(int i = 0; i < multiple; i++) {
            pdfCanvas.setStrokeColor(barColor);
            scaleHeight = scaleHeight + gridLineDistance;
            pdfCanvas.moveTo(xStart, scaleHeight);
            pdfCanvas.lineTo(xStart + chartWidth, scaleHeight);
            pdfCanvas.closePathStroke();
        }
    }

    private float getScale(int largestColumn) {
        float scaleSize = getScaleSize(largestColumn);
        return scaleSize;
    }

    private float getNumberOfBars() {
        return xaxis.length;
    }

    private void calculateBarSize() {
        // divide 90% of chartWidth by the number of bars
        float barsAndSpacers = (chartWidth * barChartRatio) / (getNumberOfBars());
        System.out.println("Bar + Spacer Size= " + barsAndSpacers);
        // this will give the width of 1 bar + 1 spacer
        this.spacerWidth = barsAndSpacers * barSpaceRatio;
        System.out.println("Spacer Size= " + spacerWidth);
        this.barWidth = barsAndSpacers * (1 - barSpaceRatio);
        System.out.println("Bar Size= " + barWidth);
        // then split that number up
//        chartWidth
    }

    private int getLargestStat() {
        int largestSize = 0;
        for(int height: yaxis) {
            if(height > largestSize)
                largestSize = height;
        }
        return largestSize;
    }

    private int getScaleSize(int number) {
        int scaleSize = 0;
        if(number < 50) scaleSize = round (number, 5);
        else if(number < 100) scaleSize = round(number,10);
        else if(number < 500) scaleSize = round(number,50);
        else if(number < 1000) scaleSize = round(number,100);
        return scaleSize;
    }

    private int round(int n, int roundBy)
    {
        // Smaller multiple
        int a = (n / roundBy) * roundBy;
        // Larger multiple
        int b = a + roundBy;
        return b;
    }

    /**
     * Sets the width of the table
     * @param chartWidth
     */
    public void setChartWidth(float chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Sets the color of the bars
     * @param barColor
     */
    public void setBarColor(DeviceCmyk barColor) {
        this.barColor = barColor;
    }

    /**
     *  Sets page margins that the chart will respect
     * @param pageMarginWidthSizeRatio
     */
    public void setPageMarginWidthSizeRatio(float pageMarginWidthSizeRatio) {
        this.pageMarginWidthSizeRatio = pageMarginWidthSizeRatio;
    }
}
