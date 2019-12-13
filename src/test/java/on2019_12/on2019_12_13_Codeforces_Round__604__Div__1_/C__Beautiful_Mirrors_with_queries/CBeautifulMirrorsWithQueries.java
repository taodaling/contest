package on2019_12.on2019_12_13_Codeforces_Round__604__Div__1_.C__Beautiful_Mirrors_with_queries;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CatTree;
import template.math.Modular;
import template.math.Power;

import java.util.Map;
import java.util.TreeMap;

public class CBeautifulMirrorsWithQueries {
    Modular mod = new Modular(998244353);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        int[] prob = new int[n];
        int inv100 = pow.inverse(100);
        for (int i = 0; i < n; i++) {
            prob[i] = mod.mul(in.readInt(), inv100);
        }
        Transform[] transforms = new Transform[n];
        for (int i = 0; i < n; i++) {
            transforms[i] = new Transform();
            transforms[i].a = 1;
            transforms[i].b = mod.valueOf(1 - prob[i]);
        }

        CatTree<Transform, Transform> catTree = new CatTree<>(transforms, n,
                new CatTree.SetHandler<Transform, Transform>() {
                    @Override
                    public Transform insertLeft(Transform element, Transform set) {
                        return mergeSet(element, set);
                    }

                    @Override
                    public Transform insertRight(Transform set, Transform element) {
                        return mergeSet(set, element);
                    }

                    @Override
                    public Transform makeSet(Transform element) {
                        return element;
                    }

                    @Override
                    public Transform mergeSet(Transform a, Transform b) {
                        return merge(a, b);
                    }
                });

        //expect(transforms[0]);
        TreeMap<Integer, Integer> map = new TreeMap<>();
        Transform whole = catTree.query(0, n - 1);
        int total = expect(whole);
        map.put(0, total);
        map.put(n, 0);
        for (int i = 0; i < q; i++) {
            Integer u = in.readInt() - 1;
            if (map.containsKey(u)) {
                total = mod.subtract(total, map.remove(u));
                Map.Entry<Integer, Integer> floor = map.floorEntry(u);
                Integer ceilKey = map.ceilingKey(u);
                total = mod.subtract(total, floor.getValue());
                Transform t = catTree.query(floor.getKey(), ceilKey - 1);
                int exp = expect(t);
                total = mod.plus(total, exp);
                map.put(floor.getKey(), exp);
            } else {
                Map.Entry<Integer, Integer> floor = map.floorEntry(u);
                total = mod.subtract(total, floor.getValue());
                Transform left = catTree.query(floor.getKey(), u - 1);
                int leftExp = expect(left);
                total = mod.plus(total, leftExp);
                map.put(floor.getKey(), leftExp);

                Integer ceil = map.ceilingKey(u);
                Transform t = catTree.query(u, ceil - 1);
                int exp = expect(t);
                total = mod.plus(total, exp);
                map.put(u, exp);
            }

            out.println(total);
        }
    }

    public int expect(Transform t) {
        return mod.mul(t.a, pow.inverse(1 - t.b));
    }

    public Transform merge(Transform a, Transform b) {
        Transform t = new Transform();
        t.a = mod.plus(a.a, mod.mul(b.a, 1 - a.b));
        t.b = mod.plus(a.b, mod.mul(b.b, 1 - a.b));
        return t;
    }
}

class Transform {
    int a; //expect to leave
    int b; //return probability

    @Override
    public String toString() {
        return String.format("a = %d, b = %d", a, b);
    }
}