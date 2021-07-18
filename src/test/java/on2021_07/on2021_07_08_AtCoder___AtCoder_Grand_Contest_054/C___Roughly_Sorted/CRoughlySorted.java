package on2021_07.on2021_07_08_AtCoder___AtCoder_Grand_Contest_054.C___Roughly_Sorted;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

public class CRoughlySorted {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
        }
        for (int i = 0; i < n; i++) {
            if (i - p[i] > k) {
                out.println(0);
                return;
            }
        }
        long ans = 1;
        IntegerBIT bit = new IntegerBIT(n);
        for (int i = 0; i < n; i++) {
            int before = bit.query(p[i] + 2, n);
            bit.update(p[i] + 1, 1);
            if (before == k) {
                //cool
                int after = n - i - 1;
                //0~after
                ans = ans * (1 + after) % mod;
            }
        }
        out.println(ans);
    }
}
