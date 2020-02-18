package contest;

import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayStack;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class HMakeSquare {
    int maxA = 5032107;
    int maxAns = 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] as = new int[n];
        for (int i = 0; i < n; i++) {
            as[i] = in.readInt();
        }
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.readInt() - 1;
            qs[i].r = in.readInt() - 1;
        }
        Query[] sortedQs = qs.clone();
        Arrays.sort(sortedQs, (a, b) -> a.r != b.r ? a.r - b.r : a.l - b.l);
        IntegerMultiWayStack stack = Factorization.factorizeRangePrime(maxA);

        SimplifiedDeque<Query> deque = new Array2DequeAdapter<>(sortedQs);
        dp = new int[maxA + 1][maxAns + 1];
        best = new int[maxAns * 2 + 1];
        Arrays.fill(best, -1);
        SequenceUtils.deepFill(dp, -1);

        IntegerList buf = new IntegerList(7);
        for (int i = 0; i < n; i++) {
            buf.clear();
            for (IntegerIterator iterator = stack.iterator(as[i]); iterator.hasNext(); ) {
                int next = iterator.next();
                int cnt = 0;
                int x = as[i];
                while (x % next == 0) {
                    x /= next;
                    cnt++;
                }
                if (cnt % 2 == 1) {
                    buf.add(next);
                }
            }

            int m = buf.size();
            dfsForBest(buf.getData(), m - 1, 1, m);
            dfsForDP(buf.getData(), m - 1, 1, m, i);

            for (int j = 1; j < best.length; j++) {
                best[j] = Math.max(best[j], best[j - 1]);
            }

            int step = 0;
            while (!deque.isEmpty() && deque.peekFirst().r == i) {
                Query first = deque.removeFirst();
                while (best[step] < first.l) {
                    step++;
                }
                first.ans = step;
            }
        }

        for (Query query : qs) {
            out.println(query.ans);
        }
    }

    int[][] dp;
    int[] best;

    public void dfsForDP(int[] list, int i, int prod, int dist, int index) {
        if (i < 0) {
            dp[prod][dist] = index;
            return;
        }
        dfsForDP(list, i - 1, prod, dist, index);
        dfsForDP(list, i - 1, prod * list[i], dist - 1, index);
    }

    public void dfsForBest(int[] list, int i, int prod, int dist) {
        if (i < 0) {
            for (int j = 0; j <= maxAns; j++) {
                if (dp[prod][j] == -1) {
                    continue;
                }
                best[dist + j] = Math.max(best[dist + j], dp[prod][j]);
            }
            return;
        }
        dfsForBest(list, i - 1, prod, dist);
        dfsForBest(list, i - 1, prod * list[i], dist - 1);
    }
}

class Query {
    int l;
    int r;
    int ans;
}