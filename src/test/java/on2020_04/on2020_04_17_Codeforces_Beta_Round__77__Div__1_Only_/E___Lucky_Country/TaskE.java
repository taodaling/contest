package on2020_04.on2020_04_17_Codeforces_Beta_Round__77__Div__1_Only_.E___Lucky_Country;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMinQueue;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class TaskE {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSU dsu = new DSU(n);
        int[] cnts = new int[n];
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(a, b);
        }
        for (int i = 0; i < n; i++) {
            cnts[dsu.find(i)]++;
        }
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (cnts[i] > 0) {
                list.add(cnts[i]);
            }
        }
        list.sort();

        IntegerList a = new IntegerList(n);
        IntegerList b = new IntegerList(n);
        for (int i = 0, size = list.size(); i < size; i++) {
            int r = i;
            while (r + 1 < size && list.get(r) == list.get(r + 1)) {
                r++;
            }
            a.add(list.get(i));
            b.add(r - i + 1);
            i = r;
        }

        int k = a.size();
        int[] last = new int[n + 1];
        int[] cur = new int[n + 1];
        int inf = (int) 1e8;


        debug.debug("a", a);
        debug.debug("b", b);
        SequenceUtils.deepFill(last, inf);
        last[0] = 0;
        IntegerMinQueue dq = new IntegerMinQueue(m, IntegerComparator.NATURE_ORDER);
        for (int i = 1; i <= k; i++) {
            int av = a.get(i - 1);
            int bv = b.get(i - 1);
            for (int j = 0; j < av; j++) {
                dq.reset();
                for (int t = j, step = 0; t <= n; t += av, step++) {
                    dq.addLast(last[t] - step);
                    if (dq.size() > bv + 1) {
                        dq.removeFirst();
                    }
                    cur[t] = step + dq.min();
                }
            }

            int[] tmp = last;
            last = cur;
            cur = tmp;
        }

        debug.debug("dp", last);
        int ans = inf;
        IntegerList lucky = new IntegerList(32);
        collect(lucky, 4, n);
        collect(lucky, 7, n);
        for (int i = 0; i < lucky.size(); i++) {
            ans = Math.min(ans, last[lucky.get(i)]);
        }

        if (ans >= inf) {
            out.println(-1);
            return;
        }
        out.println(ans - 1);
    }


    public void collect(IntegerList list, int x, int n) {
        if (x > n) {
            return;
        }
        list.add(x);
        collect(list, x * 10 + 4, n);
        collect(list, x * 10 + 7, n);
    }
}
