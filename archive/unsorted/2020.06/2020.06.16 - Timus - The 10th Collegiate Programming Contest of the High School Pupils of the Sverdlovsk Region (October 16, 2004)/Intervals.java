package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class Intervals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] vals = new int[n];
        for (int i = 0; i < n; i++) {
            vals[i] = in.readInt();
        }
        for (int i = 1; i < vals.length; i++) {
            vals[i] += vals[i - 1];
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int ans = vals[r];
            if(l > 0){
                ans -= vals[l - 1];
            }
            out.println(ans);
        }
    }
}
