package contest;

import template.datastructure.Loop;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.List;

public class DPortals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[] inEdge = new int[n];
        for (int i = 0; i < n; i++) {
            inEdge[i] = i;
        }
        Castle[] castles = new Castle[n];
        for (int i = 0; i < n; i++) {
            castles[i] = new Castle(in.readInt(), in.readInt(), in.readInt());
        }
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            inEdge[v] = Math.max(inEdge[v], u);
        }
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        for (int i = 0; i < n; i++) {
            Graph.addEdge(g, inEdge[i], i);
        }
        int[] total = new int[n];
        int cur = k;
        for (int i = 0; i < n; i++) {
            total[i] = cur;
            if (cur < castles[i].a) {
                out.println(-1);
                return;
            }
            cur += castles[i].b;
        }
        int limit = 5000;
        Loop<long[]> loop = new Loop<>(new long[limit + 1], new long[limit + 1]);
        SequenceUtils.deepFill(loop.get(), -(long) 1e18);
        loop.get()[0] = 0;
        for (int i = 1; i <= n; i++) {
            long[] last = loop.get();
            for (int j = 0; j <= limit; j++) {
                if (j > total[i - 1] - castles[i - 1].a) {
                    last[j] = -(long) 1e18;
                }
            }
            for (DirectedEdge e : g[i - 1]) {
                int to = e.to;
                last = loop.get();
                long[] next = loop.turn();
                for (int j = 0; j <= limit; j++) {
                    next[j] = last[j];
                    if (j > 0) {
                        next[j] = Math.max(next[j], last[j - 1] + castles[to].c);
                    }
                }
            }
        }
        long[] last = loop.get();
        for (int j = 0; j <= limit; j++) {
            if (j > cur) {
                last[j] = -(long)1e18;
            }
        }

        long ans = 0;
        for(int i = 0; i <= limit; i++){
            ans = Math.max(ans, last[i]);
        }

        out.println(ans);
    }
}

class Castle {
    int a;
    int b;
    int c;

    public Castle(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}