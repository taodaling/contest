package contest;

import template.FastInput;
import template.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskD {
    int n;
    Segment seg;

    int[] a;
    int[] dp;
    int[] next;
    int[] half;
    int[] mx = new int[2];

    public void prepareMx() {
        mx[0] = 0;
        mx[1] = 0;
    }

    public int dp(int i) {
        i %= n;
        if (dp[i] == -1) {
            if (next[i] != -1 && seg.queryMax(i + 1, next[i], 0, 2 * n) * 2 >= a[i]) {
                int ans = dp(next[i]);
                if (ans == -1) {
                    dp[i] = -1;
                } else {
                    dp[i] = ans + next[i] - i;
                }
                return dp[i];
            }

            int l = i + 1;
            int r = 2 * n;
            while (l < r) {
                int m = (l + r) >> 1;
                int v = seg.queryMax(i + 1, m, 0, 2 * n);
                if (v * 2 < a[i]) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            if (next[i] != -1 && next[i] <= l) {
                int ans = dp(next[i]);
                if (ans == -1) {
                    dp[i] = -1;
                } else {
                    dp[i] = ans + next[i] - i;
                }
            } else if (l == 2 * n) {
                dp[i] = -1;
            } else {
                dp[i] = l - i;
            }
        }
        return dp[i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = new int[2 * n + 1];
        for (int i = 0; i < n; i++) {
            a[n + i] = a[i] = in.readInt();
        }

        seg = new Segment(0, 2 * n, a);
        dp = new int[n];
        Arrays.fill(dp, -1);

        next = new int[n];
        Deque<Integer> minQue = new ArrayDeque<>(n * 2);
        for (int i = 2 * n - 1; i >= n; i--) {
            while (!minQue.isEmpty() && a[minQue.peekFirst()] <= a[i]) {
                minQue.removeFirst();
            }
            minQue.addFirst(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!minQue.isEmpty() && a[minQue.peekFirst()] <= a[i]) {
                minQue.removeFirst();
            }
            if (minQue.isEmpty()) {
                next[i] = -1;
            } else {
                next[i] = minQue.peekFirst();
            }
            minQue.addFirst(i);
        }

        half = new int[n];
        int last = 2 * n - 1;
        for(int i = n - 1; i >= 0; i--){

        }

        for (int i = 0; i < n; i++) {
            int ans = dp(i);
            out.println(ans);
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;

    private int min;

    private int max;
    private int maxIndex;

    public void pushUp() {
        min = Math.min(left.min, right.min);
        max = Math.max(left.max, right.max);
        if (max == left.max) {
            maxIndex = left.maxIndex;
        } else {
            maxIndex = right.maxIndex;
        }
    }

    public void pushDown() {
    }

    public Segment(int l, int r, int[] a) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m, a);
            right = new Segment(m + 1, r, a);
            pushUp();
        } else {
            maxIndex = l;
            min = max = a[l];
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return Integer.MAX_VALUE;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }

    public void queryMaxIndex(int ll, int rr, int l, int r, int[] mx) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (mx[0] < max) {
                mx[0] = max;
                mx[1] = maxIndex;
            }
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.queryMaxIndex(ll, rr, l, m, mx);
        right.queryMaxIndex(ll, rr, m + 1, r, mx);
    }
}
