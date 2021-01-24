package on2021_01.on2021_01_19_CSES___CSES_Problem_Set.Binary_Subsequences;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.IntConsumer;

public class BinarySubsequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int bestA = -1;
        int best = (int) 1e9;
        for (int i = 0; i + i <= n; i++) {
            int contrib = step(i, n - i);
            if (contrib < 0) {
                continue;
            }
            if (contrib < best) {
                best = contrib;
                bestA = i;
            }
        }
        assert bestA >= 0;
        output(bestA, n - bestA, '0', '1', x -> out.append((char) x));
    }

    public void output(int a, int b, char ac, char bc, IntConsumer consumer) {
        if (a == b) {
            assert a == 0;
            return;
        }
        if (a < b) {
            output(b, a, bc, ac, consumer);
            return;
        }
        while (a > b) {
            a -= b + 1;
            consumer.accept(ac);
        }
        output(b, a, bc, ac, consumer);
    }

    public int step(int a, int b) {
        if (a == b) {
            return a == 0 ? 0 : (int) -1e9;
        }
        if (a < b) {
            return step(b, a);
        }
        return a / (b + 1) + step(b, a % (b + 1));
    }
}
