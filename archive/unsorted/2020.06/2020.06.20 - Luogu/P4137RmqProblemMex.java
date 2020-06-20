package contest;

import template.algo.GenericMoAlgo;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class P4137RmqProblemMex {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = Math.min(n, in.readInt());
        }


        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            qs[i] = new Query(l, r);
        }

        State state = new State(n, data);
        GenericMoAlgo.solve(0, n - 1, state, qs.clone(), 700);
        for (Query q : qs) {
            out.println(q.ans);
        }
    }
}

class Query implements GenericMoAlgo.Query {
    int l;
    int r;
    int ans;

    public Query(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public int left() {
        return l;
    }

    @Override
    public int right() {
        return r;
    }
}

class State implements GenericMoAlgo.State<Query> {
    int[] cnts;
    int[] summary;
    int k;
    int[] data;

    public State(int n, int[] data) {
        this.data = data;
        cnts = new int[n + 1];
        k = (int) Math.ceil(Math.sqrt(n));
        summary = new int[DigitUtils.ceilDiv(n + 1, k)];
    }

    @Override
    public void answer(Query query) {
        for (int i = 0; i < summary.length; i++) {
            if (summary[i] == k) {
                continue;
            }
            for (int j = i * k; ; j++) {
                if (cnts[j] == 0) {
                    query.ans = j;
                    return;
                }
            }
        }
    }

    @Override
    public void add(int i) {
        int v = data[i];
        cnts[v]++;
        if (cnts[v] == 1) {
            summary[v / k]++;
        }
    }

    @Override
    public void remove(int i) {
        int v = data[i];
        cnts[v]--;
        if (cnts[v] == 0) {
            summary[v / k]--;
        }
    }
}