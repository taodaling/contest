package on2021_04.on2021_04_12_Codeforces___Codeforces_Round__210__Div__1_.A__Levko_and_Array_Recovery;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongBinaryFunction;

import java.util.Arrays;

public class ALevkoAndArrayRecovery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] ops = new int[m][4];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 4; j++) {
                ops[i][j] = in.ri();
            }
        }
        long[] data = new long[n];
        Arrays.fill(data, (long) 1e18);
        for (int i = m - 1; i >= 0; i--) {
            int t = ops[i][0];
            int l = ops[i][1] - 1;
            int r = ops[i][2] - 1;
            int x = ops[i][3];
            if (t == 1) {
                update(data, (a, b) -> a - b, x, l, r);
            } else {
                update(data, Math::min, x, l, r);
            }
        }
        update(data, Math::min, (int) 1e9, 0, n - 1);
        update(data, Math::max, (int) -1e9, 0, n - 1);
        long[] clone = data.clone();
        for (int i = 0; i < m; i++) {
            int t = ops[i][0];
            int l = ops[i][1] - 1;
            int r = ops[i][2] - 1;
            int x = ops[i][3];
            if (t == 1) {
                update(data, (a, b) -> a + b, x, l, r);
            } else {
                if (query(data, l, r) != x) {
                    out.println("NO");
                    return;
                }
            }
        }
        out.println("YES");
        for (long x : clone) {
            out.append(x).append(' ');
        }
    }

    public long query(long[] data, int l, int r) {
        long ans = data[l];
        for (int i = l; i <= r; i++) {
            ans = Math.max(ans, data[i]);
        }
        return ans;
    }

    public void update(long[] data, LongBinaryFunction merger, long x, int l, int r) {
        for (int i = l; i <= r; i++) {
            data[i] = merger.apply(data[i], x);
        }
    }
}
