package on2021_07.on2021_07_15_Library_Checker.Static_RMQ;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;

public class StaticRMQ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        ParentOnTree pot = new ParentOnTreeByFunction(n, i -> i - 1);
        DepthOnTree dot = new DepthOnTreeByParent(n, pot);
        CompressedBinaryLiftWithAttachment<SumImpl> cbl = new CompressedBinaryLiftWithAttachment<>(n, dot, pot, SumImpl::new, i -> {
            SumImpl s = new SumImpl();
            s.min = a[i];
            return s;
        });
        for(int i = 0; i < q; i++){
            int l = in.ri();
            int r = in.ri() - 1;
            SumImpl ans = new SumImpl();
            cbl.kthAncestor(r, r - l, ans);
            out.println(ans.min);
        }
    }
}
class SumImpl implements Sum<SumImpl> {
    int min = Integer.MAX_VALUE;

    @Override
    public void add(SumImpl right) {
        min = Math.min(min, right.min);
    }

    @Override
    public void copy(SumImpl right) {
        min = right.min;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}
