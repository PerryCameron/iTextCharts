package scaling;

import com.itextpdf.kernel.colors.DeviceCmyk;

import java.util.ArrayList;

public class ChartColors {

    ArrayList<DeviceCmyk> barColorPalette;
    private DeviceCmyk gridLineColor = setDefaultGridLineColor();
    private DeviceCmyk scaleColor = setDefaultScaleColor();
    private DeviceCmyk backgroundColor = setDefaultBackgroundColor();
    private DeviceCmyk borderColor = setDefaultBorderColor();

    int numberOfColors = 0;
    int colorSelected = 0;
    boolean multiColored = false;

    public ChartColors() {
        this.barColorPalette = new ArrayList<>();
        addDefaultColors();
    }

    public void resetDefaultColors() {
        gridLineColor = setDefaultGridLineColor();
        scaleColor = setDefaultScaleColor();
        backgroundColor = setDefaultBackgroundColor();
        borderColor = setDefaultBorderColor();
    }

    public  DeviceCmyk setDefaultGridLineColor() {
        return new DeviceCmyk(.12f, .05f, 0, 0.02f);
    }

    public  DeviceCmyk setDefaultScaleColor() {
        return new DeviceCmyk(0, 0, 0, 100);
    }

    public  DeviceCmyk setDefaultBackgroundColor() {
        return new DeviceCmyk(0, 0, 0, 0);
    }

    public  DeviceCmyk setDefaultBorderColor() {
        return new DeviceCmyk(0, 0, 0, 100);
    }

    public void addDefaultColors() {
        barColorPalette.add(new DeviceCmyk(0.79f, 0.33f, 0, 0.11f));
        barColorPalette.add(new DeviceCmyk(0,0.06f,0.09f,0.73f));
        barColorPalette.add(new DeviceCmyk(0,0.55f,0.71f,0.11f));
        barColorPalette.add(new DeviceCmyk(0.02f,0,0.53f,0.04f));
        barColorPalette.add(new DeviceCmyk(0.03f,0,0.05f,0.03f));
        barColorPalette.add(new DeviceCmyk(0.33f,0.11f,0,0.55f));
        barColorPalette.add(new DeviceCmyk(0.28f,0.11f,0,0.26f));
        barColorPalette.add(new DeviceCmyk(0.22f,0.02f,0,0.22f));
        barColorPalette.add(new DeviceCmyk(0.10f,0.11f,0,0.17f));
        setColorChoice();
    }

    /**
     *  picks next color on pallet, when it gets to the end it rotates back to the first color
     * @return
     */
    public DeviceCmyk nextBarColor() {
        DeviceCmyk color;
        if(multiColored) {
            // gets current color selected on palette
            color = barColorPalette.get(colorSelected);
            // move to next color, unless we have reached the end of the palette then start over
            if (colorSelected == barColorPalette.size() - 1)
                colorSelected = 0;
            else
                colorSelected++;
            return color;
        } else
            color = barColorPalette.get(colorSelected);
        // if we are not doing multi colors then first color in array is the bar color
        return color;
    }

    public DeviceCmyk currentBarColor() {
            return barColorPalette.get(colorSelected);
    }

    public void setColorChoice() {
        numberOfColors = barColorPalette.size();
        colorSelected = 0;
    }

    public void setToFirstColor() {
        colorSelected = 0;
    }

    public void flushBarColorPalette() {
        barColorPalette.clear();
        setColorChoice();
    }

    /**
     * This is for use if you are only using one color for your bar, It will choose the color element you want on,
     * the palette and set multiColored to false.
     * @param colorSelected
     */
    public void setBarColorSelected(int colorSelected) {
        this.colorSelected = colorSelected;
        multiColored = false;
    }

    public void addColor(DeviceCmyk color) {
        barColorPalette.add(color);
        setColorChoice();
    }

    public ArrayList<DeviceCmyk> getBarColorPalette() {
        return barColorPalette;
    }

    public boolean isMultiColored() {
        return multiColored;
    }

    public void setMultiColoredBars(boolean multiColored) {
        this.multiColored = multiColored;
    }

    public DeviceCmyk getGridLineColor() {
        return gridLineColor;
    }

    public void setGridLineColor(DeviceCmyk gridLineColor) {
        this.gridLineColor = gridLineColor;
    }

    public DeviceCmyk getScaleColor() {
        return scaleColor;
    }

    public void setScaleColor(DeviceCmyk scaleColor) {
        this.scaleColor = scaleColor;
    }

    public DeviceCmyk getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(DeviceCmyk backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBorderColor(DeviceCmyk borderColor) {
        this.borderColor = borderColor;
    }

    public DeviceCmyk getBorderColor() {
        return borderColor;
    }
}
