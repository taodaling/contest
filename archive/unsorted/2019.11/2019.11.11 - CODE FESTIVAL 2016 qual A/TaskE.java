package contest;

import java.util.Arrays;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;
import template.SequenceUtils;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[q + 1];
        for (int i = 1; i <= q; i++) {
            a[i] = in.readInt();
        }
        Order[] order = new Order[n + 1];
        for (int i = 1; i <= n; i++) {
            order[i] = new Order();
            order[i].val = i;
            order[i].last = -i;
        }
        IntegerList[] list = new IntegerList[n + 1];
        for (int i = 1; i <= n; i++) {
            list[i] = new IntegerList();
        }
        for (int i = 1; i <= q; i++) {
            list[a[i]].add(i);
            order[a[i]].last = i;
        }

        Arrays.sort(order, 1, n + 1, (x, y) -> -Integer.compare(x.last, y.last));

        int incUntil = n;
        while (incUntil > 1 && order[incUntil - 1].val < order[incUntil].val) {
            incUntil--;
        }

        for(int i = 1; i <= n; i++){
            SequenceUtils.reverse(list[i].getData(), 0, list[i].size());
        }

        IntegerList tmp = new IntegerList(n);
        for (int i = 1; i < incUntil; i++) {
            int x = order[i].val;
            int y = order[i + 1].val;
            if (list[x].size() < m) {
                no(out);
                return;
            }

            if(i + 1 == incUntil){
                break;
            }

            tmp.clear();
            for (int j = 0, k = 0; j < m && k < list[y].size(); j++) {
                int index = list[x].get(j);
                while (k < list[y].size() && list[y].get(k) > index) {
                    k++;
                }
                if (k == list[y].size()) {
                    no(out);
                    return;
                }
                tmp.add(list[y].get(k));
                k++;
            }

            list[y].clear();
            list[y].addAll(tmp);
        }

        yes(out);
    }

    public void no(FastOutput out) {
        out.println("No");
    }

    public void yes(FastOutput out) {
        out.println("Yes");
    }
}


class Order {
    int val;
    int last;
}
