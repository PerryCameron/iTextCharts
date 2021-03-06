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

        // Some test data
        String[] xaxis = { "2002", "2003", "2004", "2005", "2006", "2007","2008", "2009", "2010", "2011",
                "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022" };
        int[] yaxis = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
                23, 26, 15, 17, 28, 15, 7};
        // Create the bar chart
        BarChart<String,Number> chart1 = new BarChart<>(page);
        // Create a data set for the chart
        XYChart.DataSet dataSet1 = new XYChart.DataSet();
        // Create a series to hold 1 or more data sets
        XYChart.Series series1 = new XYChart.Series();
        // Put data into the data set
        for(int i = 0; i < xaxis.length; i++)
            dataSet1.add(new BarChart.Data<String, Number> (xaxis[i],yaxis[i]));
        // put data set into the series
        series1.add(dataSet1);
        // put series into the chart
        chart1.setSeries(series1.getSeries());
        chart1.getChartColors().setBarColorSelected(0);
        chart1.getChartColors().setBackgroundColor(new DeviceCmyk(0, .02f, 0.15f, 0.01f));
        chart1.setChartHeight(200);
        chart1.setShowBorder(true);
        chart1.setOutLineBars(true);
        chart1.setAutoScale(false);
        chart1.setVerticalStart(550);
        chart1.setTitleFontSize(20);
        chart1.getChartColors().setGridLineColor(new DeviceCmyk(64,0,6,3));
        chart1.setTitle("New Members By Year");
        System.out.println("chart1.stroke();");
//        chart1.sortData();
        chart1.stroke();

        String[] chart2Xaxis1 = { "Guppy", "Goldfish", "Oscar", "Swordfish", "Shark" };
        float[] chart2Yaxis1 = { 101, 123, 78, 234, 102 };
        float[] chart2Yaxis2 = { 121, 143, 98, 250, 118 };
        BarChart<String,Number> chart2 = new BarChart<>(page);
        XYChart.DataSet chart2dataSet1 = new XYChart.DataSet();
        XYChart.DataSet chart2dataSet2 = new XYChart.DataSet();
        XYChart.Series chart2Series = new XYChart.Series();
        for(int i = 0; i < chart2Xaxis1.length; i++) {
            chart2dataSet1.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis1[i]));
            chart2dataSet2.add(new BarChart.Data<String, Number>(chart2Xaxis1[i], chart2Yaxis2[i]));
        }
//        chart2Series.add(chart2dataSet1.getSet());
//        chart2Series.add(chart2dataSet2.getSet());
        chart2Series.addAll(chart2dataSet1,chart2dataSet2);
        chart2.setSeries(chart2Series.getSeries());
        chart2.getChartColors().resetDefaultColors();
        chart2.getChartColors().setMultiColoredBars(true);
        chart2.setChartSize(200,200);
        chart2.setShowBorder(true);
        chart2.setOutLineBars(true);
        chart2.setVerticalStart(250);
        chart2.setTitleFontSize(15);
        chart2.setTitle("Types Of Fish Bought In 2022");
        chart2.stroke();
        System.out.println("chart2.stroke();");
        System.out.println("singelSeries= " + chart2.isSingleDataSet());
        System.out.println("multi-colored=" + chart2.getChartColors().isMultiColored());


//        page = pdf.addNewPage();
        String[] xaxis2 = { "Time", "People", "Newsweek" };
        float[] yaxis2 = { 803, 852, 892};
        BarChart<String,Number> chart3 = new BarChart(page);
        XYChart.DataSet dataSet3 = new XYChart.DataSet();
        XYChart.Series series3 = new XYChart.Series();
        for(int i = 0; i < xaxis2.length; i++)
            dataSet3.add(new BarChart.Data<String, Number> (xaxis2[i],yaxis2[i]));
        series3.add(dataSet3);
        chart3.setSeries(series3.getSeries());
        chart3.getChartColors().resetDefaultColors();
        chart3.getChartColors().setMultiColoredBars(true);
//        chart3.getChartColors().setBackgroundColor(new DeviceCmyk(.12f, .05f, 0, 0.02f));
        chart3.setChartSize(200,350);
        chart3.setStartPoint(350,120);
        chart3.setTitleFontSize(9);
        chart3.setTitle("Magazines Read Over lifetime");
        chart3.getChartColors().setGridLineColor(new DeviceCmyk(0,0.78f,0.78f,0.08f));
        chart3.setShowBorder(true);
        chart3.getChartColors().setBorderColor(new DeviceCmyk(0,0.78f,0.78f,0.08f));
//        chart3.setShowBorder(true);
//        chart3.setGridLinesVisable(false);
        chart3.stroke();
        System.out.println("chart3.stroke();");


        Document document = new Document(pdf);
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
