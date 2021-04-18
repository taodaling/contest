package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Moons_and_umbrellas;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class MoonsAndUmbrellas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int y = in.ri();
        char[] s = in.rs().toCharArray();
        int inf = (int) 1e9;
        int[] prev = new int[2];
        int[] next = new int[2];
        //c 0
        //j 1
        int[][] cost = new int[2][2];
        cost[0][1] = x;
        cost[1][0] = y;
        boolean first = true;
        for (char c : s) {
            if (first) {
                Arrays.fill(prev, inf);
                first = false;
                if (c != 'J') {
                    //try c
                    prev[0] = 0;
                }
                if (c != 'C') {
                    //try j
                    prev[1] = 0;
                }
                continue;
            }
            Arrays.fill(next, inf);
            for (int j = 0; j < 2; j++) {
                if (c != 'J') {
                    //try c
                    next[0] = Math.min(next[0], prev[j] + cost[j][0]);
                }
                if (c != 'C') {
                    //try j
                    next[1] = Math.min(next[1], prev[j] + cost[j][1]);
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }

        int ans = Math.min(prev[0], prev[1]);
        out.printf("Case #%d: %d", testNumber, ans).println();
    }
}
