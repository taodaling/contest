package contest;


import com.sun.org.apache.xpath.internal.operations.Mod;
import org.codehaus.groovy.runtime.ScriptTestAdapter;
import template.algo.GenericMoAlgo;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class P1903 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] data = new int[n];
        in.populate(data);
        List<Query> qs = new ArrayList<>(m);
        List<Modify> mods = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            if (in.readChar() == 'Q') {
                qs.add(new Query(in.readInt() - 1, in.readInt() - 1,
                        mods.size() - 1));
            } else {
                int index = in.readInt() - 1;
                int color = in.readInt();
                mods.add(new Modify(index, data[index], color));
                data[index] = color;
            }
        }

        State state = new State(data);
        GenericMoAlgo.solve(0, n - 1, mods.size() - 1, state, qs.toArray(new Query[0]),
                mods.toArray(new Modify[0]), 5000);

        for(Query q : qs){
            out.println(q.ans);
        }
    }
}

class Query implements GenericMoAlgo.VersionQuery {
    int l;
    int r;
    int v;
    int ans;

    public Query(int l, int r, int v) {
        this.l = l;
        this.r = r;
        this.v = v;
    }

    @Override
    public int left() {
        return l;
    }

    @Override
    public int right() {
        return r;
    }

    @Override
    public int version() {
        return v;
    }
}

class Modify implements GenericMoAlgo.Modify {
    int index;
    int from;
    int to;

    public Modify(int index, int from, int to) {
        this.index = index;
        this.from = from;
        this.to = to;
    }

    @Override
    public int index() {
        return index;
    }
}

class State implements GenericMoAlgo.ModifiableState<Query, Modify> {
    int[] cnts = new int[(int) (1e6 + 1)];
    int diff = 0;
    int[] data;

    public State(int[] data) {
        this.data = data;
    }

    @Override
    public void answer(Query query) {
        query.ans = diff;
    }

    @Override
    public void add(int i) {
        int v = data[i];
        cnts[v]++;
        if (cnts[v] == 1) {
            diff++;
        }
    }

    @Override
    public void remove(int i) {
        int v = data[i];
        cnts[v]--;
        if (cnts[v] == 0) {
            diff--;
        }
    }

    @Override
    public void apply(Modify modify) {
        data[modify.index] = modify.to;
    }

    @Override
    public void revoke(Modify modify) {
        data[modify.index] = modify.from;
    }
}