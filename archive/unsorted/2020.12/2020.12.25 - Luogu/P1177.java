package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Random;

public class P1177 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        SortUtils.quickSort(a, Integer::compare, 0, n);
        for (int x : a) {
            out.append(x).append(' ');
        }
    }
}
