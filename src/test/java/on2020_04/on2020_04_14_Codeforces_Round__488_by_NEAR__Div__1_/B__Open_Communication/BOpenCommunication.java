package on2020_04.on2020_04_14_Codeforces_Round__488_by_NEAR__Div__1_.B__Open_Communication;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public class BOpenCommunication {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Pair[] p1 = new Pair[n];
        Pair[] p2 = new Pair[m];
        for (int i = 0; i < n; i++) {
            p1[i] = new Pair(in.readInt(), in.readInt());
        }
        for (int i = 0; i < m; i++) {
            p2[i] = new Pair(in.readInt(), in.readInt());
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (p1[i].equals(p2[j])) {
                    continue;
                }
                if (p1[i].a == p2[j].a || p1[i].a == p2[j].b) {
                    p1[i].aMatch++;
                }
                if (p1[i].b == p2[j].a || p1[i].b == p2[j].b) {
                    p1[i].bMatch++;
                }
                if (p2[j].a == p1[i].a || p2[j].a == p1[i].b) {
                    p2[j].aMatch++;
                }
                if (p2[j].b == p1[i].a || p2[j].b == p1[i].b) {
                    p2[j].bMatch++;
                }
            }
        }

        //-1
        for (int i = 0; i < n; i++) {
            if (p1[i].aMatch > 0 && p1[i].bMatch > 0) {
                out.println(-1);
                return;
            }
        }
        for (int i = 0; i < m; i++) {
            if (p2[i].aMatch > 0 && p2[i].bMatch > 0) {
                out.println(-1);
                return;
            }
        }

        //0
        Set<Integer> lSet = new HashSet<>();
        Set<Integer> rSet = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (p1[i].aMatch > 0) {
                lSet.add(p1[i].a);
            }
            if (p1[i].bMatch > 0) {
                lSet.add(p1[i].b);
            }
        }
        for (int i = 0; i < m; i++) {
            if (p2[i].aMatch > 0) {
                rSet.add(p2[i].a);
            }
            if (p2[i].bMatch > 0) {
                rSet.add(p2[i].b);
            }
        }

        Set<Integer> intersect = CollectionUtils.intersect(lSet, rSet);
        if (intersect.size() > 1) {
            out.println(0);
            return;
        }

        out.println(intersect.iterator().next());
    }
}

class Pair {
    int a;
    int b;
    int aMatch = 0;
    int bMatch = 0;

    public Pair(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        Pair other = (Pair) obj;
        return other.a == a && other.b == b;
    }
}
