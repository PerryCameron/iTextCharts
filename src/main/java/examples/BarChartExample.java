package examples;


import chart.BarChart;
import chart.XYChart;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BarChartExample {
    public static void main(String[] args) {
        // Initialize PDF writer
        PdfWriter writer = null;
        // set file path/name
        final String TESTPATH = System.getProperty("user.home") + "/Documents/ChartExamples";
        // Check to make sure directory exists and if not create it
        checkPath(TESTPATH);
        String dest = TESTPATH+ "/BarChart.pdf";
        try {
            writer = new PdfWriter(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);
        PdfPage page = pdf.addNewPage();
        Document document = new Document(pdf);

        // Some test data
        String[] xaxis = { "2002", "2003", "2004", "2005", "2006", "2007","2008", "2009", "2010", "2011",
                "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022" };
        int[] yaxis = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
                23, 26, 15, 17, 28, 15, 7};
        // Create the bar chart
        BarChart<String,Number> chart1 = new BarChart<>(page);
        // Create a data set for the chart
        XYChart.DataSet chart1DataSet1 = new XYChart.DataSet("New Members");
        // Create a series to hold 1 or more data sets
        XYChart.Series chart1Series = new XYChart.Series();
        // Put data into the data set
        for(int i = 0; i < xaxis.length; i++)
            chart1DataSet1.add(new BarChart.Data<String, Number> (xaxis[i],yaxis[i]));
        // put data set into the series
        chart1Series.add(chart1DataSet1);
        // put series into the chart
        chart1.setSeries(chart1Series);
        chart1.getChartColors().setBarColorSelected(0);
        chart1.getChartColors().setBackgroundColor(new DeviceCmyk(0, .02f, 0.15f, 0.01f));
        chart1.setLegendVisible(true);
        chart1.setChartHeight(200);
        chart1.setShowBorder(true);
        chart1.setOutLineBars(true);
        chart1.setAutoScale(false);
        chart1.setVerticalStart(550);
        chart1.setTitleFontSize(20);
        chart1.getChartColors().setGridLineColor(new DeviceCmyk(64,0,6,3));
        chart1.setTitle("New Members By Year");
        System.out.println("________________________________________________________");
        System.out.println("chart1.stroke();");
        chart1.stroke();
        System.out.println("singleDataSet= " + chart1.isSingleDataSet());
        System.out.println("multi-colored=" + chart1.getChartColors().isMultiColored());

        String[] chart2Xaxis1 = { "Guppy", "Goldfish", "Oscar", "Swordfish", "Shark" };
        float[] chart2Yaxis1 = { 101, 123, 78, 234, 100 };
        float[] chart2Yaxis2 = { 121, 143, 98, 250, 120 };
        float[] chart2Yaxis3 = { 140, 120, 100, 220, 140 };
        float[] chart2Yaxis4 = { 145, 140, 110, 200, 150 };
        BarChart<String,Number> chart2 = new BarChart<>(page);
        XYChart.DataSet chart2DataSet1 = new XYChart.DataSet("2001 Year of");
        XYChart.DataSet chart2DataSet2 = new XYChart.DataSet("2002 Year of");
        XYChart.DataSet chart2DataSet3 = new XYChart.DataSet("2003 Year of");
        XYChart.DataSet chart2DataSet4 = new XYChart.DataSet("2004 Year of");
        XYChart.Series chart2Series = new XYChart.Series();
        for(int i = 0; i < chart2Xaxis1.length; i++) {
            chart2DataSet1.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis1[i]));
            chart2DataSet2.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis2[i]));
            chart2DataSet3.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis3[i]));
            chart2DataSet4.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis4[i]));
        }
//        chart2DataSet2.remove(0);

        chart2Series.addAll(chart2DataSet1,chart2DataSet2,chart2DataSet3,chart2DataSet4);
        chart2.setSeries(chart2Series);
        chart2.getChartColors().resetDefaultColors();
        chart2.getChartColors().setMultiColoredBars(true);
        chart2.setChartSize(200,200);
        chart2.setShowBorder(true);
        chart2.setOutLineBars(true);
        chart2.setVerticalStart(250);
        chart2.setLegendVisible(true);
        chart2.setTitleFontSize(15);
        chart2.setTitle("Types Of Fish Bought");
        System.out.println("________________________________________________________");

        System.out.println("chart2.stroke();");
        chart2.stroke();
        System.out.println("singleDataSet= " + chart2.isSingleDataSet());
        System.out.println("multi-colored=" + chart2.getChartColors().isMultiColored());


        String[] xaxis2 = { "Time", "People", "Newsweek" };
        float[] yaxis2 = { 803, 852, 892};
        BarChart<String,Number> chart3 = new BarChart.BarChartBuilder()
                .nestedPage(page)
                .nestedChartHeight(350)
                .nestedChartWidth(200)
                .nestedXStart(350)
                .nestedYStart(120)
                .createBarChart();
        XYChart.DataSet dataSet3 = new XYChart.DataSet("Magazines");
        XYChart.Series series3 = new XYChart.Series();
        for(int i = 0; i < xaxis2.length; i++)
            dataSet3.add(new BarChart.Data<String, Number> (xaxis2[i],yaxis2[i]));
        series3.add(dataSet3);
        chart3.setSeries(series3);
        chart3.getChartColors().resetDefaultColors();
        chart3.getChartColors().setMultiColoredBars(true);
