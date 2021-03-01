package contest;

import template.datastructure.RangeAffineRangeSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class RangeUpdatesAndSums {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        RangeAffineRangeSum st = new RangeAffineRangeSum(0, n - 1);
        st.init(0, n - 1, i -> a[i]);
        for (int i = 0; i < q; i++) {
            //debug.debug("st", st);
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            if (t == 1) {
                int x = in.ri();
                st.update(l, r, 0, n - 1, 1, x);
            }else if(t == 2){
                int x = in.ri();
                st.update(l, r, 0, n - 1, 0, x);
            }else{
                long sum = st.query(l, r, 0, n - 1);
                out.println(sum);
            }
        }
    }
}
