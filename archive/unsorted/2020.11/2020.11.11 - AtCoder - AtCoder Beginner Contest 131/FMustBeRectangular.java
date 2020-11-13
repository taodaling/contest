package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class FMustBeRectangular {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        DSU dsu = new DSU(n);
        dsu.init();
        int[][] pts = new int[n][2];
        Map<Integer, Integer> xMap = new HashMap<>();
        Map<Integer, Integer> yMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.readInt();
            }
            if (xMap.containsKey(pts[i][0])) {
                dsu.merge(i, xMap.get(pts[i][0]));
            } else {
                xMap.put(pts[i][0], i);
            }
            if (yMap.containsKey(pts[i][1])) {
                dsu.merge(i, yMap.get(pts[i][1]));
            } else {
                yMap.put(pts[i][1], i);
            }
        }

        Set<Integer>[] xSets = Stream.generate(HashSet::new).limit(n).toArray(i -> new Set[i]);
        Set<Integer>[] ySets = Stream.generate(HashSet::new).limit(n).toArray(i -> new Set[i]);
        for(int i = 0; i < n; i++){
            int p = dsu.find(i);
            xSets[p].add(pts[i][0]);
            ySets[p].add(pts[i][1]);
        }

        long ans = 0;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) == i){
                ans += xSets[i].size() * (long)ySets[i].size();
            }
        }
        ans -= n;
        out.println(ans);
    }
}
