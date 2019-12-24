package on2019_12.on2019_12_24_.LUOGU1903;



import template.algo.ModifiableMoOnArray;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class LUOGU1903 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<Modify> modifyList = new ArrayList<>(m);
        List<Query> queryList = new ArrayList<>(m);

        int[] data = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            data[i] = in.readInt();
        }

        for (int i = 0; i < m; i++) {
            if (in.readChar() == 'Q') {
                Query q = new Query();
                q.l = in.readInt();
                q.r = in.readInt();
                q.v = modifyList.size();
                queryList.add(q);
            } else {
                Modify modify = new Modify();
                modify.i = in.readInt();
                modify.value = in.readInt();
                modifyList.add(modify);
            }
        }

        ModifiableMoOnArray.handle(data, modifyList.toArray(new Modify[0]),
                queryList.toArray(new Query[0]), new Handler(1000000));

        for (Query q : queryList) {
            out.println(q.ans);
        }
    }
}

class Query implements ModifiableMoOnArray.Query {
    int l;
    int r;
    int v;
    int ans;

    @Override
    public int getL() {
        return l;
    }

    @Override
    public int getR() {
        return r;
    }

    @Override
    public int getVersion() {
        return v;
    }
}

class Handler implements ModifiableMoOnArray.IntHandler<Query> {
    int[] colors;
    int cnt;

    public Handler(int n) {
        colors = new int[n + 1];
    }

    @Override
    public void add(int i, int x) {
        colors[x]++;
        if (colors[x] == 1) {
            cnt++;
        }
    }

    @Override
    public void remove(int i, int x) {
        colors[x]--;
        if (colors[x] == 0) {
            cnt--;
        }
    }

    @Override
    public void answer(Query query) {
        query.ans = cnt;
    }
}

class Modify implements ModifiableMoOnArray.IntModify {
    int value;
    int i;

    @Override
    public <Q> void apply(int[] data, ModifiableMoOnArray.IntHandler<Q> handler, int l, int r) {
        int oldValue = data[i];
        if (l <= i && i <= r) {
            handler.remove(i, oldValue);
        }
        data[i] = value;
        value = oldValue;
        if (l <= i && i <= r) {
            handler.add(i, data[i]);
        }
    }

    @Override
    public <Q> void revoke(int[] data, ModifiableMoOnArray.IntHandler<Q> handler, int l, int r) {
        apply(data, handler, l, r);
    }
}