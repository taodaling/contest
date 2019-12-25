package contest;

import template.algo.MoOnTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.MultiWayIntegerStack;

public class BZOJ3757 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        MultiWayIntegerStack edges = new MultiWayIntegerStack(n, 2 * n);
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            if (u == -1 || v == -1) {
                continue;
            }
            edges.addLast(u, v);
            edges.addLast(v, u);
        }

        Handler handler = new Handler(n);
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            queries[i] = new Query();
            queries[i].u = in.readInt() - 1;
            queries[i].v = in.readInt() - 1;
            queries[i].a = in.readInt();
            queries[i].b = in.readInt();
        }

        MoOnTreeBeta mo = new MoOnTreeBeta(edges);
        mo.handle(data, queries, handler);

        for(Query q : queries){
            out.println(q.ans);
        }
    }
}

class Query implements MoOnTreeBeta.Query {
    int u;
    int v;
    int ans;
    int a;
    int b;

    @Override
    public int getU() {
        return u;
    }

    @Override
    public int getV() {
        return v;
    }
}


class Handler implements MoOnTreeBeta.IntHandler<Query> {
    int[] cnts;
    int total;

    public Handler(int n) {
        cnts = new int[n + 1];
    }

    @Override
    public void add(int node, int x) {
        cnts[x]++;
        if (cnts[x] == 1) {
            total++;
        }
    }

    @Override
    public void remove(int node, int x) {
        cnts[x]--;
        if (cnts[x] == 0) {
            total--;
        }
    }

    @Override
    public void answer(Query query) {
        query.ans = total;
        if (query.a != query.b && cnts[query.a] > 0 && cnts[query.b] > 0) {
            query.ans--;
        }
    }
}

