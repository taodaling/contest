package contest;


import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;

public class FBattalionStrength {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    Modular powMod = mod.getModularForPowerComputation();

    public void solve(int testNumber, FastInput in, FastOutput out) {

        int inv2 = power.inverseByFermat(2);

        int n = in.readInt();
        int[] val = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            val[i] = x;
            //root = add(root, create(x));
        }

        int q = in.readInt();
        int[][] qs = new int[q][2];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < 2; j++) {
                qs[i][j] = in.readInt();
            }
        }

        IntegerList list = new IntegerList(n + q);
        for (int i = 1; i <= n; i++) {
            list.add(val[i]);
        }
        for (int i = 0; i < q; i++) {
            list.add(qs[i][1]);
        }
        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());
        Segment seg = new Segment(dm.minRank(), dm.maxRank(), i -> dm.iThElement(i));
        //debug.debug("1/2(1/4+13)", mod.mul(inv2, mod.plus(power.inverseByFermat(4), 13)));
        //debug.debug("11/2", mod.mul(inv2, 11));
        for (int i = 1; i <= n; i++) {
            val[i] = dm.rankOf(val[i]);
        }
        for (int i = 0; i < q; i++) {
            qs[i][1] = dm.rankOf(qs[i][1]);
        }

        for (int i = 1; i <= n; i++) {
            seg.add(val[i], val[i], dm.minRank(), dm.maxRank());
        }

        out.println(mod.mul(inv2, seg.exp));
        for (int i = 0; i < q; i++) {
            // debug.debug("i", i);
            int index = qs[i][0];
            int x = qs[i][1];
            seg.remove(val[index], val[index], dm.minRank(), dm.maxRank());
            val[index] = x;
            seg.add(val[index], val[index], dm.minRank(), dm.maxRank());
            out.println(mod.mul(inv2, seg.exp));
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int size;
    private int sumX;
    private int sumY;
    public int exp;
    private int x;

    static Modular mod = new Modular(1e9 + 7);
    static Modular powMod = mod.getModularForPowerComputation();
    static CachedPow pow = new CachedPow(2, mod);

    public void pushUp() {
        size = left.size + right.size;
        int pos = mod.mul(right.sumX, pow.pow(left.size));
        int neg = mod.mul(right.sumY, pow.inverse(left.size));
        sumX = mod.plus(left.sumX, pos);
        sumY = mod.plus(left.sumY, neg);

        exp = mod.plus(left.exp, right.exp);
        exp = mod.plus(exp, mod.mul(left.sumX, neg));
    }

    public void pushDown() {
    }

    public void remove() {
        size--;
        int pos = mod.mul(x, pow.pow(size));
        int inv = mod.mul(x, pow.inverse(size));
        sumX = mod.subtract(sumX, pos);
        sumY = mod.subtract(sumY, inv);
        exp = mod.subtract(exp, mod.mul(sumX, inv));
    }

    public void add() {
        int pos = mod.mul(x, pow.pow(size));
        int inv = mod.mul(x, pow.inverse(size));
        exp = mod.plus(exp, mod.mul(sumX, inv));
        sumX = mod.plus(sumX, pos);
        sumY = mod.plus(sumY, inv);
        size++;
    }

    public Segment(int l, int r, IntToIntFunction function) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, function);
            right = new Segment(m + 1, r, function);
            pushUp();
        } else {
            x = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void add(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            add();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.add(ll, rr, l, m);
        right.add(ll, rr, m + 1, r);
        pushUp();
    }

    public void remove(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            remove();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.remove(ll, rr, l, m);
        right.remove(ll, rr, m + 1, r);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.queryL(ll, rr, l, m);
        right.queryL(ll, rr, m + 1, r);
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
