package on2020_11.on2020_11_13_CSES___CSES_Problem_Set.Swap_Round_Sorting;



import dp.Lis;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class SwapRoundSorting {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }

        debug.debugArray("p", p);
        a = new ArrayList<>(n);
        b = new ArrayList<>(n);
        Permutation perm = new Permutation(p);
        List<IntegerArrayList> cs = perm.extractCircles();
        active = a;
        for (IntegerArrayList c : cs) {
            if (c.size() <= 2) {
                continue;
            }
            int l = 1;
            int r = c.size() - 1;
            while (l < r) {
                swap(c.get(l), c.get(r));
                l++;
                r--;
            }
        }
        debug.debugArray("p", p);
        active = b;
        for (int i = 0; i < n; i++) {
            assert i == p[p[i]];
            if (i < p[i]) {
                swap(i, p[i]);
            }
        }

        int req = 0;
        List<int[]>[] all = new List[]{a, b};
        for (List<int[]> x : all) {
            if (!x.isEmpty()) {
                req++;
            }
        }
        out.println(req);
        for (List<int[]> x : all) {
            if (x.isEmpty()) {
                continue;
            }
            out.println(x.size());
            for (int[] s : x) {
                out.append(s[0] + 1).append(' ').append(s[1] + 1).println();
            }
        }
    }


    List<int[]> a;
    List<int[]> b;
    List<int[]> active;
    int[] p;

    public void swap(int x, int y) {
        SequenceUtils.swap(p, x, y);
        active.add(new int[]{x, y});
    }
}
