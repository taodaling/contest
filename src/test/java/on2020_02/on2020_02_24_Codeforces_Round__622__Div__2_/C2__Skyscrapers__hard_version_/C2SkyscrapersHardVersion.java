package on2020_02.on2020_02_24_Codeforces_Round__622__Div__2_.C2__Skyscrapers__hard_version_;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class C2SkyscrapersHardVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] m = new long[n];
        for (int i = 0; i < n; i++) {
            m[i] = in.readInt();
        }
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                prev[i] = -1;
                continue;
            }
            prev[i] = i - 1;
            while (prev[i] != -1 && m[prev[i]] > m[i]) {
                prev[i] = prev[prev[i]];
            }
        }
        int[] post = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                post[i] = n;
                continue;
            }
            post[i] = i + 1;
            while (post[i] != n && m[post[i]] > m[i]) {
                post[i] = post[post[i]];
            }
        }
        long[] preSum = new long[n];
        long[] postSum = new long[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                preSum[i] = m[i];
                continue;
            }
            preSum[i] = (i - prev[i]) * m[i] + get(preSum, prev[i]);
        }
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                postSum[i] = m[i];
                continue;
            }
            postSum[i] = (post[i] - i) * m[i] + get(postSum, post[i]);
        }

        int index = 0;
        for (int i = 0; i < n; i++) {
            if (preSum[i] + postSum[i] - m[i] >
                    preSum[index] + postSum[index] - m[index]) {
                index = i;
            }
        }

//        System.err.println(Arrays.toString(prev));
//        System.err.println(Arrays.toString(post));
//        System.err.println(Arrays.toString(preSum));
//        System.err.println(Arrays.toString(postSum));

        long[] ans = new long[n];
        ans[index] = m[index];
        for (int i = index - 1; i >= 0; i--) {
            ans[i] = Math.min(m[i], ans[i + 1]);
        }
        for (int i = index + 1; i < n; i++) {
            ans[i] = Math.min(m[i], ans[i - 1]);
        }
        for (long x : ans) {
            out.append(x).append(' ');
        }
    }

    public long get(long[] x, int i) {
        if (i < 0 || i >= x.length) {
            return 0;
        }
        return x[i];
    }
}
