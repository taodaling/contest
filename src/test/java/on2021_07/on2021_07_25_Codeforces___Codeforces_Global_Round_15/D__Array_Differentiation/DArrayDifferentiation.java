package on2021_07.on2021_07_25_Codeforces___Codeforces_Global_Round_15.D__Array_Differentiation;



import template.binary.Bits;
import template.binary.FastBitCount2;
import template.io.FastInput;
import template.io.FastOutput;

public class DArrayDifferentiation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for (int i = 0; i < 1 << n; i++) {
            if (FastBitCount2.count(i) == 0) {
                continue;
            }
            int subset = i + 1;
            while (subset > 0) {
                subset = (subset - 1) & i;
                long ans = 0;
                for (int j = 0; j < n; j++) {
                    if (Bits.get(i, j) == 0) {
                        continue;
                    }
                    if (Bits.get(subset, j) == 1) {
                        ans += a[j];
                    } else {
                        ans -= a[j];
                    }
                }
                if (ans == 0) {
                    out.println("YES");
                    return;
                }
            }
        }
        out.println("NO");
    }
}
