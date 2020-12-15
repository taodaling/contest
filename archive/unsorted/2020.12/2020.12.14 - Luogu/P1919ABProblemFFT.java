package contest;

import numeric.FFT;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class P1919ABProblemFFT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = new int[(int) 1e6];
        int[] b = new int[(int) 1e6];
        int n = in.readString(a, 0);
        int m = in.readString(b, 0);
        for(int i = 0; i < n; i++){
            a[i] -= '0';
        }
        for(int i = 0; i < m; i++){
            b[i] -= '0';
        }
        SequenceUtils.reverse(a, 0, n - 1);
        SequenceUtils.reverse(b, 0, m - 1);
        int[] c = FFT.multiplyBigint(Arrays.copyOf(a, n), Arrays.copyOf(b, m));
        int head = c.length - 1;
        while (head > 0 && c[head] == 0) {
            head--;
        }
        if (head == 0) {
            out.append(0);
            return;
        }
        for (int i = head; i >= 0; i--) {
            out.append(c[i]);
        }
    }
}
