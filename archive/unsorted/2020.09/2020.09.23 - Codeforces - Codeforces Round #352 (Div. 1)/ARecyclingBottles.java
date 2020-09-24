package contest;

import template.binary.Bits;
import template.geometry.geo2.Point2;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ARecyclingBottles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Point2[] abc = new Point2[3];
        for (int i = 0; i < 3; i++) {
            abc[i] = read(in);
        }
        int n = in.readInt();


        double[] last = new double[4];
        double[] next = new double[4];
        Arrays.fill(last, 1e20);
        last[0] = 0;
        for (int i = 0; i < n; i++) {
            Point2 pt = read(in);
            double[] d = new double[3];
            for (int j = 0; j < 3; j++) {
                d[j] = Point2.dist(abc[j], pt);
            }
            Arrays.fill(next, 1e20);
            for (int j = 0; j < 4; j++) {
                if (Bits.get(j, 0) == 0) {
                    next[Bits.set(j, 0)] = Math.min(next[Bits.set(j, 0)], last[j] + d[0] + d[2]);
                }
                if (Bits.get(j, 1) == 0) {
                    next[Bits.set(j, 1)] = Math.min(next[Bits.set(j, 1)], last[j] + d[1] + d[2]);
                }
                next[j] = Math.min(next[j], last[j] + d[2] * 2);
            }
            double[] tmp = last;
            last = next;
            next = tmp;
        }

        double min = 1e20;
        for(int i = 1; i < 4; i++){
            min = Math.min(min, last[i]);
        }

        out.println(min);
    }

    Point2 read(FastInput in) {
        return new Point2(in.readInt(), in.readInt());
    }
}
