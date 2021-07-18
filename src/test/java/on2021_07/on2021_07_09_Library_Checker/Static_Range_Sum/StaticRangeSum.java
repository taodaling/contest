package on2021_07.on2021_07_09_Library_Checker.Static_Range_Sum;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;

public class StaticRangeSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        ParentOnTree pot = new ParentOnTreeByFunction(n, i -> i - 1);
        DepthOnTree dot = new DepthOnTreeByParent(n, pot);
        long[] a = in.rl(n);
        CompressedBinaryLiftWithAttachment<SumImpl> bl = new CompressedBinaryLiftWithAttachment<>(n,
                dot, pot, SumImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.s = a[i];
            return ans;
        });

        for(int i = 0; i < q; i++){
            int l = in.ri();
            int r = in.ri() - 1;
            SumImpl ans = new SumImpl();
            bl.kthAncestor(r, r - l, ans);
            out.println(ans.s);
        }
    }
}

class SumImpl implements Sum<SumImpl> {
    long s;
    @Override
    public void add(SumImpl sum) {
        s += sum.s;
    }

    @Override
    public void copy(SumImpl sum) {
        s =  sum.s;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}