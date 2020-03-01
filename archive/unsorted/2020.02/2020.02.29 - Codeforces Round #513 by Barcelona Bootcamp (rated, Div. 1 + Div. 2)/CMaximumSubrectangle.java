package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Arrays;

public class CMaximumSubrectangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] a = new long[n];
        long[] b = new long[m];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }
        long x = in.readInt();
        LongPreSum pa = new LongPreSum(a);
        LongPreSum pb = new LongPreSum(b);
        long[] minA = new long[n + 1];
        long[] minB = new long[m + 1];
        Arrays.fill(minA, (long) 1e10);
        Arrays.fill(minB, (long) 1e10);
        minA[0] = minB[0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int len = j - i + 1;
                minA[len] = Math.min(minA[len], pa.intervalSum(i, j));
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                int len = j - i + 1;
                minB[len] = Math.min(minB[len], pb.intervalSum(i, j));
            }
        }

        //System.err.println(Arrays.toString(minA));
        //System.err.println(Arrays.toString(minB));
        long area = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (DigitUtils.isMultiplicationOverflow(minA[i], minB[j], x)) {
                    continue;
                }
                area = Math.max(area, i * j);
            }
        }
        out.println(area);
    }
}
