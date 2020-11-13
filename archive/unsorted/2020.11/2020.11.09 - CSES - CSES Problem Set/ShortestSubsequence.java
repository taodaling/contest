package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ShortestSubsequence {
    int[] toInt = new int[128];
    char[] toChar = new char[4];

    {
        toChar[0] = 'A';
        toChar[1] = 'T';
        toChar[2] = 'C';
        toChar[3] = 'G';
        for (int i = 0; i < 4; i++) {
            toInt[toChar[i]] = i;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        int[] value = new int[n];
        for (int i = 0; i < n; i++) {
            value[i] = toInt[s[i]];
        }

        int[][] next = new int[4][n + 1];
        for (int i = 0; i < 4; i++) {
            next[i][n] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                next[j][i] = next[j][i + 1];
            }
            next[value[i]][i] = i;
        }
        int cur = 0;
        while (cur <= n) {
            int maxIndex = 0;
            for (int j = 1; j < 4; j++) {
                if (next[j][cur] > next[maxIndex][cur]) {
                    maxIndex = j;
                }
            }
            out.append(toChar[maxIndex]);
            cur = next[maxIndex][cur] + 1;
        }
    }
}
