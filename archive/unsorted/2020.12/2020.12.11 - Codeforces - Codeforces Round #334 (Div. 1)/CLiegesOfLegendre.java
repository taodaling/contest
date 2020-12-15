package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

public class CLiegesOfLegendre {
    public int sg(int x) {
        if (x < 4) {
            return x % 2;
        }
        if (x == 4) {
            return 2;
        }
        if (x % 2 == 1) {
            return 0;
        }
        int t = sg(x / 2);
        if (t != 1) {
            return 1;
        }
        return 2;
    }

    public int sg2(int x) {
        if (x <= 1) {
            return x;
        }
        if (x % 2 == 1) {
            return 0;
        }
        int next = sg2(x - 1);
        if (next != 1) {
            return 1;
        }
        return 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = new int[n];
        in.populate(a);
        if (k % 2 == 0) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                int sg = sg2(a[i]);
                sum ^= sg;
            }
            out.println(sum == 0 ? "Nicky" : "Kevin");
            return;
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int sg = sg(a[i]);
            sum ^= sg;
        }
        out.println(sum == 0 ? "Nicky" : "Kevin");
    }
}
