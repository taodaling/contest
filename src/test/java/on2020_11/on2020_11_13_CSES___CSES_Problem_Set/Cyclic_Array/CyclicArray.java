package on2020_11.on2020_11_13_CSES___CSES_Problem_Set.Cyclic_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class CyclicArray {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        int[] a = new int[n * 2];
        for (int i = 0; i < n; i++) {
            a[i] = a[i + n] = in.readInt();
        }
        debug.debug("a", a);
        int log = 20;
        int[][] jump = new int[n * 2][log];
        long ps = 0;
        for (int i = 0, l = -1; i < 2 * n; i++) {
            ps += a[i];
            while (ps > k) {
                ps -= a[l + 1];
                l++;
            }
            jump[i][0] = l;
        }
        for (int i = 0; i + 1 < log; i++) {
            for (int j = 0; j < n * 2; j++) {
                jump[j][i + 1] = jump[j][i] == -1 ? -1 : jump[jump[j][i]][i];
            }
        }

        int best = (int)1e9;
        for (int i = n; i < 2 * n; i++) {
            int dst = i - n;
            int cur = i;
            int ans = 0;
            for (int j = log - 1; j >= 0; j--) {
                if (jump[cur][j] <= dst) {
                    continue;
                }
                cur = jump[cur][j];
                ans += 1 << j;
            }
            ans++;
            best = Math.min(best, ans);
        }
        out.println(best);
    }
}
