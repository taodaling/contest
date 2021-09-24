package on2021_08.on2021_08_01_TopCoder_Open_Round__4.PigeonholeNim;



import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class PigeonholeNim {
    public int countWinningMoves(int R, int C, int[] piles, int emptyWins) {
        int[][] cols = new int[C][R];
        for (int i = 0; i < C; i++) {
            for (int j = 0; j < R; j++) {
                cols[i][j] = piles[i * R + j];
            }
            SequenceUtils.reverse(cols[i]);
        }
        List<Col> colList = new ArrayList<>(C);
        int failfastCnt = 0;
        for (int i = 0; i < C; i++) {
            Col c = new Col();
            c.sg = calc(cols[i], emptyWins);
            if (c.sg == -1) {
                failfastCnt++;
            }
            int high = high(cols[i]);
            for (int j = 1; j <= cols[i][high]; j++) {
                if (failfast(high, cols[i][high] - j) || !valid(j)) {
                    continue;
                }
                c.move.add(dp(high, cols[i][high] - j));
            }
            if (high > 0 && !failfast(high - 1, cols[i][high - 1] - 1) && valid(cols[i][high])) {
                c.move.add(dp(high - 1, cols[i][high - 1] - 1));
            }
            colList.add(c);
        }
        if (failfastCnt > 1) {
            return failfastCnt;
        }
        if (failfastCnt == 1) {
            int ans = 1;
            int sg = 0;
            Col only = null;
            for (Col c : colList) {
                if (c.sg != -1) {
                    sg ^= c.sg;
                    continue;
                }
                only = c;
            }
            for (int move : only.move) {
                if ((move ^ sg) == 0) {
                    ans++;
                }
            }
            return ans;
        }
        int sg = 0;
        for (Col c : colList) {
            sg ^= c.sg;
        }
        int ans = 0;
        for (Col c : colList) {
            for (int move : c.move) {
                if ((sg ^ c.sg ^ move) == 0) {
                    ans++;
                }
            }
        }
        return ans;
    }

    int[][] dp;
    boolean[][] failfast;

    public boolean failfast(int n, int i) {
        assert n >= 0 && i >= 0;
        if (failfast[n][i]) {
            return true;
        }
        if (i == 0) {
            return failfast(n - 1, failfast[n - 1].length - 1);
        }
        return false;
    }


    IntegerVersionArray iva = new IntegerVersionArray(1 << 10);

    public int dp(int n, int i) {
        assert !failfast(n, i);
        if (dp[n][i] == -1) {
            if (i == 0) {
                return dp[n][i] = dp(n - 1, dp[n - 1].length - 1);
            }
            for (int j = 1; j <= i; j++) {
                if (failfast(n, i - j) || !valid(j)) {
                    continue;
                }
                dp(n, i - j);
            }
            if (n > 0 && !failfast(n - 1, dp[n - 1].length - 2) && valid(i)) {
                dp(n - 1, dp[n - 1].length - 2);
            }
            iva.clear();
            for (int j = 1; j <= i; j++) {
                if (failfast(n, i - j) || !valid(j)) {
                    continue;
                }
                iva.set(dp(n, i - j), 1);
            }
            if (n > 0 && !failfast(n - 1, dp[n - 1].length - 2) && valid(i)) {
                iva.set(dp(n - 1, dp[n - 1].length - 2), 1);
            }
            dp[n][i] = 0;
            while (iva.get(dp[n][i]) == 1) {
                dp[n][i]++;
            }
        }
        return dp[n][i];
    }

    public boolean valid(int x) {
        while (x > 0) {
            int tail = x % 10;
            if (tail == 4 || tail == 7) {
                return false;
            }
            x /= 10;
        }
        return true;
    }

    public int high(int[] x) {
        int ans = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > 0) {
                ans = i;
            }
        }
        return ans;
    }

    //-1 for winner immediately
    public int calc(int[] col, int emptyWins) {
        int n = high(col) + 1;
        dp = new int[n][];
        failfast = new boolean[n][];
        for (int i = 0; i < n; i++) {
            dp[i] = new int[col[i] + 1];
            failfast[i] = new boolean[col[i] + 1];
        }
        SequenceUtils.deepFill(dp, -1);
        failfast[0][0] = true;
        if (emptyWins == 0) {
        } else {
            for (int i = 1; i <= col[0]; i++) {
                if (valid(i)) {
                    failfast[0][i] = true;
                }
            }
            if (col.length > 1 && col[0] == 1) {
                for (int i = 1; i <= col[1]; i++) {
                    if (valid(i)) {
                        failfast[1][i] = true;
                    }
                }
            }
        }
        if (failfast(n - 1, col[n - 1])) {
            return -1;
        }
        return dp(n - 1, col[n - 1]);
    }
}

class Col {
    int sg;
    List<Integer> move = new ArrayList<>(300);
}