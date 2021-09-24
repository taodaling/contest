package on2021_07.on2021_07_26_CodeChef___Practice_Easy_.OR_of_ANDs;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class OROfANDs {
    int[] bit = new int[32];

    public void add(int x, int v) {
        for (int i = 0; i < 32; i++) {
            bit[i] += Bits.get(x, i) * v;
        }
    }

    public int query() {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if (bit[i] > 0) {
                ans |= 1 << i;
            }
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        Arrays.fill(bit, 0);
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        for (int x : a) {
            add(x, 1);
        }
        out.println(query());
        for (int i = 0; i < q; i++) {
            int x = in.ri() - 1;
            int v = in.ri();
            add(a[x], -1);
            a[x] = v;
            add(a[x], 1);
            out.println(query());
        }
    }
}
