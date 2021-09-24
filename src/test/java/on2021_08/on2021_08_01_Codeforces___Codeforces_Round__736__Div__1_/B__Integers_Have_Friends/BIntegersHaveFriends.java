package on2021_08.on2021_08_01_Codeforces___Codeforces_Round__736__Div__1_.B__Integers_Have_Friends;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongSparseTable;

public class BIntegersHaveFriends {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        long[] d = new long[n - 1];
        for (int i = 0; i < n - 1; i++) {
            d[i] = Math.abs(a[i] - a[i + 1]);
        }
        if (n == 1) {
            out.println(1);
            return;
        }
        LongSparseTable st = new LongSparseTable(n - 1, i -> d[i], GCDs::gcd);
        int max = 0;
        for (int i = 0, r = i - 1; i < n - 1; i++) {
            r = Math.max(i - 1, r);
            while (r + 1 < n - 1 && st.query(i, r + 1) > 1) {
                r++;
            }
            int len = r - i + 1;
            max = Math.max(max, len);
        }
        out.println(max + 1);
    }
}
