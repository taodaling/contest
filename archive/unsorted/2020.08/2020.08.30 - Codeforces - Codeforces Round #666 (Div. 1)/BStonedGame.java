package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BStonedGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int sum = Arrays.stream(a).sum();
        Arrays.sort(a);
        if (a[n - 1] > sum - a[n - 1]) {
            out.println("T");
            return;
        }
        if (sum % 2 == 0) {
            out.println("HL");
        } else {
            out.println("T");
        }
    }
}
