package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

public class BInteractiveLowerBound {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.readInt();
        int start = in.readInt();
        int x = in.readInt();
        int[] cur = ask(start);
        RandomWrapper rw = new RandomWrapper();

        int time = 1000;
        for (int i = 0; i < time; i++) {
            int[] ans = ask(rw.nextInt(1, n));
            if (ans[0] <= x && ans[0] >= cur[0]) {
                cur = ans;
            }
        }

        while (cur[0] < x && cur[1] != -1) {
            cur = ask(cur[1]);
        }

        out.printf("! %d", cur[0] >= x ? cur[0] : -1).flush();
    }

    FastInput in;
    FastOutput out;

    public int[] ask(int i) {
        out.printf("? %d", i).println().flush();
        return new int[]{in.readInt(), in.readInt()};
    }
}
