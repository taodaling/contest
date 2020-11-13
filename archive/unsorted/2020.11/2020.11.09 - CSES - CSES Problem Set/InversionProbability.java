package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class InversionProbability {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        double exp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int y = a[i];
                int x = a[j];
                long way = 0;
                if (x <= y) {
                    //1 + 2 + ... + x - 1
                    way += IntMath.sumOfInterval(1, x - 1);
                } else {
                    //1 + 2 + ... + y + y + ... + y
                    way += IntMath.sumOfInterval(1, y - 1) + (x - y - 1 + 1) * y;
                }
                double prob = (double) way / (x * y);
                exp += prob;
            }
        }

        out.printf("%.6f", exp);
    }
}
