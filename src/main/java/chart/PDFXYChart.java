package chart;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import scaling.BarColors;
import scaling.ChartScale;

public class PDFXYChart {
    // test data
    private String[] xaxisData;
    private float[] yaxisData;

    BarColors barColors = new BarColors();
//    private DeviceCmyk barColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);
    private DeviceCmyk gridLineColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);
    private DeviceCmyk scaleColor = new DeviceCmyk(0, 0, 0, 100);

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
    // allows to choose bar colors (there is no limit)
    private boolean multiColoredBars = false;
    // outline bars with a different color
    private boolean outLineBars = false;
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
        if (gridLinesVisable)
            createGridLines();
        createYAxisScale();
        setTitle();
        setYscaleLabels();
        createXAxisLabels();
        printValues();
        createBars(pdfCanvas);
        createXAxisScale();
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

    private void setTitle() {
        Rectangle rectangle = new Rectangle(xStart, yStart + chartHeight, chartWidth, titleFontSize + 20);
        Canvas canvas = new Canvas(pdfCanvas, rectangle);
        canvas.add(new Paragraph(String.valueOf(title))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(scaleColor)
                .setFontSize(titleFontSize));
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

    /**
     * Creates the X Axis Scale
     */
    private void createXAxisScale() {
        pdfCanvas.setStrokeColor(scaleColor);
        pdfCanvas.moveTo(xStart, yStart);
        pdfCanvas.lineTo(xStart + chartWidth, yStart);
        pdfCanvas.closePathStroke();
        createXAxisMiniTics();
    }

    private void createXAxisMiniTics() {
        float startPoint = barStartPoint + (barWidth * 0.5f);
        for(int i = 0; i < getNumberOfBars(); i++) {
            pdfCanvas.setStrokeColor(scaleColor);
            pdfCanvas.moveTo(startPoint, yStart);
            pdfCanvas.lineTo(startPoint, yStart - (chartHeight * 0.02f));
            pdfCanvas.closePathStroke();
            System.out.print(i + " ");
            startPoint = startPoint + barWidth + spacerWidth;
        }
        System.out.println();
    }

    private void createXAxisLabels() {
            float rectangleWidth = 24;
            float rectangleHeight = 80;
            float xAxisStartPoint = (barStartPoint + (barWidth * 0.5f)) - (rectangleWidth * 0.5f) + 1;
            float yAxisStartPoint = yStart - rectangleHeight -(chartHeight * 0.035f);
        for(int i = 0; i < getNumberOfBars(); i++) {
            Rectangle rectangle = new Rectangle(xAxisStartPoint, yAxisStartPoint, rectangleWidth, rectangleHeight);
//            pdfCanvas.rectangle(rectangle);
//            pdfCanvas.stroke();
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(xaxisData[i])
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(scaleColor)
                            .setFontSize(9)
                    .setRotationAngle(4.71));
            canvas.close();
            xAxisStartPoint = xAxisStartPoint + barWidth + spacerWidth;
        }
    }


    private double calculateYAxisScaleStartPoint() {
        // starting point of the chart plus the amount you want to inset
        return xStart + (chartWidth * ((1 - barChartRatio) / 2));
    }

    private void createYAxisScale() {
        pdfCanvas.setStrokeColor(scaleColor);
        pdfCanvas.moveTo(calculateYAxisScaleStartPoint(), yStart);
        pdfCanvas.lineTo(calculateYAxisScaleStartPoint(), yStart + chartHeight);
        pdfCanvas.closePathStroke();
        createYAxisMiniTics();
    }

    private void createYAxisMiniTics() {
        float xAxisStartPoint = (float) calculateYAxisScaleStartPoint();
        float miniTicSize = (float) (gridLineDistance / 5);
        float yMiniTicStart = yStart;
        for (int i = 0; i < numScale.getNumberOfTics() * 5 + 1; i++) {
            // starts cursor at bottom left corner of chart
            pdfCanvas.moveTo(xAxisStartPoint, yMiniTicStart);
            if(i % 5 ==0)
                pdfCanvas.lineTo(xAxisStartPoint - chartWidth * 0.025, yMiniTicStart);
            else
                pdfCanvas.lineTo(xAxisStartPoint - chartWidth * 0.015, yMiniTicStart);
            pdfCanvas.closePathStroke();
            yMiniTicStart += miniTicSize;
        }
    }

    private void setYscaleLabels() {
        int increment = 0;
        float ycordinate = yStart - 12;
        for (int i = 0; i < numScale.getNumberOfTics() + 1; i++) {
            Rectangle rectangle = new Rectangle(36, ycordinate, 50, 24);
            Canvas canvas = new Canvas(pdfCanvas, rectangle);
            canvas.add(new Paragraph(String.valueOf(increment)).setTextAlignment(TextAlignment.RIGHT).setFontColor(scaleColor));
            canvas.close();
            increment += numScale.getTickSpacing();
            ycordinate += gridLineDistance;
        }
    }

    private void createBars(PdfCanvas pdfCanvas) {
        float x = barStartPoint;
        for (float height : yaxisData) {
            DeviceCmyk barColor = barColors.nextColor();
            pdfCanvas.setStrokeColor(barColor);
            Rectangle rectangle = new Rectangle(x, yStart, barWidth, calculateBarHeight(height));
            pdfCanvas.rectangle(rectangle).setFillColor(barColor).fillStroke();
            x = x + barWidth + spacerWidth;
        }
        System.out.println("x= " + x);
    }

//    private DeviceCmyk getBarStrokeColor() {
//
//    }

    private float calculateBarHeight(float height) {
        // will determine the value of 1 part
        float barPart = (float) (chartHeight / numScale.getNiceMax());
        return (float) (height * barPart);
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

    public void setScaleColor(DeviceCmyk scaleColor) {
        this.scaleColor = scaleColor;
    }

    public void setGridLineColor(DeviceCmyk gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    public BarColors getBarColors() {
        return barColors;
    }

    /**
     * Sets page margins that the chart will respect
     *
     * @param pageMarginWidthSizeRatio
     */
    public void setPageMarginWidthSizeRatio(float pageMarginWidthSizeRatio) {
        this.pageMarginWidthSizeRatio = pageMarginWidthSizeRatio;
    }

    public void setXaxisData(String[] xaxisData) {
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
