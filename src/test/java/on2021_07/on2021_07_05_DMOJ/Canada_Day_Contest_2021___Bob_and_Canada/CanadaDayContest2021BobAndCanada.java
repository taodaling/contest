package on2021_07.on2021_07_05_DMOJ.Canada_Day_Contest_2021___Bob_and_Canada;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CanadaDayContest2021BobAndCanada {
    public int eval(int a, int b) {
        return a != b ? 1 : 0;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        int inf = (int) 1e8;
        int[] prev = new int[4];
        int[] next = new int[4];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        char[] expect = "ARWR".toCharArray();
        for (char c : s) {
            Arrays.fill(next, inf);
            for (int i = 0; i < 4; i++) {
                if (i > 0) {
                    next[i] = Math.min(next[i], prev[i] + eval(c, expect[i]));
                }
                if (i + 1 < 4) {
                    next[i + 1] = Math.min(next[i + 1], prev[i] + eval(c, expect[i + 1]));
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = prev[3];
        out.println(ans);
    }
}
