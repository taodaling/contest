package on2021_01.on2021_01_23_Luogu.P1638____;



import template.io.FastInput;
import template.io.FastOutput;

public class P1638 {
    int[] cnts;
    int distinct = 0;

    public void add(int c, int x) {
        if (cnts[c] > 0) {
            distinct--;
        }
        cnts[c] += x;
        if (cnts[c] > 0) {
            distinct++;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        cnts = new int[m + 1];
        distinct = 0;
        int[] a = in.ri(n);
        int best = (int) 1e9;
        int bestL = -1;
        for (int i = 0, r = -1; i < n; i++) {
            while (distinct < m && r + 1 < n) {
                r++;
                add(a[r], 1);
            }
            if (distinct == m) {
                int cand = r - i + 1;
                if (cand < best) {
                    best = cand;
                    bestL = i;
                }
            }
            add(a[i], -1);
        }
        out.append(bestL + 1).append(' ').append(best + bestL);
    }
}
