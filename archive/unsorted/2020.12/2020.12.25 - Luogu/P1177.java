package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Random;

public class P1177 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        CompareUtils.quickSort(a, Integer::compare, 0, n);
        for (int x : a) {
            out.append(x).append(' ');
        }
    }
}
