package contest;

import template.algo.InversePair;
import template.algo.LIS;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Permutation;

import java.util.Comparator;

public class SortingMethods {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for(int i = 0; i < n; i++){
            a[i]--;
        }
        out.println(InversePair.inversePairCount(a));
        out.println(n - new Permutation(a).extractCircles().size());
        out.println(n - LIS.<Integer>lisLength(i -> a[i], n, Comparator.naturalOrder()));
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            inv[a[i]] = i;
        }
        int len = 1;
        for (int i = n - 2; i >= 0; i--) {
            if (inv[i] < inv[i + 1]) {
                len++;
            } else {
                break;
            }
        }
        out.println(n - len);
    }
}
