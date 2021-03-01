package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ASearchingLocalMinimum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        height = new int[n];
        Arrays.fill(height, -1);
        this.in = in;
        this.out = out;

        int l = 0;
        int r = n;
        while (true) {
            int m = (l + r) / 2;
            if (query(m - 1) > query(m) && query(m + 1) > query(m)) {
                out.printf("! %d", m + 1).flush();
                return;
            }
            if (query(m - 1) > query(m) && query(m) > query(m + 1)) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }

    }

    int[] height;
    FastOutput out;
    FastInput in;

    public int query(int i) {
        if (i < 0 || i >= height.length) {
            return (int) 1e9;
        }
        if (height[i] != -1) {
            return height[i];
        }
        out.printf("? %d\n", i + 1).flush();
        return height[i] = in.ri();
    }
}
