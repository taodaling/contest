package on2021_07.on2021_07_16_DMOJ___Wesley_s_Anger_Contest_5.Problem_2___MATH_137_at_Squirreloo;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.utils.SequenceUtils;

public class Problem2MATH137AtSquirreloo {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = new int[n + 1];
        for(int i = 0; i < n; i++){
            a[i] = in.ri();
        }
        SequenceUtils.reverse(a, 0, n - 1);
        Segment st = new Segment(0, n, i -> a[i]);
        a[n] = Integer.MIN_VALUE;
        for(int i = 0; i < q; i++){
            int l = in.ri();
            int e = in.ri();
            int pos = st.query(0, n, 0, n, l - e, l + e);
            out.println(pos);
        }
    }

}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int min;
    int max;

    private void modify() {
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            min = max = func.apply(l);
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m);
        right.update(L, R, m + 1, r);
        pushUp();
    }

    public int query(int L, int R, int l, int r, int low, int high) {
        if(l == r){
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (left.min >= low && left.max <= high) {
            return right.query(L, R, m + 1, r, low, high);
        }
        return left.query(L, R, l, m, low, high);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}

