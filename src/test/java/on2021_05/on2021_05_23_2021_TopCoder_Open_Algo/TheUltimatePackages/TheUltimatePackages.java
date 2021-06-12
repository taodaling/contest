package on2021_05.on2021_05_23_2021_TopCoder_Open_Algo.TheUltimatePackages;



import template.graph.Graph;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashSet;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TheUltimatePackages {
    public int[] count(int N, int D, int[] Xprefix, int[] Yprefix, int L, int seed) {
        int[] X = new int[D];
        int[] Y = new int[D];
        int XL = Xprefix.length;
        for (int i = 0; i < XL; ++i) {
            X[i] = Xprefix[i];
            Y[i] = Yprefix[i];
        }

        long state = seed;
        for (int i = XL; i < D; ++i) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            int elen = (int) (1 + state % L);
            state = (state * 1103515245 + 12345) % (1L << 31);
            Y[i] = (int) (state % (N - elen));
            X[i] = Y[i] + elen;
        }

        IntegerArrayList us = new IntegerArrayList(X.length);
        IntegerArrayList vs = new IntegerArrayList(X.length);
        int[] deg = new int[N];
        for (int i = 0; i < X.length; i++) {
            deg[X[i]]++;
            deg[Y[i]]++;
            us.add(X[i]);
            vs.add(Y[i]);
        }

        int[][] g = Graph.createGraph(N, us.toArray(), vs.toArray());
        int[][] invG = Graph.createGraph(N, vs.toArray(), us.toArray());

        int[] left = count(invG, g);
        int[] right = count(g, invG);
        int cnt = 0;
        int sum = 0;
        for(int i = 0; i < N; i++){
            if(left[i] == i && right[i] == N - i - 1){
                cnt++;
                sum += i;
            }
        }
        return new int[]{cnt, sum};
    }

    int[] count(int[][] g, int[][] invG) {
        int n = g.length;
        int[] deg = new int[n];
        for (int i = 0; i < n; i++) {
            for (int x : g[i]) {
                deg[x]++;
            }
        }
        int[] ok = new int[n];
        Arrays.fill(ok, -1);
        Deque<Integer> prev = new ArrayDeque<>(n);
        Deque<Integer> next = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) {
                ok[i] = 0;
                prev.add(i);
            }
        }
        IntegerHashSet prevLevel = new IntegerHashSet(n, false);
        IntegerHashSet check = new IntegerHashSet(n, false);
        int levelTotal = 0;
        while (!prev.isEmpty()) {
            levelTotal += prev.size();
            next.clear();
            prevLevel.clear();
            for (int head : prev) {
                prevLevel.add(head);
                if (g[head].length == 0) {
                    return ok;
                }
                for(int x : g[head]){
                    deg[x]--;
                    if(deg[x] == 0){
                        next.add(x);
                    }
                }
            }
            for(int x : next){
                check.clear();
                for(int y : invG[x]){
                    if(prev.contains(y)){
                        check.add(y);
                    }
                }
                if(check.size() == prev.size()){
                    ok[x] = levelTotal;
                }
            }

            Deque<Integer> tmp = prev;
            prev = next;
            next = tmp;
        }

        return ok;
    }
}
