package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class FRainbowBalls {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int sum = 0;
        for (int x : a) {
            sum += x;
        }

    }
}
