package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

public class BGluttony {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            indices[i] = i;
        }
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            b[indices[DigitUtils.mod(i - 1, n)]] = a[indices[i]];
        }

        for(int x : b){
            out.append(x).append(' ');
        }
    }
}
