package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBITExt;
import template.utils.CloneSupportObject;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class P4119Ynoi2018 {
    TreeMap<Integer, Interval> map = new TreeMap<>();

    void split(int key) {
        Map.Entry<Integer, Interval> entry = map.floorEntry(key - 1);
        if (entry == null || entry.getValue().r < key) {
            return;
        }
        //split
        Interval right = entry.getValue().clone();
        entry.getValue().r = key - 1;
        right.l = key;
        map.put(right.l, right);
    }

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
            int to = i;
            while (to + 1 <= n && a[to + 1] == a[i]) {
                to++;
            }
            map.put(i, new Interval(i, to, a[i]));
            opList.add(new Operation(i, to, a[i], 1, -1));
            i = to;
        }
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            if (t == 2) {
                qsList.add(new Query(in.ri(), in.ri(), in.ri(), i));
            } else {
                int l = in.ri();
                int r = in.ri();
                int y = in.ri();

                split(l);
                split(r + 1);

                while (true) {
                    Map.Entry<Integer, Interval> entry = map.ceilingEntry(l);
                    if (entry == null || entry.getValue().l > r) {
                        break;
                    }
                    Interval interval = entry.getValue();
                    map.remove(entry.getKey());
                    opList.add(new Operation(interval.l, interval.r, interval.val, -1, i));
                }
                map.put(l, new Interval(l, r, y));
                opList.add(new Operation(l, r, y, 1, i));
            }
        }
        IntegerArrayList all = new IntegerArrayList(opList.size());
        for (Operation op : opList) {
            all.add(op.val);
        }
        all.unique();
        unique = all.toArray();
        bit = new IntegerBITExt(n);
        opBuf = new Operation[opList.size()];
        qBuf = new Query[qsList.size()];
        ops = opList.toArray(new Operation[0]);
        qs = qsList.toArray(new Query[0]);
        dac(0, ops.length - 1, 0, qs.length - 1, 0, unique.length - 1);
        for (Query query : qsList) {
            out.println(query.ans);
        }
    }

    IntegerBITExt bit;
    int[] unique;

    void apply(Operation op) {
        bit.update(op.l, op.r, op.mod);
    }

    void revoke(Operation op) {
        bit.update(op.l, op.r, -op.mod);
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

class Interval extends CloneSupportObject<Interval> {
    int l;
    int r;
    int val;

    public Interval(int l, int r, int val) {
        this.l = l;
        this.r = r;
        this.val = val;
    }

    public Interval() {
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
    int l;
    int r;
    int val;
    int mod;
    int time;

    public Operation(int l, int r, int val, int mod, int time) {
        this.l = l;
        this.r = r;
        this.val = val;
        this.mod = mod;
        this.time = time;
    }
}
