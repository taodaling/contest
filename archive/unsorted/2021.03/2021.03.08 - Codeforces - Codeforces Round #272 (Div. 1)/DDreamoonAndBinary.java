package contest;


import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.SubstringCompare;
import template.utils.SequenceUtils;

public class DDreamoonAndBinary {
    int mod = (int) 1e9 + 7;
    SubstringCompare sc;

    public int compare(int l, int r, int L, int R) {
        return sc.compare(l, r, L, R);
    }

    private long valueOf(char[] s, int a) {
        return Long.valueOf(String.valueOf(s, a, s.length - a), 2);
    }

    public int compare(char[] s, int a, int k1, int b, int k2) {
        if (s.length - a >= 32 || s.length - b >= 32) {
            return -Integer.compare(a, b);
        }
        return Long.compare(valueOf(s, a) + k1, valueOf(s, b) + k2);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        sc = new SubstringCompare(new IntFunctionIntSequenceAdapter(i -> s[i], 0, n - 1));
        long[][] way = new long[n][n];
        int[][] time = new int[n][n];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(time, inf);
        way[0][0] = 1;
        time[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (j > i) {
                    way[i][j] += way[i][j - 1];
                    time[i][j] = Math.min(time[i][j], time[i][j - 1]);
                }
                way[i][j] %= mod;
            }
            for (int j = i; j < n; j++) {
                if (j > i && s[i] == '0') {
                    //invalid
                    way[i][j] = 0;
                    time[i][j] = inf;
                }
            }
            for (int j = i; j < n; j++) {
                if (time[i][j] >= inf) {
                    continue;
                }
                int l = j + 1;
                int r = (j - i) + l;
                if (r < n && compare(i, j, l, r) > 0) {
                    r++;
                }
                if (r >= n || r != l && s[l] == '0') {
                    continue;
                }
                way[l][r] += way[i][j];
                time[l][r] = Math.min(time[l][r], time[i][j] + 1);
            }
        }

        long totalWay = 0;
        int bestTail = 0;
        for (int i = 0; i < n; i++) {
            totalWay += way[i][n - 1];
            if (time[i][n - 1] < inf) {
                //possible
                if (compare(s, bestTail, time[bestTail][n - 1], i, time[i][n - 1]) > 0) {
                    bestTail = i;
                }
            }
        }

        long minOp = 0;
        for (int i = bestTail; i < n; i++) {
            minOp = minOp * 2 + s[i] - '0';
            minOp %= mod;
        }
        minOp += time[bestTail][n - 1];

        totalWay = DigitUtils.mod(totalWay, mod);
        minOp = DigitUtils.mod(minOp, mod);
        out.println(totalWay);
        out.println(minOp);
    }
}
