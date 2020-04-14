package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.FastFourierTransform;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class ENikitaAndOrderStatistics {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt() < x ? 1 : 0;
        }
        int[] prefixCnt = new int[n + 1];
        prefixCnt[0]++;
        long[] ans = new long[n + 1];
        IntegerPreSum ps = new IntegerPreSum(a);
        for (int i = 0; i < n; i++) {
            int prefix = ps.prefix(i);
            ans[0] += prefixCnt[prefix];
            prefixCnt[prefix]++;
        }

        debug.debug("prefixCnt", prefixCnt);
        int proper = Log2.ceilLog(n + n + 1);
        double[][] p1 = new double[2][1 << proper];
        double[][] p2 = new double[2][1 << proper];
        for (int i = 0; i <= n; i++) {
            p1[0][i] = prefixCnt[i];
            p2[0][i] = prefixCnt[i];
        }
        SequenceUtils.reverse(p1[0], 0, n);
        FastFourierTransform.dft(p1, proper);
        FastFourierTransform.dft(p2, proper);
        FastFourierTransform.dotMul(p1, p2, p1, proper);
        FastFourierTransform.idft(p1, proper);
        SequenceUtils.reverse(p1[0], 0, n);
        SequenceUtils.reverse(p1[1], 0, n);

        out.append(ans[0]).append(' ');
        for (int i = 1; i <= n; i++) {
            long val = DigitUtils.round(p1[0][i]);
            out.append(val).append(' ');
        }
    }
}
