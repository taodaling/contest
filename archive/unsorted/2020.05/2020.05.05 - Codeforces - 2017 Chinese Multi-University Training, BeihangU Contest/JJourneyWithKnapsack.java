package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.PentagonalNumber;
import template.polynomial.FastFourierTransform;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.Arrays;

public class JJourneyWithKnapsack {
    Debug debug = new Debug(false);

    Modular mod = new Modular(1e9 + 7);
    int limitDp = 100000;
    int[] prev = new int[limitDp + 1];
    int[] cur = new int[limitDp + 1];
    int[] pent = new int[1 << 17];
    int[] invPent = new int[1 << 17];

    {
        PentagonalNumber.getPolynomial(pent, limitDp, mod);
        Polynomials.inverse(pent, invPent, 17, mod);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);

        int n = 2 * in.readInt();
        int m = in.readInt();

        int[] a = new int[n + 1];
        for (int i = 1; i <= n / 2; i++) {
            a[i] = in.readInt();
        }
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }

        if (n / 2 <= 1000) {
            //just dp
            Arrays.fill(prev, 0, n + 1, 0);
            prev[0] = 1;
            for (int i = 1; i <= n / 2; i++) {
                int limit = Math.min(i * a[i], (int) 1e9);
                for (int k = 0; k < i; k++) {
                    int sum = 0;

                    for (int j = k; j <= n; j += i) {
                        sum = mod.plus(sum, prev[j]);
                        cur[j] = sum;
                        if (j >= limit) {
                            sum = mod.subtract(sum, prev[j - limit]);
                        }
                    }
                }

                int[] tmp = cur;
                cur = prev;
                prev = tmp;
            }

            int ans = 0;
            for (int x : b) {
                ans = mod.plus(ans, prev[n - x]);
            }

            out.println(ans);
            return;
        }


        int xMod = n + 1;

        IntegerList less = new IntegerList(n);
        for (int i = 1; i <= n / 2; i++) {
            if ((long) (a[i] + 1) * i >= xMod) {
                continue;
            }
            less.add((a[i] + 1) * i);
        }

        debug.debug("less", less);

        Arrays.fill(prev, 0, xMod, 0);
        prev[0] = 1;
        for (int i = n / 2 + 1; i <= n; i++) {
            prev[i] = mod.valueOf(-1);
        }
        for (int i = 1; i <= less.size(); i++) {
            int val = less.get(i - 1);
            for (int j = 0; j < xMod; j++) {
                cur[j] = prev[j];
                if (j >= val) {
                    cur[j] = mod.subtract(cur[j], prev[j - val]);
                }
            }
            int[] tmp = cur;
            cur = prev;
            prev = tmp;
        }

        int proper = Log2.ceilLog(xMod) + 1;
        IntegerList pentagonal = new IntegerList(xMod);
        PentagonalNumber.getPolynomial(pentagonal, xMod);


//        debug.debug("pent", pent);
//        FastFourierTransform.dft(inv, proper);
//        FastFourierTransform.dft(pent, proper);
//        FastFourierTransform.dotMul(inv, pent, buf, proper);
//        FastFourierTransform.idft(inv, proper);
//        FastFourierTransform.idft(pent, proper);
//        FastFourierTransform.idft(buf, proper);
//        debug.debug("buf", buf);

        int[] up = prev;
        int[] bot = invPent;

        int[] mul = FastFourierTransform.multiplyMod(up, xMod, bot, xMod, mod.getMod());

        int ans = 0;
        for (int x : b) {
            ans = mod.plus(ans, mul[n - x]);
        }

        debug.debug("mul", mul);
        debug.debug("up", up);
        debug.debug("bot", bot);

        out.println(ans);
    }
}
