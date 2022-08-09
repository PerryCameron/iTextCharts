package chart;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

public class LegendElement {
    PdfCanvas pdfCanvas;
    String elementName;
    private float nameFloatSize;
    private float iconSize;
    private float spacing = 5;
    private Point startPoint;
    private Rectangle icon;
    private Rectangle textBox;
    private DeviceCmyk iconColor;
    private DeviceCmyk fontColor;
    private PdfFont font;

    public LegendElement(PdfCanvas pdfCanvas) {
        this.pdfCanvas = pdfCanvas;
    }

    public void stroke() {
        drawLegendIcon();
        drawLegendText();
    }

    private void drawLegendText() {
        textBox = new Rectangle(startPoint.getX() + iconSize + spacing, startPoint.getY() - (26 - (iconSize) * 0.48f), nameFloatSize, 40);
//        pdfCanvas.rectangle(textBox).stroke();
        Canvas canvas = new Canvas(pdfCanvas, textBox);
        Paragraph paragraph = new Paragraph(elementName)
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setFont(font)
                .setFontColor(fontColor);
        canvas.add(paragraph);
        canvas.close();
    }

    private void drawLegendIcon() {
        icon = new Rectangle(startPoint.getX(), startPoint.getY(), iconSize, iconSize);
        pdfCanvas.rectangle(icon).setFillColor(iconColor).setStrokeColor(iconColor).fillStroke();
    }

    public void setIconSize(float iconSize) {
        this.iconSize = iconSize;
    }

    public void setIconColor(DeviceCmyk iconColor) {
        this.iconColor = iconColor;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
        this.nameFloatSize = getStringLength(elementName);
    }

    public void setFont(PdfFont font) {
        this.font = font;
    }

    public void setFontColor(DeviceCmyk fontColor) {
        this.fontColor = fontColor;
    }

    private float getStringLength(String string) {
        char[] stringArray = string.toCharArray();
        float length = 0;
        for(char c: stringArray) {
            length += font.getWidth(c,12); // TODO make font size changeable
        }
        return length;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
}
