package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P3___Mystery_DAG;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.stream.IntStream;

public class P3MysteryDAG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        int m = in.ri();

        edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = in.ri() - 1;
            e.b = in.ri() - 1;
            edges[i] = e;
        }

        dac(0, n - 1);
        out.append("! ");
        for (int i = 0; i < m; i++) {
            out.append(edges[i].dir);
        }
        out.println().flush();
    }

    Edge[] edges;
    FastInput in;
    FastOutput out;

    void dac(int l, int r) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(l, m);
        dac(m + 1, r);

        ask(IntStream.range(m + 1, r + 1).toArray(), IntStream.range(l, m + 1).toArray());
        int res = in.ri();
        for (int i = 0; i < res; i++) {
            int index = in.ri() - 1;
            edges[index].dir = 1;
        }
    }

    public void ask(int[] a, int[] b) {
        out.printf("? %d %d", a.length, b.length).println();
        for (int i = 0; i < a.length; i++) {
            out.append(a[i] + 1);
            if (i + 1 < a.length) {
                out.append(' ');
            }
        }
        out.println();
        for (int i = 0; i < b.length; i++) {
            out.append(b[i] + 1);
            if (i + 1 < b.length) {
                out.append(' ');
            }
        }
        out.println();
        out.flush();
    }
}

class Edge {
    int a;
    int b;
    int dir;
}