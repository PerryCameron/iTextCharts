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
        float[] yaxis = { 21, 19, 18, 15, 13, 27, 17, 19, 20, 15, 24, 17, 21, 18,
                23, 26, 15, 17, 28, 15, 7};
        BarChart chart1 = new BarChart(page);
        chart1.getBarColors().setBarColor(new DeviceCmyk(.12f, .05f, 0, 0.02f));
        chart1.setChartHeight(200);
        chart1.setXaxisData(xaxis);
        chart1.setYaxisData(yaxis);
        chart1.setVerticalHeight(550);
        chart1.setTitleFontSize(20);
        chart1.setGridLineColor(new DeviceCmyk(64,0,6,3));
        chart1.setTitle("New Members By Year");
        chart1.stroke();

        String[] xaxis1 = { "Guppy", "Goldfish", "Oscar", "Swordfish", "Shark" };
        float[] yaxis1 = { 101, 123, 78, 234, 102 };
        BarChart chart2 = new BarChart(page);
        chart2.getBarColors().setMultiColored(true);
        chart2.setChartHeight(300);
        chart2.setXaxisData(xaxis1);
        chart2.setYaxisData(yaxis1);
        chart2.setVerticalHeight(120);
        chart2.setTitleFontSize(15);
        chart2.setTitle("Types Of Fish Bought In 2022");
        chart2.stroke();

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