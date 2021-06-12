package on2021_06.on2021_06_07_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.D__Just_Meeting;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DJustMeeting {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] known = new int[m][3];
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            known[i][0] = in.ri() - 1;
            known[i][1] = in.ri() - 1;
            known[i][2] = in.ri();
            dsu.merge(known[i][0], known[i][1]);
        }
        if (m == 0) {
            out.println(choose2(n));
            return;
        }
        Map<Integer, List<Integer>> belongTo = IntStream.range(0, n).boxed()
                .collect(Collectors.groupingBy(x -> dsu.find(x)));
        Map<Integer, List<int[]>> knownSet = Stream.of(known)
                .collect(Collectors.groupingBy(x -> dsu.find(x[0])));
        IntegerArrayList all = new IntegerArrayList(m);
        dsu.init();
        long pairs = 0;
        long ans = 0;
        for(Integer key : belongTo.keySet()){
            List<Integer> pts = belongTo.get(key);
            List<int[]> edgeList = knownSet.getOrDefault(key, Collections.emptyList());
            pairs += choose2(pts.size());
            if(pts.size() == 1){
                continue;
            }
            all.clear();
            for(int[] e : edgeList){
                all.add(e[2]);
            }
            edgeList.sort(Comparator.<int[]>comparingInt(x -> x[2]).reversed());
            int[][] edges = edgeList.toArray(new int[0][]);
            for(int i = 0; i < edges.length; i++){
                int l = i;
                int r = i;
                while(r + 1 < edges.length && edges[r + 1][2] == edges[i][2]){
                    r++;
                }
                i = r;
                for(int j = l; j <= r; j++){
                    if(dsu.find(edges[j][0]) == dsu.find(edges[j][1])){
                        out.println(-1);
                        return;
                    }
                }
                for(int j = l; j <= r; j++){
                    if(dsu.find(edges[j][0]) == dsu.find(edges[j][1])){
                        continue;
                    }
                    ans += edges[j][2] * (long)dsu.size[dsu.find(edges[j][0])]
                            * dsu.size[dsu.find(edges[j][1])];
                    dsu.merge(edges[j][0], edges[j][1]);
                }
            }
        }
        ans += choose2(n) - pairs;
        out.println(ans);
    }
}
