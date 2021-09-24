package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.F__Sports_Betting;



import template.binary.Bits;
import template.binary.FastBitCount2;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.math.Power;
import template.utils.Debug;

public class FSportsBetting {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long[] I = new long[1 << n];
        long[] invI = new long[1 << n];
        long[][] fp = new long[n][n];
        for (int i = 0; i < n; i++) {
            fp[i][0] = 1;
            for (int j = 1; j < n; j++) {
                fp[i][j] = fp[i][j - 1] * a[i] % mod;
            }
        }
        long[][] subsetfp = new long[n][1 << n];
        for (int j = 0; j < n; j++) {
            subsetfp[j][0] = 1;
        }
        for (int i = 1; i < 1 << n; i++) {
            int lb = Integer.lowestOneBit(i);
            int log = Log2.floorLog(lb);
            for (int j = 0; j < n; j++) {
                subsetfp[j][i] = subsetfp[j][i - lb] * fp[log][j] % mod;
            }
        }
        I[0] = 1;
        invI[0] = 1;
        for (int i = 1; i < 1 << n; i++) {
            int lb = Integer.lowestOneBit(i);
            int log = Log2.floorLog(lb);
            I[i] = I[i - lb];
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0 || j == log) {
                    continue;
                }
                I[i] = I[i] * (a[log] + a[j]) % mod;
            }
            invI[i] = pow.inverse((int) I[i]);
        }
        debug.debug("I", I);
        long[] f = new long[1 << n];
        f[0] = 1;
        for (int i = 1; i < 1 << n; i++) {
            int lb = Integer.lowestOneBit(i);
            if (lb == i) {
                f[i] = 1;
                continue;
            }
            long invalid = 0;
            int subset = i;
            while (subset > 0) {
                subset = (subset - 1) & i;
                if(subset == 0){
                    continue;
                }
                int other = i - subset;
                int remain = FastBitCount2.count(other);
                invalid += f[subset] * subsetfp[remain][subset] % mod
                        * I[other] % mod;
            }
            invalid %= mod;
            f[i] = I[i] - invalid;
            f[i] = DigitUtils.mod(f[i], mod);
        }
        debug.debug("f", f);
        long[] prob = new long[n];
        int mask = (1 << n) - 1;
        for (int i = 1; i < 1 << n; i++) {
            int other = mask - i;
            int remain = FastBitCount2.count(other);
            long p = f[i] * subsetfp[remain][i] % mod * I[other] % mod;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                prob[j] += p;
            }
        }
        debug.debug("prob", prob);
        long ans = 0;
        for(int i = 0; i < n; i++){
            ans += prob[i] % mod;
        }
        ans = ans % mod * invI[mask] % mod;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
