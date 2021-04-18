package template.graph;

import template.primitve.generated.datastructure.IntegerBinaryFunction;

class Diameter {
    int[] data;

    public Diameter(int... data) {
        assert data.length == 2;
        this.data = data;
    }


    static Diameter merge(Diameter a, Diameter b, IntegerBinaryFunction dist) {
        assert a != null;
        assert b != null;
        int[] best = new int[2];
        int d1 = dist.apply(a.data[0], a.data[1]);
        int d2 = dist.apply(b.data[0], b.data[1]);
        int cur;
        if (d1 < d2) {
            best[0] = b.data[0];
            best[1] = b.data[1];
            cur = d2;
        } else {
            best[0] = a.data[0];
            best[1] = a.data[1];
            cur = d1;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int cand = dist.apply(a.data[i], b.data[j]);
                if (cand > cur) {
                    cur = cand;
                    best[0] = a.data[i];
                    best[1] = b.data[j];
                }
            }
        }

        return new Diameter(best);
    }
}
