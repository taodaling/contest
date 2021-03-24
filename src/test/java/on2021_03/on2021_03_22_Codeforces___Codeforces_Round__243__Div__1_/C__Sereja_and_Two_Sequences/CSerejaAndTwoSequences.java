package on2021_03.on2021_03_22_Codeforces___Codeforces_Round__243__Div__1_.C__Sereja_and_Two_Sequences;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CSerejaAndTwoSequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int s = in.ri();
        int e = in.ri();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        int[] b = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            b[i] = in.ri();
        }
        int[] reg = new int[(int) 1e5 + 1];
        int[] prev = new int[n + 1];
        int[] next = new int[n + 1];
        for (int chance = 1; ; chance++) {
            Arrays.fill(next, m + 1);
            Arrays.fill(reg, m + 1);
            int r = m;
            for (int i = 1; i <= n; i++) {
                int from = prev[i - 1];
                if (from > m) {
                    continue;
                }
                while (r >= from + 1) {
                    reg[b[r]] = r;
                    r--;
                }
                next[i] = reg[a[i]];
            }
            for (int i = 1; i <= n; i++) {
                next[i] = Math.min(next[i], next[i - 1]);
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;

            int minCost = s + 1;
            for (int i = 0; i <= n; i++) {
                if (prev[i] > m) {
                    continue;
                }
                int cand = i + prev[i] + chance * e;
                minCost = Math.min(minCost, cand);
            }
            if (minCost > s) {
                out.println(chance - 1);
                return;
            }
        }
    }
}
