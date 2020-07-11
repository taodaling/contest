package on2020_07.on2020_07_10_Codeforces___Codeforces_Round__385__Div__1_.B__Hongcow_s_Game;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BHongcowsGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int[][][] min = new int[10][2][n];
        for (int j = 0; j < 10; j++) {
            List<Integer>[] classify = new List[]{new ArrayList(), new ArrayList()};
            for (int t = 0; t < n; t++) {
                classify[Bits.get(t, j)].add(t);
            }
            for (int i = 0; i < 2; i++) {
                min[j][i] = ask(classify[i], in, out);
            }
        }

        int[] ans = new int[n];
        Arrays.fill(ans, inf);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < n; j++) {
                int rev = 1 - Bits.get(j, i);
                ans[j] = Math.min(ans[j], min[i][rev][j]);
            }
        }

        out.println(-1);
        for (int x : ans) {
            out.append(x).append(' ');
        }
        out.println().flush();
    }

    int inf = (int) 1e9;
    int n;

    public int[] ask(List<Integer> seq, FastInput in, FastOutput out) {
        int[] ans = new int[n];
        if (seq.isEmpty()) {
            Arrays.fill(ans, inf);
            return ans;
        }
        out.append(seq.size()).append(' ');
        for (int x : seq) {
            out.append(x + 1).append(' ');
        }
        out.println().flush();
        for (int i = 0; i < n; i++) {
            ans[i] = in.readInt();
        }
        return ans;
    }
}
