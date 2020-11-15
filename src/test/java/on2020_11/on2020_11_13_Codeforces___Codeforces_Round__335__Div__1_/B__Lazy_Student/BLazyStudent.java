package on2020_11.on2020_11_13_Codeforces___Codeforces_Round__335__Div__1_.B__Lazy_Student;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BLazyStudent {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].w = in.readInt();
            edges[i].t = in.readInt();
        }

        Edge[] sorted = edges.clone();
        Arrays.sort(sorted, (a, b) -> a.w == b.w ? -Integer.compare(a.t, b.t) : Integer.compare(a.w, b.w));

        int cur = 1;
        int nextL = 1;
        int nextR = 1;
        Set<Long> added = new HashSet<>();
        for (Edge e : sorted) {
            if (e.t == 1) {
                e.u = cur;
                e.v = ++cur;
                added.add(eId(e.u, e.v));
            } else {
                while (true) {
                    if (nextR > cur) {
                        out.println(-1);
                        return;
                    }
                    if (nextL >= nextR) {
                        nextL = 1;
                        nextR++;
                        continue;
                    }
                    if (added.contains(eId(nextL, nextR))) {
                        nextL++;
                        continue;
                    }
                    added.add(eId(nextL, nextR));
                    e.u = nextL++;
                    e.v = nextR;
                    break;
                }
            }
        }

        for(Edge e : edges){
            out.append(e.u).append(' ').append(e.v).println();
        }
    }

    public long eId(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }
}

class Edge {
    int t;
    int w;
    int u;
    int v;
}
