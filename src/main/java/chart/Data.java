package chart;

public class Data<X,Y> {

    X x;
    Y y;

    public Data(X x, Y y) {
        this.x = x;
        this.y = y;
    }


    public X getX() {
        return x;
    }

    private void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }
}
