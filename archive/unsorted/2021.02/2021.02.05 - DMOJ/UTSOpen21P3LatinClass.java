package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.Deque;

public class UTSOpen21P3LatinClass {
    int mod = (int) 1e9 + 7;

    public long interval(long[] data, int l, int r) {
        if (r < l) {
            return 0;
        }
        long ans = data[r];
        if (l > 0) {
            ans -= data[l - 1];
        }
        if (ans < 0) {
            ans += mod;
        }
        return ans;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] h = in.ri(n);
        int[] pre = new int[n];
        int[] post = new int[n];
        Deque<Integer> decDq = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            while (!decDq.isEmpty() && h[decDq.peekLast()] <= h[i]) {
                decDq.removeLast();
            }
            pre[i] = decDq.isEmpty() ? 0 : decDq.peekLast() + 1;
            decDq.addLast(i);
        }
        decDq.clear();
        for(int i = n - 1; i >= 0; i--){
            while(!decDq.isEmpty() && h[decDq.peekLast()] < h[i]){
                decDq.removeLast();
            }
            post[i] = decDq.isEmpty() ? n - 1 : decDq.peekLast() - 1;
            decDq.addLast(i);
        }
        long[] f = new long[n];
        long[] g = new long[n];
        long[] d = new long[n];
        long[] psD = new long[n];
        long[] psF = new long[n];
        for (int i = 0; i < n; i++) {
            f[i] += interval(psD, pre[i], i - 1);
            if (pre[i] == 0) {
                f[i]++;
            } else {
                //f[i] += g[pre[i] - 1] * (i - pre[i]);
            }
            f[i] = DigitUtils.mod(f[i], mod);
            g[i] = f[i];
            if (pre[i] == 0) {
                //g[i]++;
            } else {
                g[i] += g[pre[i] - 1];
            }
            g[i] = DigitUtils.mod(g[i], mod);
            d[i] = (post[i] - i + 1) * f[i] % mod;
            psD[i] = d[i];
            psF[i] = f[i];
            if (i > 0) {
                psD[i] = DigitUtils.modplus(psD[i], psD[i - 1], mod);
                psF[i] = DigitUtils.modplus(psF[i], psF[i - 1], mod);
            }
        }

        debug.debug("pre", pre);
        debug.debug("f", f);
        debug.debug("g", g);
        debug.debug("d", d);
        long ans = 0;
        int back = -1;
        for(int i = n - 1; i >= 0; i--){
            if(h[i] > back){
                back = h[i];
                ans += f[i];
            }
        }
        ans %= mod;
        out.println(ans);
    }
}
