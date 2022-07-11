package scaling;

import com.itextpdf.kernel.colors.DeviceCmyk;

import java.util.ArrayList;

public class BarColors {

    ArrayList<DeviceCmyk> barColors;
    int numberOfColors = 0;
    int colorSelected = 0;
    boolean multiColored = false;

    public BarColors() {
        this.barColors = new ArrayList<>();
        addDefaultColors();
    }

    public void addDefaultColors() {
        barColors.add(new DeviceCmyk(0.79f, 0.33f, 0, 0.11f));
        barColors.add(new DeviceCmyk(0,0.06f,0.09f,0.73f));
        barColors.add(new DeviceCmyk(0,0.55f,0.71f,0.11f));
        barColors.add(new DeviceCmyk(0.02f,0,0.53f,0.04f));
        barColors.add(new DeviceCmyk(0.03f,0,0.05f,0.03f));
        barColors.add(new DeviceCmyk(0.33f,0.11f,0,0.55f));
        barColors.add(new DeviceCmyk(0.28f,0.11f,0,0.26f));
        barColors.add(new DeviceCmyk(0.22f,0.02f,0,0.22f));
        barColors.add(new DeviceCmyk(0.10f,0.11f,0,0.17f));
        setColorChoice();
    }

    /**
     *  picks next color on pallet, when it gets to the end it rotates back to the first color
     * @return
     */
    public DeviceCmyk nextColor() {
        DeviceCmyk color;
        if(multiColored) {
            color = barColors.get(colorSelected);
            if (colorSelected == barColors.size() - 1)
                colorSelected = 0;
            else
                colorSelected++;
            return color;
        } else
            color = barColors.get(0);
        // if we are not doing multi colors then first color in array is the bar color
        return color;
    }

    public void setColorChoice() {
        numberOfColors = barColors.size();
        colorSelected = 0;
    }

    public void flushDefaultColors() {
        barColors.clear();
        setColorChoice();
    }

    /**
     * This is for use if you are only using one color for your bar, It will remove all colors from the array,
     * set multiColored to false and put the color you chose into the array
     * @param color
     */
    public void setBarColor(DeviceCmyk color) {
        flushDefaultColors();
        barColors.add(color);
        multiColored = false;
    }

    public void addColor(DeviceCmyk color) {
        barColors.add(color);
        setColorChoice();
    }

    public ArrayList<DeviceCmyk> getBarColors() {
        return barColors;
    }

    public boolean isMultiColored() {
        return multiColored;
    }

    public void setMultiColored(boolean multiColored) {
        this.multiColored = multiColored;
    }
}
