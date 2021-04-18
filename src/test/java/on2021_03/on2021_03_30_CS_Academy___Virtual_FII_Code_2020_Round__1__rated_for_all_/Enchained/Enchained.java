package on2021_03.on2021_03_30_CS_Academy___Virtual_FII_Code_2020_Round__1__rated_for_all_.Enchained;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.primitve.generated.datastructure.LongIterator;
import template.primitve.generated.datastructure.LongMultiWayStack;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

public class Enchained {

    DSUExt dsu;
    int[] max;
    Debug debug = new Debug(true);

    public boolean intersect(Link a, Link b) {
        return a.l < b.l && b.l < a.r && a.r < b.r;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int L = in.ri() - 1;
        Link[] links = new Link[n];
        for (int i = 0; i < n; i++) {
            links[i] = new Link();
            links[i].l = in.ri();
            links[i].r = in.ri();
            links[i].id = i;
        }
        IntegerArrayList list = new IntegerArrayList(2 * n);
        for (Link link : links) {
            list.add(link.l);
            list.add(link.r);
            list.add(link.l + 1);
            list.add(link.r + 1);
            list.add(link.r - 1);
            list.add(link.l + L);
            list.add(link.l + L + 1);
            list.add(link.r + L);
            list.add(link.r + L + 1);
            list.add(link.r + L - 1);
        }
        list.unique();
        for (Link link : links) {
            link.lL = list.binarySearch(link.l + L);
            link.rL = list.binarySearch(link.r + L);
            link.l = list.binarySearch(link.l);
            link.r = list.binarySearch(link.r);

        }

        Link[] sortByL = links.clone();
        Link[] sortByR = links.clone();
        Arrays.sort(sortByL, Comparator.comparingInt(x -> x.l));
        Arrays.sort(sortByR, Comparator.comparingInt(x -> x.r));
        for (int i = 0; i < n; i++) {
            sortByL[i].lid = i;
            sortByR[i].rid = i;
        }

        debug.debugArray("sortByL", sortByL);
        debug.debugArray("sortByR", sortByR);
        int inf = (int) 1e9;
        int[][] headDp = new int[n][n];
        int[][] tailDp = new int[n][n];
        SequenceUtils.deepFill(headDp, -inf);
        SequenceUtils.deepFill(tailDp, -inf);
        for (int i = 0; i < n; i++) {
            headDp[i][i] = 1;
            tailDp[i][i] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            int tail = n - 1;
            int max = 1;
            for (int j = n - 1; j >= 0; j--) {
                if (!intersect(sortByR[j], sortByL[i])) {
                    continue;
                }
                int l = sortByR[j].r + 1;
                int r = sortByL[i].r - 1;
                while (tail >= 0 && sortByL[tail].l >= l) {
                    if (sortByL[tail].l <= r) {
                        max = Math.max(max, tailDp[sortByL[i].id][sortByL[tail].id]);
                    }
                    tail--;
                }
                tailDp[sortByR[j].id][sortByL[i].id] = max + 1;
            }
        }
        for (int i = 0; i < n; i++) {
            int head = 0;
            int max = 1;
            for (int j = 0; j < n; j++) {
                if (!intersect(sortByR[i], sortByL[j])) {
                    continue;
                }
                int l = sortByR[i].r + 1;
                int r = sortByL[j].r - 1;
                while (head < n && sortByR[head].r <= r) {
                    if (sortByR[head].r >= l) {
                        max = Math.max(max, headDp[sortByR[head].id][sortByR[i].id]);
                    }
                    head++;
                }
                headDp[sortByR[i].id][sortByL[j].id] = max + 1;
            }
        }

        debug.debug("tailDp", tailDp);
        debug.debug("headDp", headDp);

        int m = list.size();
        dsu = new DSUExt(m + 1);
        dsu.init();
        max = new int[m];


        LongMultiWayStack stack = new LongMultiWayStack(m, n * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tailDp[i][j] < 0) {
                    continue;
                }
                int l = links[i].l + 1;
                int r = links[j].l - 1;
                if (i == j) {
                    r = links[j].r - 1;
                }
                stack.addLast(tailDp[i][j], DigitUtils.asLong(l, r));
            }
        }

        for (int i = n; i >= 0; i--) {
            for (LongIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                long x = iterator.next();
                int l = DigitUtils.highBit(x);
                int r = DigitUtils.lowBit(x);
                while (true) {
                    l = dsu.right[dsu.find(l)];
                    if (l > r) {
                        break;
                    }
                    max[l] = i;
                    dsu.merge(l, l + 1);
                }
            }
        }

        long best = 1;
        for (Link link : links) {
            if (link.l < link.r) {
                best = 2;
                break;
            }
        }
        IntegerSparseTable st = new IntegerSparseTable(i -> max[i], max.length, Math::max);

        debug.debug("st", st);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (headDp[i][j] == -inf) {
                    continue;
                }
                int l = links[i].rL + 1;
                int r = links[j].rL - 1;
                if (i == j) {
                    l = links[i].lL + 1;
                }
                int cand = headDp[i][j];
                if (l <= r) {
                    cand += st.query(l, r) + 1;
                }
                best = Math.max(cand, best);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (headDp[i][j] == -inf) {
                    continue;
                }
                int l = links[i].rL + 1;
                int r = links[j].rL - 1;
                int cand = headDp[i][j];
                if (l < r) {
                    cand++;
                }
                best = Math.max(cand, best);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tailDp[i][j] < 0) {
                    continue;
                }
                int l = links[i].l + 1;
                int r = links[j].l - 1;
                int cand = tailDp[i][j];
                if (l <= r) {
                    cand++;
                }
                best = Math.max(cand, best);
            }
        }

        out.println(best);
    }
}

class DSUExt extends DSU {
    int[] right;

    public DSUExt(int n) {
        super(n);
        right = new int[n];
    }

    public void init(int n) {
        super.init(n);
        for (int i = 0; i < n; i++) {
            right[i] = i;
        }
    }

    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        right[a] = Math.max(right[a], right[b]);
    }

}

class Link {
    int id;
    int lid;
    int rid;
    int l;
    int r;
    int rL;
    int lL;

    public String toString() {
        return "" + id;
    }

}