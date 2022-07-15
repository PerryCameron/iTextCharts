package examples;


import chart.BarChart;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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


        String[] xaxis = { "2002", "2003", "2004", "2005", "2006", "2007","2008", "2009", "2010", "2011",
                "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022" };
        int[] yaxis = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
                23, 26, 15, 17, 28, 15, 7};
        BarChart chart1 = new BarChart(page);
        for(int i = 0; i < xaxis.length; i++)
            chart1.setData(new BarChart.Data<String, Number> (xaxis[i],yaxis[i]));


        chart1.getChartColors().setBarColorSelected(0);
        chart1.getChartColors().setBackgroundColor(new DeviceCmyk(0, .02f, 0.15f, 0.01f));
        chart1.setChartHeight(200);
//        chart1.setXSeriesData(xaxis);
        chart1.setShowBorder(true);
//        chart1.setYSeriesData(yaxis);
        chart1.setOutLineBars(true);
        chart1.setAutoScale(false);
        chart1.setVerticalStart(550);
        chart1.setTitleFontSize(20);
        chart1.getChartColors().setGridLineColor(new DeviceCmyk(64,0,6,3));
        chart1.setTitle("New Members By Year");
        System.out.println("chart1.stroke();");
        chart1.stroke();

        String[] xaxis1 = { "Guppy", "Goldfish", "Oscar", "Swordfish", "Shark" };
        float[] yaxis1 = { 101, 123, 78, 234, 102 };
        BarChart chart2 = new BarChart(page);
        for(int i = 0; i < xaxis1.length; i++)
            chart2.setData(new BarChart.Data<String, Number> (xaxis1[i],yaxis1[i]));
        chart2.getChartColors().resetDefaultColors();
        chart2.getChartColors().setMultiColoredBars(true);
        chart2.setChartSize(200,200);
//        chart2.setXSeriesData(xaxis1);
//        chart2.setYSeriesData(yaxis1);
        chart2.setShowBorder(true);
        chart2.setOutLineBars(true);
        chart2.setVerticalStart(250);
        chart2.setTitleFontSize(15);
        chart2.setTitle("Types Of Fish Bought In 2022");
        chart2.stroke();
        System.out.println("chart2.stroke();");

//        page = pdf.addNewPage();
        String[] xaxis2 = { "Time", "People", "Newsweek" };
        float[] yaxis2 = { 803, 852, 892};
        BarChart chart3 = new BarChart(page);
        for(int i = 0; i < xaxis2.length; i++)
            chart3.setData(new BarChart.Data<String, Number> (xaxis2[i],yaxis2[i]));
        chart3.getChartColors().resetDefaultColors();
        chart3.getChartColors().setMultiColoredBars(true);
//        chart3.getChartColors().setBackgroundColor(new DeviceCmyk(.12f, .05f, 0, 0.02f));
        chart3.setChartSize(200,350);
//        chart3.setXSeriesData(xaxis2);
//        chart3.setYSeriesData(yaxis2);
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
