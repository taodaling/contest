package on2019_11.on2019_11_28_Educational_Codeforces_Round_75__Rated_for_Div__2_.F___Red_White_Fence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Log2;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int limit = 300000;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }

        Log2 log2 = new Log2();

        Modular mod = new Modular(998244353);
        int proper = log2.ceilLog(n);
        Composite comp = new Composite(1 << proper, mod);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);

        int[][] ps = new int[k][];
        int[] bs = new int[k];
        for (int i = 0; i < k; i++) {
            int sCnt = 0;
            int dCnt = 0;
            int b = in.readInt();
            bs[i] = b;
            for (int j = 0; j < b; j++) {
                if (cnts[j] >= 2) {
                    dCnt++;
                } else if (cnts[j] == 1) {
                    sCnt++;
                }
            }

            int[] singleDft = new int[1 << proper];
            int[] doubleDft = new int[1 << proper];
            int twoPower = 1;
            for (int j = 0; j < (1 << proper); j++) {
                singleDft[j] = mod.mul(twoPower, comp.composite(sCnt, j));
                doubleDft[j] = comp.composite(dCnt, j);
                twoPower = mod.mul(twoPower, 2);
            }
            ntt.dft(singleDft, proper);
            ntt.dft(doubleDft, proper);
            ntt.dotMul(doubleDft, doubleDft, doubleDft, proper);
            ntt.dotMul(singleDft, doubleDft, singleDft, proper);
            ntt.idft(singleDft, proper);
            ps[i] = singleDft;
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int qi = in.readInt() / 2 - 1;
            int ans = 0;
            for (int j = 0; j < k; j++) {
                if (bs[j] > qi) {
                    continue;
                }
                int remain = qi - bs[j];
                if (remain < ps[j].length) {
                    ans = mod.plus(ans, ps[j][remain]);
                }
            }
            out.println(ans);
        }
    }
}
