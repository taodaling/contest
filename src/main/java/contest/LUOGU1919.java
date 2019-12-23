package contest;

import numeric.FFT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedLog2;
import template.math.DigitUtils;
import template.polynomial.FastFourierTransform;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class LUOGU1919 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double[][] a = new double[2][1 << 21];
        double[][] b = new double[2][1 << 21];

        int n = in.readString(a[0], 0);
        for (int i = 0; i < n; i++) {
            a[0][i] -= '0';
        }
        SequenceUtils.reverse(a[0], 0, n - 1);

        int m = in.readString(b[0], 0);
        for (int i = 0; i < m; i++) {
            b[0][i] -= '0';
        }
        SequenceUtils.reverse(b[0], 0, m - 1);

        int len = n + m - 1;
        int proper = CachedLog2.ceilLog(len);
        double[][] c = new double[2][1 << 21];

        FastFourierTransform.dft(a, proper);
        FastFourierTransform.dft(b, proper);
        FastFourierTransform.dotMul(a, b, c, proper);
        FastFourierTransform.idft(c, proper);

        int[] ans = new int[1 << 21];
        int remain = 0;
        for (int i = 0; i < ans.length; i++) {
            remain += DigitUtils.roundToInt(c[0][i]);
            ans[i] = remain % 10;
            remain /= 10;
        }
        boolean flag = false;
        for(int i = ans.length - 1; i >= 0; i--){
            if(!flag && ans[i] > 0){
                flag = true;
            }
            if(flag) {
                out.append(ans[i]);
            }
        }
    }
}
