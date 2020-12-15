package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MarsStomatology {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int p = in.ri();
        int[] active = new int[k];
        in.populate(active);
        List<Tooth>[] sets = new List[k];
        for (int i = 0; i < k; i++) {
            sets[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            int b = in.ri() - 1;
            sets[b].add(new Tooth(i, a));
        }
        int inf = (int) 1e9;
        int[][] dp = new int[k + 1][n + 1];
        int[][] choice = new int[k + 1][n + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < k; i++) {
            List<Tooth> set = sets[i];
            set.sort(Comparator.comparingInt(x -> x.cost));
            System.arraycopy(dp[i], 0, dp[i + 1], 0, dp[i].length);
            int sum = active[i];
            for (int j = 0; j < set.size(); j++) {
                sum += set.get(j).cost;
                for (int t = 0; t <= n; t++) {
                    int cand = dp[i][t] + sum;
                    if (t + j + 1 <= n && dp[i + 1][t + j + 1] > cand) {
                        dp[i + 1][t + j + 1] = cand;
                        choice[i + 1][t + j + 1] = j + 1;
                    }
                }
            }
        }

        int cur = n;
        while (dp[k][cur] > p) {
            cur--;
        }
        out.println(cur);
        for (int i = k; i > 0; i--) {
            int pick = choice[i][cur];
            cur -= pick;
            for (Tooth x : sets[i - 1]) {
                if (pick == 0) {
                    break;
                }
                pick--;
                out.append(x.id + 1).append(' ');
            }
        }
    }
}

class Tooth {
    int id;
    int cost;

    public Tooth(int id, int cost) {
        this.id = id;
        this.cost = cost;
    }
}