package contest;

import template.datastructure.DSU;
import template.io.FastInput;

import java.io.PrintWriter;

public class DArtsemAndSaunders {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] f = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            f[i] = in.readInt();
        }
        int m = 0;
        for (int i = 1; i <= n; i++) {
            if (f[f[i]] != f[i]) {
                out.println(-1);
                return;
            }
            if (f[i] == i) {
                m++;
            }
        }

        boolean[] handled = new boolean[n + 1];
        int cur = 1;
        int[] g = new int[n + 1];
        int[] h = new int[m + 1];
        for (int i = 1; i <= n; i++) {
            if (handled[f[i]]) {
                continue;
            }
            handled[f[i]] = true;
            g[f[i]] = cur++;
        }
        for (int i = 1; i <= n; i++) {
            g[i] = g[f[i]];
        }
        for (int i = 1; i <= n; i++) {
            h[g[i]] = f[i];
        }
        boolean valid = true;
        for (int i = 1; i <= m; i++) {
            if (g[h[i]] != i) {
                valid = false;
            }
        }
        for (int i = 1; i <= n; i++) {
            if (h[g[i]] != f[i]) {
                valid = false;
            }
        }
        if (!valid) {
            out.println(-1);
            return;
        }
        out.println(m);
        for(int i = 1; i <= n; i++){
            out.print(g[i]);
            out.append(' ');
        }
        out.println();
        for(int i = 1; i <= m; i++){
            out.print(h[i]);
            out.append(' ');
        }
    }
}
