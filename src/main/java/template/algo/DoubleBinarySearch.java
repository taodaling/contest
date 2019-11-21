package template.algo;

public abstract class DoubleBinarySearch {
    private final double relativeErrorTolerance;
    private final double absoluteErrorTolerance;

    public DoubleBinarySearch(double relativeErrorTolerance, double absoluteErrorTolerance) {
        this.relativeErrorTolerance = relativeErrorTolerance;
        this.absoluteErrorTolerance = absoluteErrorTolerance;
    }

    public abstract boolean check(double mid);

    public double binarySearch(double l, double r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (r - l > absoluteErrorTolerance) {
            if ((r < 0 && (r - l) < -r * relativeErrorTolerance) || (l > 0 && (r - l) < l * relativeErrorTolerance)) {
                break;
            }

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
