package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.TreeSet;

public class DGridGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int w = in.readInt();
        int h = in.readInt();
        int n = in.readInt();
        TreeSet<Integer>[] sets = new TreeSet[h + 1];
        for (int i = 1; i <= h; i++) {
            sets[i] = new TreeSet<>();
        }
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            sets[y].add(x);
        }

        int x = 1;
        int y = 1;
        int time = 0;
        int ans = w;
        while (true) {
            Integer r = sets[y].ceiling(x);
            if (r == null) {
                r = w + 1;
            }
            ans = Math.min(ans, time + r - x);
            if (r == x + 1) {
                break;
            }

            time++;
            x++;
            if (y < h && !sets[y + 1].contains(x)) {
                y++;
            }
        }

        out.println(ans);
    }
}