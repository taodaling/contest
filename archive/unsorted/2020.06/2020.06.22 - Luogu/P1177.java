package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

public class P1177 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        SortUtils.radixSort(a, 0, a.length - 1);
        for(int x : a){
            out.append(x).append(' ');
        }
    }
}
