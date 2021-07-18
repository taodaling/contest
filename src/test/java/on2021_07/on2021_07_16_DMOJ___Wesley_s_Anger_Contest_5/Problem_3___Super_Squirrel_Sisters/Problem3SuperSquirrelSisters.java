package on2021_07.on2021_07_16_DMOJ___Wesley_s_Anger_Contest_5.Problem_3___Super_Squirrel_Sisters;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Problem3SuperSquirrelSisters {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        cnts = new int[n + 1];
        int ans = 0;
        for (int each = 1; each * each <= n; each++) {
            int size = each * each;
            init(each);
            for (int j = 0; j < n; j++) {
                add(a[j], 1);
                if (j - size >= 0) {
                    add(a[j - size], -1);
                }
                if (j >= size - 1 && reach == each) {
                    ans++;
                }
            }
        }
        out.println(ans);
    }

    int[] cnts;
    int target;
    int reach;

    void init(int t) {
        target = t;
        reach = 0;
        Arrays.fill(cnts, 0);
    }

    void add(int x, int v) {
        if (cnts[x] >= target) {
            reach--;
        }
        cnts[x] += v;
        if (cnts[x] >= target) {
            reach++;
        }
    }
}
