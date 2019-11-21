package contest;

import template.DynamicMST;
import template.FastInput;
import template.FastOutput;

import java.util.*;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long x = in.readLong();
        int[][] edges = new int[m][3];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
            edges[i][2] = in.readInt();
        }

        DynamicMST mst = new DynamicMST(n);
        for (int i = 0; i < m; i++) {
            mst.addEdge(edges[i][0], edges[i][1], edges[i][2]);
        }
        if (mst.getEdgeNum() != n - 1) {
            out.println(0);
            return;
        }
        long diff = x - mst.getTotalWeight();
        if (diff < 0) {
            out.println(0);
            return;
        }

        Map<Long, Integer> cntMap = new HashMap<>(m);
        for (int[] e : edges) {
            long d = e[2] - mst.maxWeightBetween(e[0], e[1]);
            cntMap.put(d, cntMap.getOrDefault(d, 0) + 1);
        }

        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);
        if (diff == 0) {
            int num = cntMap.get(diff);
            int ans = mod.subtract(pow.pow(2, m), pow.pow(2, m - num + 1));
            out.println(ans);
            return;
        }
        if (!cntMap.containsKey(diff)) {
            out.println(0);
            return;
        }

        int lessCnt = 0;
        int equalCnt = 0;
        int greaterCnt = 0;
        for (Map.Entry<Long, Integer> entry : cntMap.entrySet()) {
            if (entry.getKey() < diff) {
                lessCnt += entry.getValue();
            }
            if (entry.getKey() == diff) {
                equalCnt += entry.getValue();
            }
            if (entry.getKey() > diff) {
                greaterCnt += entry.getValue();
            }
        }

        int ans = mod.subtract(pow.pow(2, equalCnt + 1), 2);
        ans = mod.mul(ans, pow.pow(2, greaterCnt));
        out.println(ans);
    }
}

