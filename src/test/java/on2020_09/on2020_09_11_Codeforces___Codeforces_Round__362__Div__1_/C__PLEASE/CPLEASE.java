package on2020_09.on2020_09_11_Codeforces___Codeforces_Round__362__Div__1_.C__PLEASE;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.utils.Buffer;
import template.utils.Debug;

public class CPLEASE {
    int mod = (int) (1e9 + 7);
    Modular modular = new Modular(mod);
    Power pow = new Power(modular);

    Buffer<long[][]> buf = new Buffer<>(() -> new long[2][2], x -> {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                x[i][j] = 0;
            }
        }
    });

    public long[][] mul(long[][] a, long[][] b) {
        long[][] c = buf.alloc();
        c[0][0] = (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % mod;
        c[0][1] = (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % mod;
        c[1][0] = (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % mod;
        c[1][1] = (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % mod;
        return c;
    }

    public long[][] pow(long[][] a, long k) {
        if (k == 0) {
            long[][] ans = buf.alloc();
            for (int i = 0; i < 2; i++) {
                ans[i][i] = 1;
            }
            return ans;
        }
        long[][] y = pow(a, k / 2);
        long[][] y2 = mul(y, y);
        buf.release(y);
        if (k % 2 == 1) {
            y = y2;
            y2 = mul(y, a);
            buf.release(y);
        }
        return y2;
    }

    public void normalize(long[][] c) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                c[i][j] %= mod;
            }
        }
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long[][] transfrom = buf.alloc();
        transfrom[0][0] = 1;
        transfrom[0][1] = 1;

        transfrom[1][0] = 2;
        transfrom[1][1] = 0;

        int k = in.readInt();
        long botSum = 1;
        int powMod = mod - 1;
        for (int i = 0; i < k; i++) {
            long a = in.readLong();
            long[][] next = pow(transfrom, a);
            buf.release(transfrom);
            transfrom = next;
            botSum = botSum * (a % powMod) % powMod;
        }

        long[][] init = buf.alloc();
        init[1][0] = 1;

        long[][] res = mul(transfrom, init);
        long ans = res[1][0];

        long bot = pow.pow(2, botSum);
        ans = ans * pow.inverse(2) % mod;
        bot = bot * pow.inverse(2) % mod;
        out.append(ans).append('/').append(bot);
    }


}
