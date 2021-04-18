package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__223__Div__1_.C__Sereja_and_Brackets;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class CSerejaAndBrackets {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.rs(s);
        SegTree<SumImpl, Update.NilUpdate> st = new SegTree<>(0, n - 1,
                SumImpl::new, Update.NilUpdate.SUPPLIER, i -> {
            SumImpl ans = new SumImpl();
            if (s[i] == '(') {
                ans.asLeft();
            } else {
                ans.asRight();
            }
            return ans;
        });

        debug.debug("st", st);
        SumImpl qry = new SumImpl();
        int m = in.ri();
        for(int i = 0; i < m; i++){
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            qry.clear();
            st.query(l, r, 0, n - 1, qry);
            int ans = qry.bestRight * 2;
            out.println(ans);
        }
    }
}

class SumImpl implements Sum<SumImpl, Update.NilUpdate> {
    int left;
    int right;
    int min;

    int bestLeft;
    int bestRight;

    public void asLeft() {
        left = 1;
        right = 0;
        min = 0;
        bestLeft = 1;
        bestRight = 0;
    }

    public void asRight() {
        left = 0;
        right = 1;
        min = -1;
        bestLeft = 0;
        bestRight = 0;
    }

    public void clear() {
        left = right = min = bestLeft = bestRight = 0;
    }

    @Override
    public void add(SumImpl sum) {
        int now = bestLeft - bestRight;
        //reduce + sum.min + now >= 0
        int reduce = Math.max(-sum.min - now, 0);
        bestLeft += sum.left;
        bestRight += sum.right - reduce;

        min = Math.min(min, left - right + sum.min);
        left += sum.left;
        right += sum.right;
    }

    @Override
    public void update(Update.NilUpdate nilUpdate) {

    }

    @Override
    public void copy(SumImpl sum) {
        left = sum.left;
        right = sum.right;
        min = sum.min;
        bestLeft = sum.bestLeft;
        bestRight = sum.bestRight;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "SumImpl{" +
                "left=" + left +
                ", right=" + right +
                ", min=" + min +
                ", bestLeft=" + bestLeft +
                ", bestRight=" + bestRight +
                '}';
    }
}