package on2020_06.on2020_06_16_Codeforces___VK_Cup_2017___Round_2.D__Varying_Kibibits;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.IntRadix;
import template.math.Modular;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DVaryingKibibits {
    Modular mod = new Modular(1e9 + 7);
    int limit = (int) 1e6;
    int digit = 6;
    int[][] sum = new int[7][limit];
    int[][] cnt = new int[7][limit];
    int[][] prod = new int[7][limit];
    int[] occur = new int[limit];
    CachedPow pow = new CachedPow(2, mod);


    public int cnt(int i, int j) {
        if (cnt[i][j] == -1) {
            if (i == digit) {
                return (int) (cnt[i][j] = occur[j]);
            }
            cnt[i][j] = 0;
            int cur = radix.get(j, i);
            if (cur < 9) {
                cnt[i][j] += cnt(i, (int) radix.set(j, i, cur + 1));
            }
            cnt[i][j] += cnt(i + 1, j);
            cnt[i][j] = mod.valueOf(cnt[i][j]);
        }
        return (int) cnt[i][j];
    }

    public int prod(int i, int j) {
        if (prod[i][j] == -1) {
            if (i == digit) {
                int ans = mod.valueOf((long)(1 + occur[j]) * occur[j] / 2);
                ans = mod.mul(ans, j);
                ans = mod.mul(ans, j);
                return prod[i][j] = ans;
            }
            prod[i][j] = 0;
            int sum = 0;
            int cur = radix.get(j, i);
            if (cur < 9) {
                prod[i][j] = mod.plus(prod[i][j], prod(i, (int) radix.set(j, i, cur + 1)));
                sum = mod.plus(sum, sum(i, (int) radix.set(j, i, cur + 1)));
            }
            prod[i][j] = mod.plus(prod[i][j], prod(i + 1, j));
            prod[i][j] = mod.plus(prod[i][j], mod.mul(sum, sum(i + 1, j)));
        }
        return prod[i][j];
    }

    IntRadix radix = new IntRadix(10);

    public int sum(int i, int j) {
        if (sum[i][j] == -1) {
            if (i == digit) {
                return sum[i][j] = mod.mul(occur[j], j);
            }
            sum[i][j] = 0;
            int cur = radix.get(j, i);
            if (cur < 9) {
                sum[i][j] = mod.plus(sum[i][j], sum(i, (int) radix.set(j, i, cur + 1)));
            }
            sum[i][j] = mod.plus(sum[i][j], sum(i + 1, j));
        }
        return sum[i][j];
    }

    int[] way = new int[limit];

    public int ie(int i, int j, int cnt) {
        if (i == digit) {
            if (cnt % 2 == 1) {
                return mod.valueOf(-way[j]);
            }
            return way[j];
        }
        int cur = radix.get(j, i);
        int ans = ie(i + 1, j, cnt);
        if (cur < 9) {
            ans = mod.plus(ans, ie(i + 1, (int) radix.set(j, i, cur + 1), cnt + 1));
        }
        return ans;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        SequenceUtils.deepFill(sum, -1);
        SequenceUtils.deepFill(cnt, -1);
        SequenceUtils.deepFill(prod, -1);

        for (int i = 0; i < n; i++) {
            occur[in.readInt()]++;
        }

        debug.elapse("init");
        for (int i = 0; i < limit; i++) {
            int c = cnt(0, i);
            int p = prod(0, i);
            if (c == 0) {
                continue;
            }
            way[i] = mod.mul(p, pow.pow(c - 1));
        }

        debug.elapse("dp");
        //inclusion-exclusion
        int[] ie = new int[limit];
        for (int i = 0; i < limit; i++) {
            ie[i] = ie(0, i, 0);
        }

        debug.elapse("ie");
        long xor = 0;
        for(int i = 0; i < limit; i++){
            long val = (long)i * ie[i];
            xor ^= val;
        }

        out.println(xor);
    }
}