//        chart3.setChartSize(200,350);
//        chart3.setStartPoint(350,120);
        chart3.setTitleFontSize(9);
        chart3.setLegendVisible(true);
        chart3.setTitle("Magazines Read Over lifetime");
        chart3.getChartColors().setGridLineColor(new DeviceCmyk(0,0.78f,0.78f,0.08f));
        chart3.setShowBorder(true);
        chart3.getChartColors().setBorderColor(new DeviceCmyk(0,0.78f,0.78f,0.08f));
        System.out.println("________________________________________________________");
        System.out.println("chart3.stroke();");
        chart3.stroke();
        System.out.println("singleDataSet= " + chart3.isSingleDataSet());
        System.out.println("multi-colored=" + chart3.getChartColors().isMultiColored());

//        PdfPage page2 = pdf.addNewPage();
        String[] chart4Xaxis1 = { "2002", "2003", "2004", "2005", "2006", "2007","2008", "2009", "2010", "2011",
                "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022" };
        int[] chart4Yaxis1 = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
                23, 26, 15, 17, 28, 15, 7};
        int[] chart4Yaxis2 = { 18, 17, 9, 9, 14, 28, 18, 20, 21, 16, 25, 18, 22, 19,
                24, 27, 16, 18, 29, 16, 8};
        int[] chart4Yaxis3 = { 4, 5, 6, 7, 8, 5, 6, 7, 2, 3, 4, 7, 4, 6,
                7, 5, 6, 7, 8, 6, 2};
        int[] chart4Yaxis4 = { 2, 3, 4, 5, 6, 2, 3, 4, 1, 2, 3, 2, 3, 4,
                4, 5, 6, 3, 4, 5, 6};
        // Create the bar chart
        BarChart<String,Number> chart4 = new BarChart<>(pdf.addNewPage());
        // Create a data set for the chart
        XYChart.DataSet chart4DataSet1 = new XYChart.DataSet("New");
        XYChart.DataSet chart4DataSet2 = new XYChart.DataSet("Return");
        XYChart.DataSet chart4DataSet3 = new XYChart.DataSet("Legacy");
        XYChart.DataSet chart4DataSet4 = new XYChart.DataSet("Differed");
        // Create a series to hold 1 or more data sets
        XYChart.Series chart4Series1 = new XYChart.Series();
        // Put data into the data set
        for(int i = 0; i < chart4Xaxis1.length; i++) {
            chart4DataSet1.add(new BarChart.Data<String, Number>(chart4Xaxis1[i], chart4Yaxis1[i]));
            chart4DataSet2.add(new BarChart.Data<String, Number>(chart4Xaxis1[i], chart4Yaxis2[i]));
            chart4DataSet3.add(new BarChart.Data<String, Number>(chart4Xaxis1[i], chart4Yaxis3[i]));
            chart4DataSet4.add(new BarChart.Data<String, Number>(chart4Xaxis1[i], chart4Yaxis4[i]));
        }
        // put data set into the series
        chart4Series1.addAll(chart4DataSet1,chart4DataSet2,chart4DataSet3,chart4DataSet4);
        // put series into the chart
        chart4.setSeries(chart4Series1);
        chart4.getChartColors().setBarColorSelected(0);
        chart4.setChartHeight(200);
        chart4.setShowBorder(true);
        chart4.setOutLineBars(false);
        chart4.setAutoScale(false);
        chart4.setVerticalStart(550);
        chart4.setTitleFontSize(20);
        chart4.setLegendVisible(true);
        chart4.getChartColors().setGridLineColor(new DeviceCmyk(64,0,6,3));
        chart4.setTitle("New Members By Year");
        System.out.println("________________________________________________________");
        System.out.println("chart4.stroke();");
        chart4.stroke();
        document.close();
        File file = new File(dest);
        Desktop desktop = Desktop.getDesktop(); // Gui_Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()

        try {
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkPath(String path) {
        File recordsDir = new File(path);
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }
    }

}
