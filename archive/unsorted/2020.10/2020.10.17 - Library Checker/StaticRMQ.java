package contest;

import sun.security.provider.Sun;
import template.datastructure.RMQBySegment;
import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.utils.Buffer;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StaticRMQ {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        SumController sc = new SumController();
        SegTree<Sum, Object> segTree = new SegTree<>(n, sc, new UpdateController());
        segTree.init(1, 0, n - 1, i -> new Sum(a[i]));
        debug.debug("segTree", segTree);
        for (int i = 0; i < q; i++) {
            int l = in.readInt();
            int r = in.readInt() - 1;
            int ans = segTree.query(1, l, r, 0, n - 1).min;
            out.println(ans);
        }

    }
}

class UpdateController implements AlgebraController<Object> {

    @Override
    public Object alloc() {
        return new Object();
    }

    @Override
    public Object clone(Object o) {
        return new Object();
    }
}

class Sum {
    int min;

    public Sum(int min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "" + min;
    }
}

class SumController extends Buffer<Sum> implements AlgebraController<Sum> {
    static int inf = (int) 1e9;

    public SumController() {
        super(() -> new Sum(inf), x -> {
            x.min = inf;
        });
    }

    @Override
    public Sum plus(Sum a, Sum b) {
        Sum sum = alloc();
        sum.min = Math.min(a.min, b.min);
        return sum;
    }

    @Override
    public Sum clone(Sum sum) {
        Sum clone = new Sum(sum.min);
        return clone;
    }
}