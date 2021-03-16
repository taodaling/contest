package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class P2617DynamicRankings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        List<Operation> opList = new ArrayList<>(n + m);
        List<Query> qsList = new ArrayList<>(m);
        for (int i = 1; i <= n; i++) {
            opList.add(new Operation(i, a[i], 1, -1));
        }
        for (int i = 0; i < m; i++) {
            char t = in.rc();
            if (t == 'Q') {
                qsList.add(new Query(in.ri(), in.ri(), in.ri(), i));
            } else {
                int x = in.ri();
                int y = in.ri();
                opList.add(new Operation(x, a[x], -1, i));
                opList.add(new Operation(x, y, 1, i));
                a[x] = y;
            }
        }
        IntegerArrayList all = new IntegerArrayList(opList.size());
        for (Operation op : opList) {
            all.add(op.val);
        }
        all.unique();
        unique = all.toArray();
        bit = new IntegerBIT(n + 1);
        opBuf = new Operation[opList.size()];
        qBuf = new Query[qsList.size()];
        ops = opList.toArray(new Operation[0]);
        qs = qsList.toArray(new Query[0]);
        dac(0, ops.length - 1, 0, qs.length - 1, 0, unique.length - 1);
        for(Query query : qsList){
            out.println(query.ans);
        }
    }

    IntegerBIT bit;
    int[] unique;

    void apply(Operation op) {
        bit.update(op.x, op.mod);
    }

    void revoke(Operation op) {
        bit.update(op.x, -op.mod);
    }

    Operation[] opBuf;
    Query[] qBuf;
    Operation[] ops;
    Query[] qs;

    public void dac(int L, int R, int l, int r, int lo, int hi) {
        if (l > r) {
            return;
        }
        if (lo == hi) {
            for (int i = l; i <= r; i++) {
                qs[i].ans = unique[lo];
            }
            return;
        }
        int mid = (lo + hi) / 2;
        int opMid = L;
        int opBufWpos = 0;
        for (int i = L; i <= R; i++) {
            if (ops[i].val <= unique[mid]) {
                SequenceUtils.swap(ops, i, opMid++);
            } else {
                opBuf[opBufWpos++] = ops[i];
            }
        }
        System.arraycopy(opBuf, 0, ops, opMid, opBufWpos);
        int dqHead = L;
        int qBufWpos = 0;
        int qMid = l;
        for (int i = l; i <= r; i++) {
            while (dqHead < opMid && ops[dqHead].time <= qs[i].time) {
                apply(ops[dqHead++]);
            }
            int num = bit.query(qs[i].l, qs[i].r);
            if (num >= qs[i].k) {
                SequenceUtils.swap(qs, i, qMid++);
            } else {
                qs[i].k -= num;
                qBuf[qBufWpos++] = qs[i];
            }
        }
        System.arraycopy(qBuf, 0, qs, qMid, qBufWpos);
        //right first
        while (dqHead - 1 >= L) {
            revoke(ops[--dqHead]);
        }
        dac(opMid, R, qMid, r, mid + 1, hi);
        dac(L, opMid - 1, l, qMid - 1, lo, mid);
    }
}

class Query {
    int time;
    int l;
    int r;
    int k;
    int ans;

    public Query(int l, int r, int k, int time) {
        this.l = l;
        this.r = r;
        this.k = k;
        this.time = time;
    }
}

class Operation {
    int x;
    int val;
    int mod;
    int time;

    public Operation(int x, int val, int mod, int time) {
        this.x = x;
        this.val = val;
        this.mod = mod;
        this.time = time;
    }
}
