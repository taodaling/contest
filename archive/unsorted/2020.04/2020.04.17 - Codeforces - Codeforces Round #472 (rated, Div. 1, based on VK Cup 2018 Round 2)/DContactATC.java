package contest;

import numeric.Fraction;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.DoubleDiscreteMap;
import template.primitve.generated.datastructure.DoubleList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.Arrays;

public class DContactATC {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int w = in.readInt();
        Plane[] planes = new Plane[n];
        for (int i = 0; i < n; i++) {
            planes[i] = new Plane();
            planes[i].x = in.readInt();
            planes[i].v = in.readInt();
        }

        DoubleList list = new DoubleList(n);
        for (int i = 0; i < n; i++) {
            planes[i].a = (double) (-w + planes[i].v) / planes[i].x;
            planes[i].b = (double) (w + planes[i].v) / planes[i].x;
            list.add(planes[i].b);
        }

        DoubleDiscreteMap dm = new DoubleDiscreteMap(list.getData(), 0, list.size());
        Arrays.sort(planes, (a, b) -> a.a != b.a ? -Double.compare(a.a, b.a) : Double.compare(a.b, b.b));
        IntegerBIT bit = new IntegerBIT(dm.maxRank() + 2);

        debug.debug("ps", planes);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int b = dm.rankOf(planes[i].b);
            ans += bit.query(b + 1);
            bit.update(b + 1, 1);
        }

        out.println(ans);
    }
}

class Plane {
    int x;
    int v;
    double a;
    double b;

    @Override
    public String toString() {
        return "" + a + ", " + b;
    }
}
