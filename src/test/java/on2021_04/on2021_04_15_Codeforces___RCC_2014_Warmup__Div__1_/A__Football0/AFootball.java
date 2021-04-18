package on2021_04.on2021_04_15_Codeforces___RCC_2014_Warmup__Div__1_.A__Football0;



import template.graph.TournamentBuilder;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Optional;

public class AFootball {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();

        int[] deg = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            deg[i] = Math.max(k, (i + 1) * i / 2 - sum);
            sum += deg[i];
        }
        Optional<boolean[][]> res = TournamentBuilder.build(deg);
        if (!res.isPresent()) {
            out.println(-1);
            return;
        }
        out.println(n * k);
        boolean[][] adj = res.get();
        for (int i = 0; i < n; i++) {
            int remain = k;
            for (int j = 0; j < n; j++) {
                if (adj[i][j] && remain > 0) {
                    remain--;
                    out.append(i + 1).append(' ').append(j + 1).println();
                }
            }
        }
    }
}
