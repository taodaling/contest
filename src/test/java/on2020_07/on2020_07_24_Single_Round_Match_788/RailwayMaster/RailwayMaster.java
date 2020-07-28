package on2020_07.on2020_07_24_Single_Round_Match_788.RailwayMaster;



import template.datastructure.DSU;

import java.util.Arrays;
import java.util.PriorityQueue;

public class RailwayMaster {
    public int maxProfit(int N, int M, int K, int[] a, int[] b, int[] v) {
        boolean[] retain = new boolean[a.length];
        Arrays.fill(retain, true);
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> -Integer.compare(v[x], v[y]));
        for (int i = 0; i < a.length; i++) {
            pq.add(i);
        }
        int ans = 0;
        while (K > 0 && !pq.isEmpty()) {
            int head = pq.remove();
            retain[head] = false;
            if (connected(N, a, b, retain)) {
                ans += v[head];
                K--;
            } else {
                retain[head] = true;
            }
        }

        return ans;
    }

    public boolean connected(int N, int[] a, int[] b, boolean[] retain) {
        DSU dsu = new DSU(N);
        for (int i = 0; i < a.length; i++) {
            if (retain[i]) {
                dsu.merge(a[i], b[i]);
            }
        }
        for (int i = 0; i < N; i++) {
            if (dsu.find(i) != dsu.find(0)) {
                return false;
            }
        }
        return true;
    }
}
