package on2020_01.on2020_01_03_Educational_Codeforces_Round_50__Rated_for_Div__2_.D__Vasya_and_Arrays;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.LongPreSum;

public class DVasyaAndArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int m = in.readInt();
        long[] b = new long[m];
        for (int j = 0; j < m; j++) {
            b[j] = in.readInt();
        }

        int ans = solve(a, 0, 0, b, 0, 0);
        if (ans >= 1e8) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }

    public int solve(long[] a, int i, long pa, long[] b, int j, long pb) {
        if (i == a.length && j == b.length) {
            if (pa != 0 || pb != 0) {
                return (int) 1e8;
            }
            return 0;
        }
        if (pa <= pb && i < a.length) {
            pa += a[i++];
        } else if (j < b.length) {
            pb += b[j++];
        } else {
            return (int) 1e8;
        }
        int ans = 0;
        if (pa == pb) {
            ans++;
            pa = pb = 0;
        }
        ans += solve(a, i, pa, b, j, pb);
        return ans;
    }
}
