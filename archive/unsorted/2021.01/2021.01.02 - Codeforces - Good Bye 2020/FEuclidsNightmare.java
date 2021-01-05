package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;

public class FEuclidsNightmare {
    int mod = (int) 1e9 + 7;

    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSUExt dsu = new DSUExt(m);
        dsu.init();
        boolean[] used = new boolean[m];
        IntegerArrayList list = new IntegerArrayList(2);
        IntegerArrayList ans = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            list.clear();
            for (int j = 0; j < k; j++) {
                int x = in.ri() - 1;
                int to = dsu.left[dsu.find(x)];
                if (!used[to]) {
                    list.add(to);
                }
            }
            list.sort();
            if (list.size() == 1) {
                int x = list.get(0);
                used[x] = true;
                ans.add(i);
            } else if (list.size() == 2) {
                int a = list.first();
                int b = list.tail();
                if (a == b) {
                    continue;
                }
                if (a < b) {
                    int tmp = a;
                    a = b;
                    b = tmp;
                }
                used[a] = true;
                dsu.merge(a, b);
                ans.add(i);
            }
        }

        out.append(pow.pow(2, ans.size())).append(' ').append(ans.size()).println();
        for(int x : ans.toArray()){
            out.append(x + 1).append(' ');
        }
        out.println();

    }
}

class DSUExt extends DSU {
    int[] left;

    public DSUExt(int n) {
        super(n);
        left = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            left[i] = i;
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        left[a] = Math.min(left[a], left[b]);
    }
}
