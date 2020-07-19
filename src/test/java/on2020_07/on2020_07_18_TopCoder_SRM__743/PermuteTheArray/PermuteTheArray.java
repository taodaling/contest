package on2020_07.on2020_07_18_TopCoder_SRM__743.PermuteTheArray;



import dp.Lis;
import template.datastructure.DeltaDSU;
import template.datastructure.XorDeltaDSU;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class PermuteTheArray {
    public int[] getPermutation(int[] A, int[] x, int[] y, int[] d) {
        int n = A.length;
        int m = x.length;
        Deque<Integer>[] dq = new Deque[2];
        for (int i = 0; i < 2; i++) {
            dq[i] = new ArrayDeque<>();
        }
        Arrays.sort(A);
        for (int i = 0; i < n; i++) {
            dq[A[i] % 2].addLast(A[i]);
        }
        XorDeltaDSU dsu = new XorDeltaDSU(n);
        int[] impossible = new int[0];
        for (int i = 0; i < m; i++) {
            if (dsu.find(x[i]) == dsu.find(y[i])) {
                if (dsu.delta(x[i], y[i]) != d[i]) {
                    return impossible;
                }
                continue;
            }
            dsu.merge(x[i], y[i], d[i]);
        }
        List<Integer>[][] indices = new List[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                indices[i][j] = new ArrayList<>();
            }
        }
        for (int i = 0; i < n; i++) {
            indices[dsu.find(i)][dsu.delta(i, dsu.find(i))].add(i);
        }
        for (int i = 0; i < n; i++) {
            if (indices[i][0].isEmpty() || (!indices[i][1].isEmpty() &&
                    indices[i][1].get(0) < indices[i][0].get(0))) {
                SequenceUtils.swap(indices[i], 0, 1);
            }
        }

        List<List<Integer>[]> groups = new ArrayList<>();
        int[] assign = new int[n];
        Arrays.fill(assign, -1);
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            groups.add(indices[i]);
        }
        groups.sort((a, b) -> Integer.compare(minIndex(a), minIndex(b)));
        boolean[][][] dp = new boolean[groups.size() + 1][n + 1][n + 1];
        dp[groups.size()][0][0] = true;
        for (int i = groups.size(); i >= 1; i--) {
            int a = groups.get(i - 1)[0].size();
            int b = groups.get(i - 1)[1].size();
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    if (!dp[i][j][k]) {
                        continue;
                    }
                    if (a + j <= n && b + k <= n) {
                        dp[i - 1][a + j][b + k] = true;
                    }
                    if (a + k <= n && b + j <= n) {
                        dp[i - 1][b + j][a + k] = true;
                    }
                }
            }
        }

        int[] ans = new int[n];
        int[] remains = new int[]{dq[0].size(), dq[1].size()};
        if (!dp[0][remains[0]][remains[1]]) {
            return impossible;
        }
        for (int i = 0; i < n; i++) {
            int g = dsu.find(i);
            if (assign[g] == -1) {
                int gid = groups.indexOf(indices[g]);
                boolean use1 = remains[1] >= indices[g][0].size() && remains[0] >= indices[g][1].size() &&
                        dp[gid + 1][remains[1] - indices[g][0].size()][remains[0] - indices[g][1].size()];
                if (dq[1].isEmpty() || (!dq[0].isEmpty() && dq[0].peekFirst() < dq[1].peekFirst())) {
                    if (remains[0] >= indices[g][0].size() && remains[1] >= indices[g][1].size() &&
                            dp[gid + 1][remains[0] - indices[g][0].size()][remains[1] - indices[g][1].size()]) {
                        use1 = false;
                    }
                }

                int xor = dsu.delta(minIndex(indices[g]), g);
                if (use1) {
                    assign[g] = 1 ^ xor;
                    remains[1] -= indices[g][0].size();
                    remains[0] -= indices[g][1].size();
                } else {
                    assign[g] = 0 ^ xor;
                    remains[0] -= indices[g][0].size();
                    remains[1] -= indices[g][1].size();
                }
            }

            int val = assign[g];
            if (dsu.delta(i, g) == 1) {
                val ^= 1;
            }
            ans[i] = dq[val].removeFirst();
        }

        return ans;
    }

    public int minIndex(List<Integer>[] lists) {
        int ans = lists[0].get(0);
        return ans;
    }
}
