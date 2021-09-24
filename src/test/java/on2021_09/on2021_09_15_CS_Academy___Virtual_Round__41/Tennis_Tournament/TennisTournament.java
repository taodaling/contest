package on2021_09.on2021_09_15_CS_Academy___Virtual_Round__41.Tennis_Tournament;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class TennisTournament {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri() - 1;
        int m = in.ri();
        ans = new int[1 << n];
        Arrays.fill(ans, -1);
        used = new boolean[1 << n];
        L = 0;
        R = (1 << n) - 1;
        set(0, k);
        int l = 0;
        int r = (1 << m) - 1;
        for (int i = l; i <= r; i++) {
            if (ans[i] == -1) {
                set(i, popFront());
            }
        }
        l = 0;
        r = (1 << m + 1) - 1;
        for (int i = l; i <= r && i < 1 << n; i++) {
            if (ans[i] == -1) {
                set(i, popBack());
            }
        }
        for (int i = 0; i < 1 << n; i++) {
            if (ans[i] == -1) {
                set(i, popFront());
            }
        }
        int rank = n;
        for (int i = 0; i < 1 << n; i++) {
            if (ans[i] > ans[0]) {
                rank = Bits.theFirstDifferentIndex(i, 0);
                break;
            }
        }
        debug.debugArray("ans", ans);
        debug.debug("rank", rank);
        if (rank != m) {
            out.println(-1);
            return;
        }
        for (int i = 0; i < 1 << n; i++) {
            out.append(ans[i] + 1).append(' ');
        }
    }

    Debug debug = new Debug(false);

    public void set(int i, int x) {
        assert ans[i] == -1;
        assert !used[x];
        ans[i] = x;
        used[x] = true;
    }

    int[] ans;
    boolean[] used;
    int L;
    int R;

    public int popFront() {
        while (used[L]) {
            L++;
        }
        return L++;
    }

    public int popBack() {
        while (used[R]) {
            R--;
        }
        return R--;
    }
}
