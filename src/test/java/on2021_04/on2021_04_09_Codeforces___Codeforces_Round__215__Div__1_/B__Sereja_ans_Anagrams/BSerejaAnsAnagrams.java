package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__215__Div__1_.B__Sereja_ans_Anagrams;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.MultiSetHasher;
import template.rand.SparseMultiSetHasher;
import template.string.KMPAutomaton;

public class BSerejaAnsAnagrams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int p = in.ri();
        long[] a = in.rl(n);
        long[] b = in.rl(m);
        int leftShift = (int) Math.min((long) m * p, n + 1);
        MultiSetHasher hasher = new SparseMultiSetHasher(n + m);
        for (int i = 0; i < n; i++) {
            a[i] = hasher.hash(a[i]);
        }
        long target = 0;
        for (int i = 0; i < m; i++) {
            b[i] = hasher.hash(b[i]);
            target = hasher.merge(target, b[i]);
        }
        int ans = 0;
        boolean[] ok = new boolean[n];
        for (int i = 0; i < p && i < n; i++) {
            long sum = 0;
            for (int j = i; j < n; j += p) {
                sum = hasher.merge(sum, a[j]);
                if (j >= leftShift) {
                    sum = hasher.remove(sum, a[j - leftShift]);
                }
                if (sum == target) {
                    ok[j - p * (m - 1)] = true;
                    ans++;
                }
            }
        }

        out.println(ans);
        for (int i = 0; i < n; i++) {
            if (ok[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
