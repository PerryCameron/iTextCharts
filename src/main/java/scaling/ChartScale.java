package scaling;

public class ChartScale {

    private double minPoint;
    private double maxPoint;
    private double maxTicks = 10;
    private double tickSpacing;
    private double range;
    private double niceMin;
    private double niceMax;
    private double minMaxDiff;
    private double niceMinMaxDiff;

    /**
     * Instantiates a new instance of the ChartScale class.
     *
     * @param min the minimum data point on the axis
     * @param max the maximum data point on the axis
     */
    public ChartScale(double min, double max) {
        this.minPoint = min;
        this.maxPoint = max;
        calculate();
    }

    public ChartScale() {
    }

    /**
     * Calculate and update values for tick spacing and nice
     * minimum and maximum data points on the axis.
     */
    private void calculate() {
        this.minMaxDiff = maxPoint - minPoint;
        this.range = niceNum(minMaxDiff, false);
        this.tickSpacing = niceNum(range / (maxTicks - 1), true);
        this.niceMin =
                Math.floor(minPoint / tickSpacing) * tickSpacing;
        this.niceMax =
                Math.ceil(maxPoint / tickSpacing) * tickSpacing;
    }

    /**
     * Returns a "nice" number approximately equal to range Rounds
     * the number if round = true Takes the ceiling if round = false.
     *
     * @param range the data range
     * @param round whether to round the result
     * @return a "nice" number to be used for the data range
     */
    private double niceNum(double range, boolean round) {
        double exponent; /** exponent of range */
        double fraction; /** fractional part of range */
        double niceFraction; /** nice, rounded fraction */

        exponent = Math.floor(Math.log10(range));

        fraction = range / Math.pow(10, exponent);

        if (round) {
            if (fraction < 1.5)
                niceFraction = 1;
            else if (fraction < 3)
                niceFraction = 2;
            else if (fraction < 7)
                niceFraction = 5;
            else
                niceFraction = 10;
        } else {
            if (fraction <= 1)
                niceFraction = 1;
            else if (fraction <= 2)
                niceFraction = 2;
            else if (fraction <= 5)
                niceFraction = 5;
            else
                niceFraction = 10;
        }
        return niceFraction * Math.pow(10, exponent);
    }

    /**
     * Sets the minimum and maximum data points for the axis.
     *
     * @param minPoint the minimum data point on the axis
     * @param maxPoint the maximum data point on the axis
     */
    public void setMinMaxPoints(double minPoint, double maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        calculate();
    }

    /**
     * Sets maximum number of tick marks we're comfortable with
     *
     * @param maxTicks the maximum number of tick marks for the axis
     */
    public void setMaxTicks(double maxTicks) {
        this.maxTicks = maxTicks;
        calculate();
    }

    public double getMinPoint() {
        return minPoint;
    }

    public double getMaxPoint() {
        return maxPoint;
    }

    public double getMaxTicks() {
        return maxTicks;
    }

    public double getTickSpacing() {
        return tickSpacing;
    }

    public double getRange() {
        return range;
    }

    public double getNiceMin() {
        return niceMin;
    }

    public double getNiceMax() {
        return niceMax;
    }

    public double getNumberOfTics() {
        return niceMax / tickSpacing;
    }

    public double getNiceTics() { return getNiceMinMaxDiff() / tickSpacing; }

    public boolean recommedScaleAutoSize() {
        if(minMaxDiff/niceMax < 0.6f) return true;
        return false;
    }

    public double getMinMaxDiff() {
        return maxPoint - minPoint;
    }

    public double getNiceMinMaxDiff() {
        return niceMax - niceMin;
    }
}
