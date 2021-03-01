package contest;

import template.graph.GeneralGraphMatch;
import template.io.FastInput;
import template.io.FastOutput;

public class WC2016NPC {
    int n;
    int m;

    int ballId(int i) {
        return i;
    }

    int basketId(int t, int i) {
        return t * m + n + i;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        int e = in.ri();
        GeneralGraphMatch match = new GeneralGraphMatch(n + 3 * m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 3; k++) {
                    match.addEdge(basketId(j, i), basketId(k, i));
                }
            }
        }
        for (int i = 0; i < e; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            for (int j = 0; j < 3; j++) {
                match.addEdge(ballId(u), basketId(j, v));
            }
        }
        int ans = match.maxMatch(false) - n;
        out.println(ans);
        for (int i = 0; i < n; i++) {
            int mate = match.mateOf(i);
            assert mate >= 0;
            mate = (mate - n) % m;
            out.append(mate + 1).append(' ');
        }
        out.println();
    }
}
