package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class CParty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int mask = (1 << n) - 1;
        int[] dp = new int[mask + 1];
        int[] prev = new int[mask + 1];
        int[] taken = new int[mask + 1];
        Arrays.fill(dp, (int)1e8);


        int[] edges = new int[n];
        for (int i = 0; i < n; i++) {
            edges[i] = Bits.setBit(edges[i], i, true);
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges[a] = Bits.setBit(edges[a], b, true);
            edges[b] = Bits.setBit(edges[b], a, true);
        }

        for (int i = 1; i <= mask; i++) {
            int v = i;
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 1) {
                    v &= edges[j];
                }
            }
            dp[v] = 0;
            prev[v] = 0;
            taken[v] = 0;
        }

        for (int i = 1; i <= mask; i++) {
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 1) {
                    int next = i | edges[j];
                    if (dp[next] > dp[i] + 1) {
                        dp[next] = dp[i] + 1;
                        prev[next] = i;
                        taken[next] = j;
                    }
                }
            }
        }

        out.println(dp[mask]);
        IntegerList ans = new IntegerList(n);
        for (int i = mask; prev[i] != 0; i = prev[i]) {
            ans.add(taken[i]);
        }
        ans.reverse();
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i) + 1).append(' ');
        }
    }
}
