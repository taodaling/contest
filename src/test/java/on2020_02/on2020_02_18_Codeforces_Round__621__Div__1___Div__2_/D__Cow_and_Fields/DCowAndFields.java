package on2020_02.on2020_02_18_Codeforces_Round__621__Div__1___Div__2_.D__Cow_and_Fields;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.List;

public class DCowAndFields {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[] a = new int[k];
        for (int i = 0; i < k; i++) {
            a[i] = in.readInt() - 1;
        }
        List<UndirectedEdge>[] g = Graph.createUndirectedGraph(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        int s = 0;
        int t = n - 1;
        int[] fromS = new int[n];
        int[] fromT = new int[n];
        IntegerDeque dq = new IntegerDequeImpl(n);
        Graph.bfs(g, s, fromS, (int) 1e9, dq);
        Graph.bfs(g, t, fromT, (int) 1e9, dq);

        int origin = fromS[t];
        Pair[] pairs = new Pair[k];
        for (int i = 0; i < k; i++) {
            pairs[i] = new Pair(fromS[a[i]], fromT[a[i]]);
        }
        Arrays.sort(pairs, (x, y) -> Integer.compare(x.a - x.b, y.a - y.b));
        int[] maxD = new int[k];
        maxD[k - 1] = pairs[k - 1].b;
        for(int i = k - 2; i >= 0; i--){
            maxD[i] = Math.max(maxD[i + 1], pairs[i].b);
        }

        int ans = 0;
        for(int i = 0; i < k - 1; i++){
            ans = Math.max(ans, pairs[i].a + maxD[i + 1] + 1);
        }
        ans = Math.min(ans, origin);
        out.println(ans);
    }
}

class Pair {
    int a;
    int b;

    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }
}