package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class AWarriorAndArcher {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int min = (int) 1e9;
        int ban = (n - 2) / 2;
        for (int i = 0; i + ban + 2 <= n; i++) {
            int l = i;
            int r = i + ban + 2 - 1;
            min = Math.min(a[r] - a[l], min);
        }
        out.println(min);
    }
}
