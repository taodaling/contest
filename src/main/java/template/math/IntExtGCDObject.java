package template.math;

public class IntExtGCDObject {
    private int[] xy = new int[2];

    public int extgcd(int a, int b) {
        return ExtGCD.extGCD(a, b, xy);
    }

    public int getX() {
        return xy[0];
    }

    public int getY() {
        return xy[1];
    }
}
