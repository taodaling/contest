package on2021_11.on2021_11_15_DMOJ___DMOPC__21_Contest_3.P3___Hopping_Frogs;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class P3HoppingFrogs {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        frogs = new int[n + m + 1];
        finish = new boolean[n + m + 1];
        Arrays.fill(frogs, 0, n, 1);
        Arrays.fill(frogs, n + 1, n + m + 1, -1);
        zero = n;

        int prefer = 1;
        while (make < n + m) {
            debug.debugArray("frogs", frogs);
            boolean ok = false;
            for (int i = zero - 2; i <= zero + 2; i++) {
                if (i < 0 || i >= frogs.length || frogs[i] != prefer) {
                    continue;
                }
                int dst = check(i);
                if (dst != -1) {
                    ok = true;
                    op(i, dst);
                    break;
                }
            }
            if (!ok) {
                prefer = -prefer;
                for (int i = zero - 2; i <= zero + 2; i++) {
                    if (i < 0 || i >= frogs.length || frogs[i] != prefer) {
                        continue;
                    }
                    int dst = check(i);
                    if (dst != -1) {
                        ok = true;
                        op(i, dst);
                        break;
                    }
                }
            }
            if (!ok) {
                throw new RuntimeException();
            }

        }
        debug.debugArray("frogs", frogs);
        out.println(op.size());
        for (int i = 0; i < op.size(); i++) {
            out.println(op.get(i));
        }
    }

    int make;
    boolean[] finish;
    int[] frogs;
    IntegerArrayList op = new IntegerArrayList((int) 6e6);

    private boolean doubleCheck(int x, int face) {
        if (face >= 0 && face < frogs.length && frogs[x] == frogs[face] &&
                !finish[face]) {
            return false;
        }
        return true;
    }

    private int check(int x) {
        if (x < 0 || x >= frogs.length || frogs[x] == 0) {
            return -1;
        }
        int to = x + frogs[x];
        if (to < 0 || to >= frogs.length || frogs[to] == frogs[x]) {
            return -1;
        }
        if (frogs[to] == 0) {
            return doubleCheck(x, to + frogs[x]) ? to : -1;
        }
        to += frogs[x];

        if (to < 0 || to >= frogs.length || frogs[to] != 0 || !doubleCheck(x, to + frogs[x])) {
            return -1;
        }
        return to;
    }

    int zero;

    public void op(int x, int to) {
        if (to == -1) {
            throw new RuntimeException();
        }
        SequenceUtils.swap(frogs, to, x);
        int toto = to + frogs[to];
        if (toto < 0 || toto >= frogs.length || frogs[toto] == frogs[to] && finish[toto]) {
            finish[to] = true;
            make++;
        }

        op.add(x);
        zero = x;
    }
}
