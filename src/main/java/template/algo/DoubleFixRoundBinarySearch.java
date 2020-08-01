package template.algo;

public abstract class DoubleFixRoundBinarySearch {
    private int round;

    public DoubleFixRoundBinarySearch(int round) {
        this.round = round;
    }

    public abstract boolean check(double mid);

    public double binarySearch(double l, double r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        int remain = round;
        while (remain-- > 0) {
            double mid = (l + r) / 2;
            if (check(mid)) {
                r = mid;
            } else {
                l = mid;
            }
        }
        return (l + r) / 2;
    }
}
