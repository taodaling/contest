package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.Dominoes;



import template.io.FastInput;
import template.io.FastOutput;

public class Dominoes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        int best = 0;
        for (int i = 0, r = 0; i < n; i++) {
            while (r + 1 < n && k >= a[r + 1] - a[r] - 1) {
                k -= a[r + 1] - a[r] - 1;
                r++;
            }
            best = Math.max(best, k + a[r] - a[i] + 1);
            if (i + 1 < n) {
                k += a[i + 1] - a[i] - 1;
            }
        }
        out.println(best);
    }
}
