package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;

public class Electrician {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Edge[] edges = new Edge[n];
        IntegerArrayList all = new IntegerArrayList(2 * n);
        for (int i = 0; i < n; i++) {
            edges[i] = new Edge();
            edges[i].id = i;
            edges[i].a = in.ri();
            edges[i].b = in.ri();
            edges[i].r = in.ri();
            edges[i].cost = in.ri();
            all.add(edges[i].a);
            all.add(edges[i].b);
        }
        all.unique();
        for (int i = 0; i < n; i++) {
            edges[i].a = all.binarySearch(edges[i].a);
            edges[i].b = all.binarySearch(edges[i].b);
        }
        Arrays.sort(edges, (x, y) -> x.r == y.r ? Integer.compare(x.cost, y.cost) : Integer.compare(x.r, y.r));
        DSU dsu = new DSU(all.size());
        dsu.init();
        long sum = 0;
        for (int i = n - 1; i >= 0; i--) {
            Edge e = edges[i];
            if (dsu.find(e.a) != dsu.find(e.b)) {
                dsu.merge(e.a, e.b);
                sum += e.cost;
            }
        }
        out.println(sum);
        for(int i = 0; i < n; i++){
            out.append(edges[i].id + 1).append(' ');
        }
    }
}

class Edge {
    int id;
    int a;
    int b;
    int cost;
    int r;
}
