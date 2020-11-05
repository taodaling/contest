package on2020_11.on2020_11_01_Codeforces___Codeforces_Round__680__Div__1__based_on_Moscow_Team_Olympiad_.C__Team_Building;



import template.algo.UndoDSU;
import template.algo.UndoStack;
import template.algo.UndoXorDSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CTeamBuilding {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        int[] g = new int[n];
        in.populate(g);
        Map<Long, List<int[]>> edges = new HashMap<>();
        UndoStack stack = new UndoStack(m);
        UndoXorDSU dsu = new UndoXorDSU(n);
        dsu.init();
        boolean[] valid = new boolean[k + 1];
        Arrays.fill(valid, true);
        valid[0] = false;
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            if (g[u] == g[v]) {
                if (dsu.find(u) == dsu.find(v)) {
                    if (dsu.xor(u, v) != 1) {
                        valid[g[u]] = false;
                    }
                } else {
                    stack.push(dsu.merge(u, v, 1));
                }
            } else {
                int[] e = new int[]{u, v};
                long key = merge(g[u], g[v]);
                edges.computeIfAbsent(key, x -> new ArrayList<>()).add(e);
            }
        }

        long validCnt = 0;
        for (int i = 1; i <= k; i++) {
            if (valid[i]) {
                validCnt++;
            }
        }
        long ans = validCnt * (validCnt - 1) / 2;

        for (Map.Entry<Long, List<int[]>> entry : edges.entrySet()) {
            int g1 = DigitUtils.highBit(entry.getKey());
            int g2 = DigitUtils.lowBit(entry.getKey());
            if(!(valid[g1] && valid[g2])){
                continue;
            }
            int size = stack.size();
            boolean possible = true;
            for (int[] e : entry.getValue()) {
                int u = e[0];
                int v = e[1];
                if (dsu.find(u) == dsu.find(v)) {
                    if (dsu.xor(u, v) == 0) {
                        possible = false;
                    }
                } else {
                    stack.push(dsu.merge(u, v, 1));
                }
            }
            if (!possible) {
                ans--;
            }
            while (stack.size() > size) {
                stack.pop();
            }
        }

        out.println(ans);
    }

    public long merge(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }
}
