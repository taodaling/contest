package on2019_11.on2019_11_10_Codeforces_Round__591__Div__1__based_on_Technocup_2020_Elimination_Round_1_.A___Save_the_Nature;



import template.*;

import java.util.Arrays;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] p = new long[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() / 100;
        }
        Randomized.randomizedArray(p, 0, n);
        Arrays.sort(p);
        SequenceUtils.reverse(p, 0, n);
        PreSum ps = new PreSum(p);
        int x = in.readInt();
        int a = in.readInt();
        int y = in.readInt();
        int b = in.readInt();
        long k = in.readLong();

        int l = 1;
        int r = n;
        while (l < r) {
            int m = (l + r) / 2;
            if (check(m, ps, x, a, y, b) >= k) {
                r = m;
            } else {
                l = m + 1;
            }
        }

        if (check(l, ps, x, a, y, b) >= k) {
            out.println(l);
        } else {
            out.println(-1);
        }
    }

    public long check(int k, PreSum p, int x, int a, int y, int b) {
        if (x <= y) {
            int tmp = x;
            x = y;
            y = tmp;
            tmp = a;
            a = b;
            b = tmp;
        }

        int t1 = 0;
        int t2 = 0;
        int t3 = 0;
        for (int i = 1; i <= k; i++) {
            if (i % a == 0 && i % b == 0) {
                t1++;
            } else if (i % a == 0) {
                t2++;
            } else if (i % b == 0) {
                t3++;
            }
        }

        long ans1 = p.intervalSum(0, t1 - 1) * (x + y);
        long ans2 = p.intervalSum(t1, t1 + t2 - 1) * x;
        long ans3 = p.intervalSum(t1 + t2, t1 + t2 + t3 - 1) * y;
        return ans1 + ans2 + ans3;
    }
}
