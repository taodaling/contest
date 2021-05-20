package on2021_05.on2021_05_17_AtCoder___AtCoder_Beginner_Contest_196.F___Substring_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;
import template.primitve.generated.datastructure.IntegerPreSum;

public class FSubstring2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 1e6;
        char[] s = new char[limit];
        char[] t = new char[limit];
        int n = in.rs(s);
        int m = in.rs(t);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        for (int i = 0; i < m; i++) {
            t[i] -= '0';
        }
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = s[i];
        }
        double[] b = new double[m];
        for (int i = 0; i < m; i++) {
            b[i] = t[i];
        }
        double[] delta = FastFourierTransform.deltaConvolution(a, b);
        IntegerPreSum psA = new IntegerPreSum(i -> s[i], n);
        IntegerPreSum psB = new IntegerPreSum(i -> t[i], m);

        long ans = m;
        for (int i = 0; i + m <= n; i++) {
            long d = 0;
            d += psA.intervalSum(i, i + m - 1);
            d += psB.post(0);
            d -= 2 * Math.round(delta[i]);
            ans = Math.min(ans, d);
        }

        out.println(ans);
    }
}
