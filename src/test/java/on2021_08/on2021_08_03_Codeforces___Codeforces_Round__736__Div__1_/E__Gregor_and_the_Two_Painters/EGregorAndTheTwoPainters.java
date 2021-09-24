package on2021_08.on2021_08_03_Codeforces___Codeforces_Round__736__Div__1_.E__Gregor_and_the_Two_Painters;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class EGregorAndTheTwoPainters {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int x = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(m);
        int[] na = na(a);
        int[] nb = na(b);

        debug.debugArray("na", na);
        debug.debugArray("nb", nb);
        Item[] aItems = new Item[n];
        Item[] bItems = new Item[m];
        for (int i = 0; i < n; i++) {
            aItems[i] = new Item(na[i], a[i], 0);
        }
        for (int i = 0; i < m; i++) {
            bItems[i] = new Item(nb[i], b[i], 1);
        }
        Item[] mix = new Item[n + m];
        System.arraycopy(aItems, 0, mix, 0, n);
        System.arraycopy(bItems, 0, mix, n, m);
        Arrays.sort(mix, Comparator.<Item>comparingInt(y -> y.na - y.a).reversed());
        IntegerBIT[] bit = new IntegerBIT[2];
        for (int i = 0; i < 2; i++) {
            bit[i] = new IntegerBIT(inf);
        }
        long ans = 0;
        for (Item item : mix) {
            int L = x - item.na + 1;
            int R = x - item.a;
            L = Math.max(1, L);
            if (L <= R) {
                ans += bit[item.type ^ 1].query(L, R);
            }
            bit[item.type].update(item.a, 1);
        }
        out.println(ans);
    }

    Debug debug = new Debug(false);

    int inf = (int) 3e5;

    public int[] na(int[] a) {
        int n = a.length;
        IntegerDeque dq = new IntegerDequeImpl(n);
        int[] left = new int[n];
        int[] right = new int[n];
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && a[dq.peekLast()] > a[i]) {
                dq.removeLast();
            }
            left[i] = dq.isEmpty() ? -1 : dq.peekLast();
            dq.addLast(i);
        }
        dq.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!dq.isEmpty() && a[dq.peekLast()] >= a[i]) {
                dq.removeLast();
            }
            right[i] = dq.isEmpty() ? n : dq.peekLast();
            dq.addLast(i);
        }
        RMQBeta rmq = new RMQBeta(n, (i, j) -> -Integer.compare(a[i], a[j]));
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = inf;
            if (left[i] >= 0) {
                ans[i] = Math.min(ans[i], a[rmq.query(left[i] + 1, i)]);
            }
            if (right[i] < n) {
                ans[i] = Math.min(ans[i], a[rmq.query(i, right[i] - 1)]);
            }
        }
        return ans;
    }
}


class Item {
    int na;
    int a;
    int type;

    public Item(int na, int a, int type) {
        this.na = na;
        this.a = a;
        this.type = type;
    }
}