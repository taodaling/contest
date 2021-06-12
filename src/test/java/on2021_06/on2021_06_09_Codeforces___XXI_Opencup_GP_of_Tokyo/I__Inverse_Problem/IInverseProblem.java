package on2021_06.on2021_06_09_Codeforces___XXI_Opencup_GP_of_Tokyo.I__Inverse_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.primitve.generated.datastructure.IntegerPreSum;

public class IInverseProblem {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] x = new int[m];
        for (int i = 0; i < m; i++) {
            x[i] = in.ri() - 1;
        }
        int k = 1;
        while (k < x.length && x[k] > x[k - 1]) {
            k++;
        }
        boolean[] include = new boolean[n];
        for (int t : x) {
            include[t] = true;
        }
        IntegerPreSum ps = new IntegerPreSum(i -> include[i] ? 0 : 1, n);
        long ans = 1;
        int considered = 0;
        for (int i = 0; i + 1 < k; i++) {
            int between = ps.intervalSum(x[i], x[i + 1]);
            int slot = i + 1;
            int cur = slot + considered;
            for (int j = 0; j < between; j++) {
                ans = ans * cur % mod;
                cur++;
            }
            considered += between;
        }
        int between = ps.intervalSum(x[k - 1], n);
        int slot = k + 1;
        int cur = slot + considered;
        for (int j = 0; j < between; j++) {
            ans = ans * cur % mod;
            cur++;
        }
        considered += between;
        if (considered + m != n) {
            out.println(0);
            return;
        }
        out.println(ans);
    }
}
