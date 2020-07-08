package on2020_07.on2020_07_08_TopCoder_SRM__747.RochesterSequence;



import template.math.Modular;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RochesterSequence {
    public void bf(int[] S) {
        dfs(S, 0, S.length - 1, new ArrayList<>(), 0);
    }

    List<int[]> ans = new ArrayList<>();

    public void dfs(int[] S, int l, int r, List<int[]> seq, int last) {
        if ((r - l + 1) / 2 + seq.size() <= ans.size()) {
            return;
        }
        if (l >= r) {
            if (ans.size() < seq.size()) {
                ans.clear();
                ans.addAll(seq);
            }
            return;
        }
        if (S[l] + S[r] <= last) {
            seq.add(new int[]{l, r});
            dfs(S, l + 1, r - 1, seq, S[l] + S[r]);
            seq.remove(seq.size() - 1);
        }
        dfs(S, l + 1, r, seq, last);
        dfs(S, l, r - 1, seq, last);
    }

    Debug debug = new Debug(true);

    public int[] solve(int[] Sprefix, int n, int a, int b, int m) {
        Modular mod = new Modular(1e9 + 7);
        int[] S = new int[n];
        for (int i = 0; i <= Sprefix.length - 1; i++) {
            S[i] = Sprefix[i];
        }
        for (int i = Sprefix.length; i <= n - 1; i++) {
            S[i] = (int) (((long) S[i - 1] * a + b) % m);
        }
//        Randomized.shuffle(S);
//        Arrays.sort(S);

        for (int i = 0; i < n; i++) {
            S[i] = -S[i];
        }
        //SequenceUtils.reverse(S);
        debug.debug("S", S);
        bf(S);
        debug.debug("bf", Arrays.deepToString(ans.toArray(new int[0][])));

        int[][] maxDP = new int[n][n];
        int[][] cntDP = new int[n][n];
        int[] right = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int max = 0;
            int cnt = 1;
            for (int j = i + 1; j < n; j++) {
                right[j] = j;
            }
            for (int j = i + 1; j < n; j++) {
                int threshold = S[i] + S[j];
                for (int t = i + 1; t < j; t++) {
                    while (right[t] + 1 < j && S[t] + S[right[t] + 1] <= threshold) {
                        right[t]++;
                        if (max < maxDP[t][right[t]]) {
                            max = maxDP[t][right[t]];
                            cnt = 0;
                        }
                        if (max == maxDP[t][right[t]]) {
                            cnt = mod.plus(cnt, cntDP[t][right[t]]);
                        }
                    }
                }
                if (max == 0 && cnt > 1) {
                    throw new RuntimeException();
                }
                maxDP[i][j] = max + 1;
                cntDP[i][j] = cnt;
            }
        }

        int max = 0;
        int cnt = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (maxDP[i][j] > max) {
                    max = maxDP[i][j];
                    cnt = 0;
                }
                if (maxDP[i][j] == max) {
                    cnt = mod.plus(cnt, cntDP[i][j]);
                }
            }
        }

        debug.debug("maxDP", maxDP);
        // debug.debug("cntDP", cntDP);
        return new int[]{max * 2, cnt};
    }
}
