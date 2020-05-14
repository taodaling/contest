package on2020_05.on2020_05_14_Codeforces___Codeforces_Round__426__Div__1_.B__The_Bakery;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BTheBakery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        machine = new Machine(a);
        lastLevel = new int[n + 1];
        curLevel = new int[n + 1];

        int inf = (int) 1e8;
        Arrays.fill(lastLevel, -inf);
        lastLevel[0] = 0;
        for (int i = 1; i <= k; i++) {
            curLevel[0] = -inf;
            dac(0, n, 1, n);
            int[] tmp = lastLevel;
            lastLevel = curLevel;
            curLevel = tmp;
        }

        int ans = lastLevel[n];
        out.println(ans);
    }

    int[] lastLevel;
    int[] curLevel;
    Machine machine;

    void dac(int ll, int lr, int cl, int cr) {
        if (cl > cr) {
            return;
        }
        if (ll == lr) {
            for (int i = cl; i <= cr; i++) {
                machine.move(ll + 1, i);
                curLevel[i] = lastLevel[ll] + machine.distinct;
            }
            return;
        }

        int m = (cl + cr) / 2;
        int index = ll;
        for (int i = ll; i < m && i <= lr; i++) {
            machine.move(i + 1, m);
            int val = lastLevel[i] + machine.distinct;
            if (val > curLevel[m]) {
                curLevel[m] = val;
                index = i;
            }
        }

        dac(ll, index, cl, m - 1);
        dac(index, lr, m + 1, cr);
    }
}

class Machine {
    final int[] a;
    int l = 0;
    int r = -1;
    int[] cnts = new int[35000 + 1];
    int distinct;

    Machine(int[] a) {
        this.a = a;
    }

    public void add(int i) {
        cnts[i]++;
        if (cnts[i] == 1) {
            distinct++;
        }
    }

    public void remove(int i) {
        cnts[i]--;
        if (cnts[i] == 0) {
            distinct--;
        }
    }

    public void move(int lTo, int rTo) {
        while (l > lTo) {
            l--;
            add(a[l]);
        }
        while (r < rTo) {
            r++;
            add(a[r]);
        }
        while (l < lTo) {
            remove(a[l]);
            l++;
        }
        while (r > rTo) {
            remove(a[r]);
            r--;
        }
    }
}