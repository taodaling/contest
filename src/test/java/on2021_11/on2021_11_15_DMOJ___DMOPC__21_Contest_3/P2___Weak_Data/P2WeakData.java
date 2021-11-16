package on2021_11.on2021_11_15_DMOJ___DMOPC__21_Contest_3.P2___Weak_Data;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;

public class P2WeakData {
    static LongHashMap set = new LongHashMap((int) 2e6, false);
    static int N = (int) 1e6;

    static {
        for (int i = N; i >= 0; i--) {
            set.put((long) i * (i - 1) / 2, i);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long k = in.rl();
        int x = -1;
        int y = -1;
        if (k == 0) {
            out.println(1);
            out.println(1);
            return;
        }
        for (int i = 1; i <= N + 1; i++) {
            long cur = (long) i * (i - 1) / 2;
            if (set.containKey(k - cur)) {
                int cand = (int) set.get(k - cur);
                if (cand + i <= N + 1 && cand + i >= 2) {
                    x = i;
                    y = cand;
                    break;
                }
            }
        }

        if (x == -1) {
            assert false;
            out.println(-1);
            return;
        }
        x--;
        long[] ans = new long[x + y];
        for (int i = 0; i < x; i++) {
            ans[i] = 0;
        }
        for (int i = 0; i < y; i++) {
            ans[i + x] = 1;
        }
        for (int i = x + y - 1; i > 0; i--) {
            ans[i] -= ans[i - 1];
        }
        out.println(ans.length);
        for (int i = 0; i < ans.length; i++) {
            out.append(ans[i]);
            if (i + 1 < ans.length) {
                out.append(' ');
            }
        }
        out.println();

        assert check(ans) == k;
    }

    public static long check(long[] a) {
        System.err.println("check");
        int[] cnt = new int[2];
        cnt[0] = 1;
        long ans = 0;
        long ps = 0;
        for (long x : a) {
            ps += x;
            ans += cnt[(int) (ps & 1)];
            cnt[(int) (ps & 1)]++;
        }
        return ans;
    }
}
