package charts;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import scaling.ChartScale;

public class PDFXYChart {
    // test data
    private float[] xaxisData;
    private float[] yaxisData;

    private DeviceCmyk barColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);
    private DeviceCmyk gridLineColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);
    private DeviceCmyk xAxisColor = new DeviceCmyk(0, 0, 0, 100);

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
    // ratio of the space between the bars to the size of the bars
    private float barSpaceRatio = 0.3f;
    // ratio of all the bars to the entire width of the chart
    private float barChartRatio = 0.95f;
    // standard margin sizes microsoft word uses
    private float pageMarginWidthSizeRatio = 0.15095f;
    // xcordinate that the bars start on
    private float barStartPoint;
    // object to make a nice looking scale
    private ChartScale numScale;
    // canvas object
    private PdfCanvas pdfCanvas;
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

    public PDFXYChart(PdfPage page) {
        this.pdfCanvas = new PdfCanvas(page);
    }

    public void stroke() {
        getPDFSize();
        this.numScale = getGridLineSpacing();
        printNumScaleValues();
        this.gridLineDistance = chartHeight / (float) numScale.getNumberOfTics();
        if (chartWidth == 0) setChartWidth();
        if (xStart == 0) getXStart();
        if (yStart == 0) getYStart();
        calculateBarSize();
        calculateBarStartPoint();
        createXScale();
        if (gridLinesVisable)
            createGridLines();
        createYAxisScale();
        setTitle();
        setYscaleText();
        printValues();
        createBars(pdfCanvas);
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
        System.out.println("minPoint= " + numScale.getMinPoint());
        System.out.println("maxPoint= " + numScale.getMaxPoint());
        System.out.println("tickSpacing= " + numScale.getTickSpacing());
        System.out.println("range= " + numScale.getRange());
        System.out.println("niceMax= " + numScale.getNiceMax());
        System.out.println("niceMin= " + numScale.getNiceMin());
        System.out.println("numberOfTics= " + numScale.getNumberOfTics());
    }

    private void setYscaleText() {
        int increment = 0;
        float ycordinate = yStart - 12;
        for (int i = 0; i < numScale.getNumberOfTics() + 1; i++) {
            Rectangle rectangle = new Rectangle(36, ycordinate, 50, 24);
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(String.valueOf(increment)).setTextAlignment(TextAlignment.RIGHT).setFontColor(xAxisColor));
            canvas.close();
            increment += numScale.getTickSpacing();
            ycordinate += gridLineDistance;
        }
    }

    private void setTitle() {
        Rectangle rectangle = new Rectangle(xStart, yStart + chartHeight, chartWidth, titleFontSize + 12);
        Canvas canvas = new Canvas(pdfCanvas, rectangle);
        canvas.add(new Paragraph(String.valueOf(title)).setTextAlignment(TextAlignment.CENTER).setFontColor(xAxisColor).setFontSize(titleFontSize));
        canvas.close();
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

    private void getYStart() {
        yStart = pdfPageHeight - chartHeight - (pdfPageHeight * 0.15f);
    }

    private void getPDFSize() {
        Rectangle thisPage = pdfCanvas.getDocument().getPage(1).getPageSize();
        this.pdfPageHeight = thisPage.getHeight();
        this.pdfPageWidth = thisPage.getWidth();
    }

    private void calculateBarStartPoint() {
        // starting point of the chart plus the amount you want to inset
        barStartPoint = xStart + spacerWidth + (chartWidth * ((1 - barChartRatio) / 2));
    }

    private double calculateYAxisScaleStartPoint() {
        // starting point of the chart plus the amount you want to inse
        return xStart + (chartWidth * ((1 - barChartRatio) / 2));
    }

    private void createYAxisScale() {
        pdfCanvas.setStrokeColor(gridLineColor);
        pdfCanvas.moveTo(calculateYAxisScaleStartPoint(), yStart);
        pdfCanvas.lineTo(calculateYAxisScaleStartPoint(), yStart + chartHeight);
        pdfCanvas.closePathStroke();
        createMiniTics();
    }

    private void createMiniTics() {
        float miniTicSize = (float) (gridLineDistance / 5);
        float yMiniTicStart = yStart;
        for (int i = 0; i < numScale.getNumberOfTics() * 5; i++) {
            pdfCanvas.moveTo(calculateYAxisScaleStartPoint(), yMiniTicStart);
            pdfCanvas.lineTo(calculateYAxisScaleStartPoint() - chartWidth * 0.015, yMiniTicStart);
            pdfCanvas.closePathStroke();
            yMiniTicStart += miniTicSize;
            System.out.print(i + " ");
        }
        System.out.println();
    }

    private void createBars(PdfCanvas pdfCanvas) {
        float x = barStartPoint;
        for (float height : yaxisData) {
            pdfCanvas.setStrokeColor(barColor);
            Rectangle rectangle = new Rectangle(x, yStart, barWidth, calculateBarHeight(height));
            pdfCanvas.rectangle(rectangle).setFillColor(barColor).fillStroke();
            x = x + barWidth + spacerWidth;
        }
        System.out.println("x= " + x);
    }

    private float calculateBarHeight(float height) {
        // will determine the value of 1 part
        float barPart = (float) (chartHeight / numScale.getNiceMax());
        return (float) (height * barPart);  // not quite right, i fucked this up
    }

    /**
     * Creates the line on the X axis
     */
    private void createXScale() {
        pdfCanvas.setStrokeColor(xAxisColor);
        pdfCanvas.moveTo(xStart, yStart - 1);
        pdfCanvas.lineTo(xStart + chartWidth, yStart - 1);
        pdfCanvas.closePathStroke();
    }

    private void createGridLines() {
        float scaleHeight = yStart;
        for (int i = 0; i < numScale.getNumberOfTics(); i++) {
            pdfCanvas.setStrokeColor(gridLineColor);
            scaleHeight = scaleHeight + gridLineDistance;
            pdfCanvas.moveTo(xStart, scaleHeight);
            pdfCanvas.lineTo(xStart + chartWidth, scaleHeight);
            pdfCanvas.closePathStroke();
        }
    }

    private float getNumberOfBars() {
        return xaxisData.length;
    }

    private void calculateBarSize() {
        // divide 90% of chartWidth by the number of bars
        float barsAndSpacers = (chartWidth * barChartRatio) / (getNumberOfBars());
        // this will give the width of space between bars
        this.spacerWidth = barsAndSpacers * barSpaceRatio;
        // this gives the width of a bar
        this.barWidth = barsAndSpacers * (1 - barSpaceRatio);
    }

    private float[] getMinMaxStats() {
        float maxSize = yaxisData[0];
        float minSize = yaxisData[0];
        float result[] = new float[2];
        for (float height : yaxisData) {
            if (height > maxSize)
                maxSize = height;
            if (height < minSize)
                minSize = height;

        }
        result[0] = minSize;
        result[1] = maxSize;
        return result;
    }

    private ChartScale getGridLineSpacing() {
        float minmax[] = getMinMaxStats();
        ChartScale numScale = new ChartScale(minmax[0], minmax[1]);
        return numScale;
    }

    /**
     * Sets the width of the table
     *
     * @param chartWidth
     */
    public void setChartWidth(float chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Sets the color of the bars
     *
     * @param barColor
     */
    public void setBarColor(DeviceCmyk barColor) {
        this.barColor = barColor;
    }

    public void setxAxisColor(DeviceCmyk xAxisColor) {
        this.xAxisColor = xAxisColor;
    }

    /**
     * Sets page margins that the chart will respect
     *
     * @param pageMarginWidthSizeRatio
     */
    public void setPageMarginWidthSizeRatio(float pageMarginWidthSizeRatio) {
        this.pageMarginWidthSizeRatio = pageMarginWidthSizeRatio;
    }

    public void setXaxisData(float[] xaxisData) {
        this.xaxisData = xaxisData;
    }

    public void setYaxisData(float[] yaxisData) {
        this.yaxisData = yaxisData;
    }

    public void setVerticalHeight(float yStart) {
        this.yStart = yStart;
    }

    public void setChartHeight(float chartHeight) {
        this.chartHeight = chartHeight;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }
}
