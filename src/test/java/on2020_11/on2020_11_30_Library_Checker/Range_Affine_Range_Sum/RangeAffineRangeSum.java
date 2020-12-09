package on2020_11.on2020_11_30_Library_Checker.Range_Affine_Range_Sum;



import template.io.FastInput;
import template.io.FastOutput;

public class RangeAffineRangeSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = new int[n];
        in.populate(a);
        template.datastructure.RangeAffineRangeSum.mod = 998244353;
        template.datastructure.RangeAffineRangeSum st = new template.datastructure.RangeAffineRangeSum(0, n - 1);
        st.init(0, n - 1, i -> a[i]);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int l = in.ri();
            int r = in.ri() - 1;

            if(t == 0){
                st.update(l, r, 0, n - 1, in.ri(), in.ri());
            }else{
                int ans = st.query(l, r, 0, n - 1);
                out.println(ans);
            }
        }
    }
}
