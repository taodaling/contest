package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.algo.MoOnTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerDiscreteMap;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.List;

public class BZOJ1146 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }

        IntegerList allTimes = new IntegerList(n + q);
        allTimes.addAll(data);

        IntegerMultiWayStack edges = new IntegerMultiWayStack(n, n * 2);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges.addLast(a, b);
            edges.addLast(b, a);
        }

        List<Modify> modifyList = new ArrayList<>(q);
        List<Query> queryList = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            int k = in.readInt();
            int a = in.readInt();
            int b = in.readInt();
            if (k == 0) {
                Modify modify = new Modify();
                modify.x = a - 1;
                modify.val = b;
                modifyList.add(modify);
                allTimes.add(modify.val);
            } else {
                Query query = new Query();
                query.u = a - 1;
                query.v = b - 1;
                query.version = modifyList.size();
                query.k = k;
                queryList.add(query);
            }
        }

        IntegerDiscreteMap dm = new IntegerDiscreteMap(allTimes.getData(), 0, allTimes.size());
        for (int i = 0; i < n; i++) {
            data[i] = dm.rankOf(data[i]);
        }
        for (Modify modify : modifyList) {
            modify.val = dm.rankOf(modify.val);
        }

        MoOnTreeBeta mo = new MoOnTreeBeta(edges);
        mo.handle(data, modifyList.toArray(new Modify[0]),
                queryList.toArray(new Query[0]), new Handler(dm.maxRank()));

        for (Query query : queryList) {
            if (query.ans == -1) {
                out.println("invalid request!");
                continue;
            }
            out.println(dm.iThElement(query.ans));
        }
    }
}

class Query implements MoOnTreeBeta.VersionQuery {
    int u;
    int v;
    int version;
    int ans;
    int k;

    @Override
    public int getU() {
        return u;
    }

    @Override
    public int getV() {
        return v;
    }

    @Override
    public int getVersion() {
        return version;
    }
}

class Modify implements MoOnTreeBeta.IntModify {
    int x;
    int val;

    @Override
    public <Q extends MoOnTreeBeta.VersionQuery> void apply(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists) {
        if (exists[x]) {
            handler.remove(x, data[x]);
        }
        int oldVal = data[x];
        data[x] = val;
        val = oldVal;
        if (exists[x]) {
            handler.add(x, data[x]);
        }
    }

    @Override
    public <Q extends MoOnTreeBeta.VersionQuery> void revoke(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists) {
        apply(data, handler, exists);
    }
}

class Handler implements MoOnTreeBeta.IntHandler<Query> {
    int[] cnts;
    int[] summary;
    int blockSize;

    public Handler(int n) {
        cnts = new int[n + 1];
        blockSize = (int) Math.ceil(Math.sqrt(n + 1));
        summary = new int[n / blockSize + 1];
    }

    @Override
    public void add(int node, int x) {
        cnts[x]++;
        summary[x / blockSize]++;
    }

    @Override
    public void remove(int node, int x) {
        cnts[x]--;
        summary[x / blockSize]--;
    }

    @Override
    public void answer(Query query) {
        query.ans = -1;
        int k = query.k;
        for (int i = summary.length - 1; i >= 0; i--) {
            if (k > summary[i]) {
                k -= summary[i];
                continue;
            }

            for (int j = Math.min((i + 1) * blockSize, cnts.length) - 1; ; j--) {
                if (k > cnts[j]) {
                    k -= cnts[j];
                    continue;
                }
                query.ans = j;
                return;
            }
        }
    }
}