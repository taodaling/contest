package on2021_03.on2021_03_31_Single_Round_Match_803.MarriageAndSelectionChallenge;



import template.binary.Bits;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class MarriageAndSelectionChallenge {
    //    Debug debug = new Debug(false);
    public String solve(String S) {
        char[] data = S.toCharArray();
        for (int i = 0; i < data.length; i++) {
            data[i] -= 'a';
        }
        int len = 12;
        int[][] go = new int[data.length][len];
        int[] reg = new int[len];
        Arrays.fill(reg, data.length);
        for (int i = data.length - 1; i >= 0; i--) {
            go[i] = reg.clone();
            reg[data[i]] = i;
        }
        int inf = (int)1e8;
        int[][] dp = new int[data.length + 1][1 << len];
        SequenceUtils.deepFill(dp, -inf);
        Arrays.fill(dp[data.length], 0);
        for (int i = data.length - 1; i >= 0; i--) {
            int c = data[i];
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < 1 << len; k++) {
                    for (int t = 0; t < len; t++) {
                        int to = go[i][t];
                        if (t == c) {
                            dp[i][k | (1 << c)] = Math.max(dp[i][k | (1 << c)], dp[to][k] + 1);
                        } else if(Bits.get(k, c) == 0){
                            dp[i][k | (1 << c)] = Math.max(dp[i][k | (1 << c)], dp[to][k] + 1);
                        }
                    }
                }
            }
        }
        int pos = -1;
        int best = -1;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 1 << len; j++) {
                if (dp[reg[i]][j] > best) {
                    pos = reg[i];
                    best = dp[reg[i]][j];
                }
            }
        }

        int state = 0;
        StringBuilder ans = new StringBuilder();
        while (pos < data.length) {
            state = Bits.set(state, data[pos]);
            ans.append(S.charAt(pos));
            best--;
            int next = -1;
            for (int i = 0; i < len && next == -1; i++) {
                int to = go[pos][i];
                for (int j = 0; j < 1 << len; j++) {
                    if (i != data[pos] && ((state) & j) != 0) {
                        continue;
                    }
                    if (i == data[pos] && (((state) & j) | (1 << i)) != (1 << i)) {
                        continue;
                    }
                    if (dp[to][j] == best) {
                        next = to;
                        break;
                    }
                }
            }
            if(next == -1){
                throw new IllegalStateException();
            }
            pos = next;
        }

        return ans.toString();
    }
}
