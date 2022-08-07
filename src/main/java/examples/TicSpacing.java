package examples;

import scaling.ChartScale;

public class TicSpacing {
    public static void main(String[] args) {
        ChartScale chartScale = new ChartScale();
        chartScale.setMinMaxPoints(20,100);
        System.out.println("number of tics= " + chartScale.getNumberOfTics());
        System.out.println("number of nice tics= " + chartScale.getNiceTics());
        System.out.println("recommend autosize= " + chartScale.recommedScaleAutoSize());
        System.out.println(("range = " + chartScale.getRange()));
        System.out.println("tic spacing = " + chartScale.getTickSpacing());
        System.out.println("nice min= " + chartScale.getNiceMin());
        System.out.println("nice max= " + chartScale.getNiceMax());
    }
}
