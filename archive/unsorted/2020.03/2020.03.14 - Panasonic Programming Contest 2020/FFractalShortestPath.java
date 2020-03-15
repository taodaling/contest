package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Radix;
import template.utils.Debug;

public class FFractalShortestPath {
    Debug debug = new Debug(false);

    public long min(long... x) {
        long ans = x[0];
        for (long a : x) {
            ans = Math.min(a, ans);
        }
        return ans;
    }

    public long move(long x1, long x2, long l, long r) {
        return min(x1 - l, r - x1, x2 - l, r - x2) + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.readInt();
        Radix radix = new Radix(3);
        for (int i = 0; i < q; i++) {
            long a = in.readLong() - 1;
            long b = in.readLong() - 1;
            long c = in.readLong() - 1;
            long d = in.readLong() - 1;
            long dist = Math.abs(a - c) + Math.abs(b - d);
            long d1 = -1;
            long d2 = -1;
            for (int j = 30; j >= 0; j--) {
                long size = radix.valueOfBit(j);
                if (Math.abs(a / size - c / size) >= 2 && (b % (3 * size)) / size == 1 && (d % (3 * size)) / size == 1) {
                    d1 = dist + 2 * move(b % (size * 3), d % (size * 3), size, size * 2 - 1);
                    break;
                }
            }
            {
                long tmp = a;
                a = b;
                b = tmp;
            }
            {
                long tmp = c;
                c = d;
                d = tmp;
            }
            for (int j = 30; j >= 0; j--) {
                long size = radix.valueOfBit(j);
                if (Math.abs(a / size - c / size) >= 2 && b / size / 3 == 1 && d / size / 3 == 1) {
                    d2 = dist + 2 * move(b % (size * 3), d % (size * 3), size, size * 2 - 1);
                    break;
                }
            }

            debug.debug("d1", d1);
            debug.debug("d2", d2);
            if (d1 != -1 || d2 != -1) {
                if (d1 >= 0 && d2 >= 0) {
                    throw new RuntimeException();
                }
                out.println(Math.max(d1, d2));
            } else {
                out.println(dist);
            }
        }
    }
}
