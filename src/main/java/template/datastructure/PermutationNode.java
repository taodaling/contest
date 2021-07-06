package template.datastructure;

import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

public class PermutationNode<T extends PermutationNode<T>> {
    public List<T> adj = new ArrayList<>();
    /**
     * it's a permutation of [l,r]
     */
    public int l;
    public int r;
    /**
     * 合点（join node）
     */
    public boolean join;

    /**
     * it's P[ll..rr]
     */
    public int ll;
    public int rr;

    private PermutationNode fail;
    private int failMin;
    private int failMax;

    public boolean increment() {
        return l == r || adj.get(0).l == l;
    }

    public int length() {
        return r - l + 1;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d]", l, r);
    }

    /**
     * perm is a permutation of [0, n)
     *
     * @param perm
     * @return
     */
    public static <T extends PermutationNode> T build(int[] perm, Supplier<T> supplier) {
        int n = perm.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++) {
            index[perm[i]] = i;
        }
        RMQBeta rangeMax = new RMQBeta(n, (a, b) -> -Integer.compare(index[a], index[b]));
        Deque<PermutationNode> dq = new ArrayDeque<>(n);
        for (int k = 0; k < n; k++) {
            PermutationNode x = supplier.get();
            x.failMin = x.failMax = x.l = x.r = perm[k];
            x.ll = x.rr = k;
            x.join = true;

            while (!dq.isEmpty()) {
                //step 1
                PermutationNode y = dq.peekLast();
                if (y.join && y.adj.size() >= 2 && (x.r == y.l - 1 && !y.increment() ||
                        x.l == y.r + 1 && y.increment())) {
                    dq.removeLast();
                    y.adj.add(x);
                    y.l = Math.min(y.l, x.l);
                    y.r = Math.max(y.r, x.r);
                    y.rr = x.rr;
                    x = y;
                    continue;
                }

                //step 2
                if (y.r + 1 == x.l || x.r + 1 == y.l) {
                    dq.removeLast();
                    PermutationNode z = supplier.get();
                    z.adj.add(y);
                    z.adj.add(x);
                    z.join = true;
                    z.l = Math.min(y.l, x.l);
                    z.r = Math.max(y.r, x.r);
                    z.ll = y.ll;
                    z.rr = x.rr;
                    x = z;
                    continue;
                }

                //step 3
                //cut node has at least 4 children
                x.failMin = x.l;
                x.failMax = x.r;
                x.fail = y;
                boolean find = false;

                for (PermutationNode node = y; node != null; node = node.fail) {
                    int l = Math.min(x.failMin, node.l);
                    int r = Math.max(x.failMax, node.r);

                    if (index[rangeMax.query(l, r)] > k) {
                        //fail here
                        break;
                    }

                    int cnt = k - node.ll + 1;
                    if (cnt == r - l + 1) {
                        find = true;
                        //can be merged into a cut node
                        PermutationNode fa = supplier.get();
                        fa.join = false;
                        fa.adj.add(x);
                        fa.l = l;
                        fa.r = r;
                        fa.ll = node.ll;
                        fa.rr = k;
                        while (!dq.isEmpty()) {
                            PermutationNode tail = dq.removeLast();
                            fa.adj.add(tail);
                            if (tail == node) {
                                break;
                            }
                        }
                        SequenceUtils.reverse(fa.adj);
                        x = fa;
                        break;
                    }

                    x.failMin = Math.min(x.failMin, node.failMin);
                    x.failMax = Math.max(x.failMax, node.failMax);
                    x.fail = node.fail;
                }

                if (!find) {
                    break;
                }
            }

            if (dq.isEmpty()) {
                x.failMin = x.l;
                x.failMax = x.r;
            }
            dq.addLast(x);
        }

        if (dq.size() != 1) {
            throw new IllegalStateException();
        }

        return (T) dq.removeFirst();
    }
}
