package on2021_06.on2021_06_22_.Brief_Statements_Union;



import template.binary.Bits;
import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.awt.image.FilteredImageSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BriefStatementsUnion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Query[] qs = new Query[k];
        for (int i = 0; i < k; i++) {
            qs[i] = new Query();
            qs[i].l = in.ri() - 1;
            qs[i].r = in.ri() - 1;
            qs[i].v = in.rl();
            qs[i].id = i;
        }

        for (int i = 0; i < 2; i++) {
            classify[i] = new ArrayList<>(k);
        }
        left = new int[k];
        right = new int[k];
        notSatisfied = new ArrayList<>(k);
        impossible = new ArrayList<>(k);
        occur = new int[n + 1];
        belongTo = new int[n + 1];
        isOne = new int[n + 1];
        isZero = new int[n + 1];
        BitSet ans = new BitSet(k);
        ans.fill(true);
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < k; j++) {
                qs[j].bit = Bits.get(qs[j].v, i);
            }
            ans.and(solve(qs, n));
        }
        for (int i = 0; i < k; i++) {
            out.append(ans.get(i) ? 1 : 0);
        }
    }

    List<Query>[] classify = new List[2];
    List<Query> notSatisfied;
    List<Query> impossible;

    public int sum(int[] ps, int l, int r) {
        return ps[r] - (l == 0 ? 0 : ps[l - 1]);
    }

    int[] occur;
    int[] belongTo;
    int[] isOne;
    int[] isZero;
    int[] left;
    int[] right;
    int inf = (int) 1e9;

    public BitSet solve(Query[] qs, int n) {
        Arrays.fill(occur, 0);
        Arrays.fill(belongTo, 0);
        Arrays.fill(isOne, 0);
        Arrays.fill(isZero, 0);
        Arrays.fill(left, inf);
        Arrays.fill(right, -inf);
        impossible.clear();
        notSatisfied.clear();
        for (int i = 0; i < 2; i++) {
            classify[i].clear();
        }
        for (Query q : qs) {
            classify[q.bit].add(q);
        }
        for (Query q : classify[1]) {
            occur[q.l] += 1;
            occur[q.r + 1] -= 1;
            belongTo[q.l] += q.id;
            belongTo[q.r + 1] -= q.id;
        }
        for (int i = 1; i <= n; i++) {
            occur[i] += occur[i - 1];
            belongTo[i] += belongTo[i - 1];
        }
        for (int i = 0; i < n; i++) {
            isOne[i] = occur[i] == 1 ? 1 : 0;
            isZero[i] = occur[i] == 0 ? 1 : 0;
        }
        for (int i = 1; i < n; i++) {
            isOne[i] += isOne[i - 1];
            isZero[i] += isZero[i - 1];
        }

        BitSet bs = new BitSet(qs.length);
        for (Query q : classify[0]) {
            if (sum(isZero, q.l, q.r) > 0) {
                continue;
            }
            if (sum(isOne, q.l, q.r) == 0) {
                impossible.add(q);
            } else {
                notSatisfied.add(q);
            }
        }
        if (impossible.size() > 0) {
            if (impossible.size() == 1 && notSatisfied.isEmpty()) {
                bs.set(impossible.get(0).id);
            }
            return bs;
        }
        for (Query q : classify[0]) {
            if (notSatisfied.isEmpty() || notSatisfied.size() == 1 && notSatisfied.get(0) == q) {
                bs.set(q.id);
            }
        }
        int maxL = -inf;
        int minR = inf;
        for (Query q : notSatisfied) {
            maxL = Math.max(maxL, q.l);
            minR = Math.min(minR, q.r);
        }
        for (int i = 0; i < n; i++) {
            if (occur[i] == 1) {
                int index = belongTo[i];
                left[index] = Math.min(left[index], i);
                right[index] = Math.max(right[index], i);
            }
        }
        for (Query q : classify[1]) {
            if (left[q.id] <= minR && right[q.id] >= maxL) {
                bs.set(q.id);
            }
        }
        return bs;
    }
}

class Query {
    int l;
    int r;
    long v;
    int bit;
    int id;

    @Override
    public String toString() {
        return Arrays.toString(new int[]{l, r, bit});
    }
}
