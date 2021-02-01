package contest;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.Arrays;

public class RaceCircuit {
    IntegerArrayList collector = new IntegerArrayList((int) 2e5);
    IntegerHashMap map = new IntegerHashMap((int) 2e5, true);
    int n, m;

    //3 for ( and 1 for )
    private void collect(int i, int buildRes, int sum) {
        if (sum > 0) {
            return;
        }
        if (i == -1) {
            if (sum == 0) {
                collector.add(buildRes);
            }
            return;
        }
        collect(i - 1, (buildRes << 2) | 0, sum);
        collect(i - 1, (buildRes << 2) | 1, sum - 1);
        collect(i - 1, (buildRes << 2) | 3, sum + 1);
    }

    private int get(int x, int i) {
        return (x >> 2 * i) & 3;
    }

    private int set(int x, int i, int v) {
        return x ^ ((get(x, i) ^ v) << 2 * i);
    }

    private int set(int x, int i, int v, int u) {
        return set(set(x, i, v), i + 1, u);
    }

    private int searchMatch(int x, int i) {
        int v = get(x, i);
        if (v == 0) {
            return -1;
        }
        int cnt = v - 2;
        for (int j = i + (v - 2); ; j += (v - 2)) {
            int y = get(x, j);
            if (y == 0) {
                continue;
            }
            cnt += y - 2;
            if (cnt == 0) {
                return j;
            }
        }
    }

    private boolean[][] rotate(boolean[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        boolean[][] ans = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans[i][j] = mat[j][i];
            }
        }
        return ans;
    }

    private String toString(int x) {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i <= m; i++) {
            int v = get(x, i);
            ans.append(v == 3 ? '(' : v == 1 ? ')' : '#');
        }
        return ans.toString();
    }

    private int inverse(int x, int i) {
        assert get(x, i) != 0;
        return x ^ (2 << 2 * i);
    }
//
//    Debug debug = new Debug(true);

    /**
     * mat[i][j] means whether we can pass it. O(nm3^m)
     *
     * @return
     */
    public int count(int[] right, int[] down, int[] corner, int n, int m) {
        this.n = n;
        this.m = m;
        int rbx = n - 1;
        int rby = m - 1;

        collector.clear();
        map.clear();
        collect(m, 0, 0);
        int[] all = collector.toArray();
        assert CompareUtils.strictAscending(all, 0, all.length - 1);
        for (int i = 0; i < all.length; i++) {
            map.put(all[i], i);
        }
        int[][] match = new int[m + 1][all.length];
        int[] shift = new int[all.length];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j < all.length; j++) {
//                debug.debug("i", i);
//                debug.debug("j", toString(all[j]));
                match[i][j] = searchMatch(all[j], i);
            }
        }
        for (int i = 0; i < all.length; i++) {
            if (get(all[i], m) != 0) {
                shift[i] = -1;
            } else {
                shift[i] = map.get(all[i] << 2);
            }
        }
        int inf = (int) 1e8;
        int[] prev = new int[all.length];
        int[] next = new int[all.length];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int rc = right[i * m + j];
                int dc = down[i * m + j];
                int cc = corner[i * m + j];
                if (rc == -1) {
                    rc = inf;
                }
                if (dc == -1) {
                    dc = inf;
                }
                if (cc == -1) {
                    cc = inf;
                }

                if (j == 0) {
                    Arrays.fill(next, inf);
                    for (int k = 0; k < all.length; k++) {
                        if (prev[k] == inf) {
                            continue;
                        }
                        int to = shift[k];
                        if (to == -1) {
                            continue;
                        }
                        next[to] = Math.min(next[to], prev[k]);
                    }
                    int[] tmp = prev;
                    prev = next;
                    next = tmp;
//                    debug.debug("i", i);
//                    debug.debug("j", j);
//                    for (int k = 0; k < all.length; k++) {
//                        debug.debug(toString(all[k]), prev[k]);
//                    }
                }
                Arrays.fill(next, inf);
                for (int k = 0; k < all.length; k++) {
                    if (prev[k] == inf) {
                        continue;
                    }
                    int state = all[k];
                    int front = get(state, j);
                    int top = get(state, j + 1);
                    if (front != 0 && top != 0) {
                        //reverse
                        int to = set(state, j, 0, 0);
                        if (front == top) {
                            to = inverse(to, match[j + (front == 3 ? 1 : 0)][k]);
                        }
                        if (front == 3 && top == 1 && !(i == rbx && j == rby)) {
                        } else {
                            assert map.containKey(to);
                            to = map.get(to);
                            next[to] = Math.min(next[to], prev[k] + cc);
                        }
                    } else if (Integer.signum(front) != Integer.signum(top)) {
                        int v = Math.max(front, top);
                        //to r
                        {
                            int cost = prev[k] + rc;
                            if (top != 0) {
                                cost += cc;
                            }
                            int to = set(state, j, 0, v);
                            assert map.containKey(to);
                            to = map.get(to);
                            next[to] = Math.min(next[to], cost);
                        }
                        //to b
                        {
                            int cost = prev[k] + dc;
                            if (front != 0) {
                                cost += cc;
                            }
                            int to = set(state, j, v, 0);
                            assert map.containKey(to);
                            to = map.get(to);
                            next[to] = Math.min(next[to], cost);
                        }
                    } else {
                        //rb
                        int to = set(state, j, 3, 1);
                        assert map.containKey(to);
                        to = map.get(to);
                        next[to] = Math.min(next[to], prev[k] + rc + dc + cc);
                    }
                }
                int[] tmp = prev;
                prev = next;
                next = tmp;
//                debug.debug("i", i);
//                debug.debug("j", j);
//                for (int k = 0; k < all.length; k++) {
//                    debug.debug(toString(all[k]), prev[k]);
//                }
            }
        }
        assert map.get(0) == 0;
        int ans = prev[0];
        return ans >= inf ? -1 : ans;
    }

    public int construct(int R, int C, int[] costRight, int[] costDown, int[] costCorner) {
        int ans = count(costRight, costDown, costCorner, R, C);
        return ans;
    }
}
