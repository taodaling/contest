package on2021_03.on2021_03_25_Codeforces___Codeforces_Round__236__Div__1_.B__Upgrading_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.PollardRho;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashSet;

import java.util.Set;

public class BUpgradingArray {
    IntegerHashSet isBad;

    public int sum(int x) {
        Set<Integer> set = PollardRho.findAllFactors(x);
        int ans = 0;
        for (int factor : set) {
            int val = isBad.contain(factor) ? -1 : 1;
            while (x % factor == 0) {
                x /= factor;
                ans += val;
            }
        }
        return ans;
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(m);
        isBad = new IntegerHashSet(m, false);
        for (int x : b) {
            isBad.add(x);
        }
        int[] pregcd = new int[n];
        int last = 0;
        for (int i = 0; i < n; i++) {
            last = pregcd[i] = GCDs.gcd(last, a[i]);
        }
        long ans = 0;
        int div = 1;
        for (int i = n - 1; i >= 0; i--) {
            int profit = sum(pregcd[i] / div);
            if (profit < 0) {
                div = pregcd[i];
            }
            ans += sum(a[i] / div);
        }
        out.println(ans);
    }
}
