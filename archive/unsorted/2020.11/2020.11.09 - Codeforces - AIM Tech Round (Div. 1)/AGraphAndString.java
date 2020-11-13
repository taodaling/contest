package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Set;

public class AGraphAndString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            adj[u][v] = adj[v][u] = true;
        }
        int a = -1;
        int c = -1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!adj[i][j]) {
                    a = i;
                    c = j;
                }
            }
        }
        if (a == -1) {
            out.println("Yes");
            for (int i = 0; i < n; i++) {
                out.append('a');
            }
            return;
        }
        boolean[] setA = new boolean[n];
        boolean[] setC = new boolean[n];
        setA[a] = true;
        setC[c] = true;
        for (int i = 0; i < n; i++) {
            if (adj[i][a]) {
                setA[i] = true;
            }
            if (adj[i][c]) {
                setC[i] = true;
            }
        }
        boolean[] setB = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (setA[i] && setC[i]) {
                setB[i] = true;
                setA[i] = setC[i] = false;
            }
        }

        for (int i = 0; i < n; i++) {
            if (!(setA[i] || setB[i] || setC[i])) {
                out.println("No");
                return;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int ci = setA[i] ? 0 : setC[i] ? 2 : 1;
                int cj = setA[j] ? 0 : setC[j] ? 2 : 1;
                if ((Math.abs(ci - cj) <= 1) != (adj[i][j])) {
                    out.println("No");
                    return;
                }
            }
        }

        out.println("Yes");
        for (int i = 0; i < n; i++) {
            char ci = setA[i] ? 'a' : setC[i] ? 'c' : 'b';
            out.append(ci);
        }
    }

}
