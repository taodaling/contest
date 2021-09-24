package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Cube_Coloring;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CubeColoring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            if (x > 1) {
                cnt[1]++;
            } else {
                cnt[0]++;
            }
        }
        //2 + 2 + 2
        long ans = 0;
        ans += choose(cnt[1], 3);
        //2 + 2 + 1 + 2
        ans += choose(cnt[1], 2) * choose(n - 2, 2);

//        f4 = new long[n + 1];
//        f6 = new long[n + 1];
//        Arrays.fill(f4, -1);
//        Arrays.fill(f6, -1);

        //2 + 1 + 1 + 1 + 1
        ans += cnt[1] * f4(n - 1) / 2;
        //1 + 1 + 1 + 1 + 1 + 1
        ans += f6(n);

        out.println(ans);
    }

//    long[] f4;
//    long[] f6;

    public long f4(int n) {
        return choose(n, 4) * 6;
    }

    public long f6(int n) {
        return choose(n, 6) * 5 * 6;
    }

    public long fact(long n, long m) {
        if (m > n) {
            return 0;
        }
        if (m == 0) {
            return 1;
        }
        return fact(n - 1, m - 1) * n;
    }

    public long choose(long n, long m) {
        if (m > n) {
            return 0;
        }
        if (m == 0) {
            return 1;
        }
        return choose(n - 1, m - 1) * n / m;
    }
}
