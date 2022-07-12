package examples;

import scaling.ChartScale;

public class TicSpacing {
    public static void main(String[] args) {
        ChartScale chartScale = new ChartScale();
        chartScale.setMinMaxPoints(78,230);
        System.out.println("number of tics= " + chartScale.getNumberOfTics());
        System.out.println("number of nice tics= " + chartScale.getNiceTics());
        System.out.println("recommend autosize= " + chartScale.recommedScaleAutoSize());
    }
}
