package on2020_05.on2020_05_11_Codeforces___Lyft_Level_5_Challenge_2018___Final_Round__Open_Div__1_.A__The_Tower_is_Going_Home;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.PriorityQueue;

public class ATheTowerIsGoingHome {
    int[][] hs;
    int[] vs;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        vs = new int[n];
        for (int i = 0; i < n; i++) {
            vs[i] = in.readInt();
        }
        int add = 0;
        hs = new int[m][3];
        PriorityQueue<Integer> right = new PriorityQueue<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                hs[i][j] = in.readInt();
            }
            if (hs[i][0] != 1) {
                continue;
            }
            if (hs[i][1] == 1e9) {
                add++;
                continue;
            }
            right.add(hs[i][1]);
        }

        Randomized.shuffle(vs);
        Arrays.sort(vs);

        if (n == 0) {
            out.println(add);
            return;
        }

        while (!right.isEmpty() && right.peek() < vs[0]) {
            right.poll();
        }

        int ans = Math.min(n, right.size());
        for (int i = 0; i < n - 1; i++) {
            while (!right.isEmpty() && right.peek() < vs[i + 1]) {
                right.poll();
            }
            ans = Math.min(ans, right.size() + i + 1);
        }

        out.println(ans + add);
    }
}
