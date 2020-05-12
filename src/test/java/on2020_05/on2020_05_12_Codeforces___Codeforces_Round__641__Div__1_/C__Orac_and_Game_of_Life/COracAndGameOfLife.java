package on2020_05.on2020_05_12_Codeforces___Codeforces_Round__641__Div__1_.C__Orac_and_Game_of_Life;



import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.utils.Debug;

import java.util.Arrays;

public class COracAndGameOfLife {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        int t = in.readInt();

        dsu = new DSU(n * m);
        LongDeque dq = new LongDequeImpl(n * m);
        LongDeque bak = new LongDequeImpl(n * m);
        //LongDeque holdA = new LongDequeImpl(n * m);
        //LongDeque holdB = new LongDequeImpl(n * m);
        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() - '0';
            }
        }
        long time = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int[] d : dirs) {
                    int ni = d[0] + i;
                    int nj = d[1] + j;
                    if (ni < 0 || nj < 0 || ni >= n || nj >= m) {
                        continue;
                    }
                    if (dsu.find(id(ni, nj)) == dsu.find(id(i, j))
                            || color(ni, nj, time) != color(i, j, time)) {
                        continue;
                    }
                    if (dsu.size[dsu.find(id(ni, nj))] == 1) {
                        dq.addLast(DigitUtils.asLong(ni, nj));
                    }
                    if (dsu.size[dsu.find(id(i, j))] == 1) {
                        dq.addLast(DigitUtils.asLong(i, j));
                    }

                    int c = color(i, j, time);
                    dsu.merge(id(ni, nj), id(i, j));
                    dsu.evenZero[dsu.find(id(i, j))] = c == time % 2;

                }
            }
        }

        Query[] qs = new Query[t];
        for (int i = 0; i < t; i++) {
            qs[i] = new Query();
            qs[i].i = in.readInt() - 1;
            qs[i].j = in.readInt() - 1;
            qs[i].t = in.readLong();
        }

        Query[] sorted = qs.clone();
        Arrays.sort(sorted, (a, b) -> Long.compare(a.t, b.t));

        Range2DequeAdapter<Query> range = new Range2DequeAdapter<>(i -> sorted[i], 0, sorted.length - 1);
//        {StringBuilder c = new StringBuilder();
//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < m; j++){
//                c.append(color(i, j, time));
//            }
//            c.append('\n');
//        }
//        debug.debug("time", time);
//        debug.debug("c", c);}
        while (!dq.isEmpty()) {
            time++;
            while (!dq.isEmpty()) {
                long key = dq.removeFirst();
                int i = DigitUtils.highBit(key);
                int j = DigitUtils.lowBit(key);
                for (int[] d : dirs) {
                    int ni = d[0] + i;
                    int nj = d[1] + j;
                    if (ni < 0 || nj < 0 || ni >= n || nj >= m) {
                        continue;
                    }
                    if (dsu.find(id(ni, nj)) == dsu.find(id(i, j))
                            || color(ni, nj, time) != color(i, j, time)) {
                        continue;
                    }
                    //holdA.addLast(DigitUtils.asLong(i, j));
                    //holdB.addLast(DigitUtils.asLong(i, j));
                    if (dsu.size[dsu.find(id(ni, nj))] == 1) {
                        bak.addLast(DigitUtils.asLong(ni, nj));
                    }
                    if (dsu.size[dsu.find(id(i, j))] == 1) {
                        bak.addLast(DigitUtils.asLong(i, j));
                    }

                    int c = color(i, j, time);
                    dsu.merge(id(ni, nj), id(i, j));
                    dsu.evenZero[dsu.find(id(i, j))] = c == time % 2;
                }
            }
            {
                LongDeque tmp = dq;
                dq = bak;
                bak = tmp;
            }
            while (!range.isEmpty() && range.peekFirst().t == time) {
                Query head = range.removeFirst();
                head.ans = color(head.i, head.j, time);
            }

//            StringBuilder c = new StringBuilder();
//            for(int i = 0; i < n; i++){
//                for(int j = 0; j < m; j++){
//                    c.append(color(i, j, time));
//                }
//                c.append('\n');
//            }
//            debug.debug("time", time);
//            debug.debug("c", c);
        }

        while (!range.isEmpty()) {
            Query head = range.removeFirst();
            head.ans = color(head.i, head.j, head.t);
        }

        for (Query q : qs) {
            out.println(q.ans);
        }
    }

    int n;
    int m;
    DSU dsu;
    int[][] mat;

    public int color(int i, int j, long t) {
        int id = dsu.find(id(i, j));
        if (dsu.size[id] == 1) {
            return mat[i][j];
        }
        if (dsu.evenZero[id]) {
            return t % 2 == 0 ? 0 : 1;
        } else {
            return t % 2 == 0 ? 1 : 0;
        }
    }

    int id(int i, int j) {
        return i * m + j;
    }
}

class Query {
    int i;
    int j;
    long t;
    int ans;

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", i, j, t);
    }
}

class DSU {
    int[] p;
    int[] rank;
    int[] size;
    boolean[] evenZero;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        size = new int[n];
        evenZero = new boolean[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }


    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        size[a] += size[b];
        p[b] = a;
    }
}
