package contest;

import template.datastructure.DoubleIntervalMap;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Iterator;

public class WayToTheUniversity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double carSpeed = 20e3 / 3600;
        double egorSpeed = 5e3 / 3600;

        double t1 = 2 / egorSpeed;
        double total = 4 / egorSpeed;
        double prec = 1e-10;

        DoubleIntervalMap dim = new DoubleIntervalMap();
        dim.setTrue(0, 1e50);

        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int d = in.readInt();
            double l = d / carSpeed;
            double r = (d + 5) / carSpeed;
            dim.setFalse(l, r);
        }

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int d = in.readInt();
            double l = d / carSpeed - t1;
            double r = (d + 5) / carSpeed - t1;
            dim.setFalse(l, r);
        }

        double sum = 0;
        double r = 0;
        for (DoubleIntervalMap.Interval interval : dim) {
            if (Math.abs(interval.l - r) > prec) {
                sum = 0;
            }
            sum += interval.length();
            r = interval.r;
            if (sum >= t1 - prec) {
                out.printf("%.6f", interval.l);
                return;
            }
        }
    }
}
