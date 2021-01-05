package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BFindTheArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        for(int i = 0; i < n; i++){
            out.append(Integer.highestOneBit(a[i])).append(' ');
        }
        out.println();
    }
}
