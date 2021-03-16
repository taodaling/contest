package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class CRidingInALift {
    int mod = (int) 1e9 + 7;

    void handleLazy(long[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = DigitUtils.modWithoutDivision(data[i], mod);
        }
        for (int i = 1; i < data.length; i++) {
            data[i] += data[i - 1];
            if (data[i] >= mod) {
                data[i] -= mod;
            }
        }
    }

    void addTag(long[] data, int l, int r, long x) {
        if (l > r) {
            return;
        }
        data[l] += x;
        data[r + 1] -= x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int k = in.ri();
        int m;
        int init;
        if (a < b) {
            m = b - 2;
            init = b - a - 1;
        } else {
            m = n - b - 1;
            init = a - b - 1;
        }
        long[] prev = new long[m + 2];
        prev[init] = 1;
        long[] next = new long[m + 2];
        for (int r = 0; r < k; r++) {
            Arrays.fill(next, 0);
            for (int i = 0; i <= m; i++) {
                addTag(next, 0, Math.min(i + i, m), prev[i]);
                addTag(next, i, i, -prev[i]);
            }
            handleLazy(next);
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = 0;
        for(int i = 0; i <= m; i++){
            ans += prev[i];
        }
        ans %= mod;
        out.println(ans);
    }
}
