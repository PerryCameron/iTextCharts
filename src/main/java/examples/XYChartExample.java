package examples;


import charts.PDFXYChart;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class XYChartExample {
    public static final String TESTPATH = System.getProperty("user.home") + "/Documents/Test";

    public static DeviceCmyk barColor = new DeviceCmyk(.12f, .05f, 0, 0.02f);

    public static int tableHeight;
    public static int scaleWidth;
    public static int multiple;
    public static int chartStart;
    public static int chartWidth;

    public static void main(String[] args) {
        // Initialize PDF writer
        PdfWriter writer = null;
        // Check to make sure directory exists and if not create it
        checkPath(TESTPATH);
        String dest = TESTPATH+ "/MembershipReport.pdf";

        try {
            writer = new PdfWriter(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);
        PdfPage page = pdf.addNewPage();


        PDFXYChart chart1 = new PDFXYChart(page);
        chart1.setChartHeight(200);
//        chart1.setChartWidth(300);
        chart1.stroke();

        Document document = new Document(pdf);
        document.close();

        System.out.println("destination=" + dest);
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
