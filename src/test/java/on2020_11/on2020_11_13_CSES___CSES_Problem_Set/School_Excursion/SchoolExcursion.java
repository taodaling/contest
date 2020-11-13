package on2020_11.on2020_11_13_CSES___CSES_Problem_Set.School_Excursion;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SchoolExcursion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSUExt dsu = new DSUExt(n);
        dsu.init();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(a, b);
        }
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                map.put(dsu.size[i], map.getOrDefault(dsu.size[i], 0) + 1);
            }
        }
        int[] prev = new int[n + 1];
        prev[0] = 1;
        int[] next = new int[n + 1];
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            Arrays.fill(next, 0);
            int k = e.getKey();
            int v = e.getValue();
            for (int j = 0; j < k; j++) {
                int ps = 0;
                for (int t = j; t <= n; t += k) {
                    ps += prev[t];
                    if (t - (v + 1) * k >= 0) {
                        ps -= prev[t - (v + 1) * k];
                    }
                    next[t] = ps > 0 ? 1 : 0;
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }

        for(int i = 1; i <= n; i++){
            out.append(prev[i]);
        }
    }
}

class DSUExt extends DSU {
    int[] size;

    public DSUExt(int n) {
        super(n);
        size = new int[n];
    }

    @Override
    public void init(int n) {
        super.init(n);
        Arrays.fill(size, 1);
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        size[a] += size[b];
    }
}