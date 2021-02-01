package template.problem;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.CompareUtils;

import java.util.Arrays;

public class CountGridHamiltonPath {
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

//    Debug debug = new Debug(true);

    /**
     * mat[i][j] means whether we can pass it. O(nm3^m)
     *
     * @param mat
     * @return
     */
    public long count(boolean[][] mat) {
        if (mat.length < mat[0].length) {
            mat = rotate(mat);
        }
        n = mat.length;
        m = mat[0].length;
        int rbx = -1;
        int rby = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j]) {
                    rbx = i;
                    rby = j;
                }
            }
        }

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
        long[] prev = new long[all.length];
        long[] next = new long[all.length];
        prev[0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j == 0) {
                    Arrays.fill(next, 0);
                    for (int k = 0; k < all.length; k++) {
                        if(prev[k] == 0){
                            continue;
                        }
                        int to = shift[k];
                        if (to == -1) {
                            continue;
                        }
                        next[to] += prev[k];
                    }
                    long[] tmp = prev;
                    prev = next;
                    next = tmp;
//                    debug.debug("i", i);
//                    debug.debug("j", j);
//                    for (int k = 0; k < all.length; k++) {
//                        debug.debug(toString(all[k]), prev[k]);
//                    }
                }
                Arrays.fill(next, 0);
                for (int k = 0; k < all.length; k++) {
                    if(prev[k] == 0){
                        continue;
                    }
                    int state = all[k];
                    int front = get(state, j);
                    int top = get(state, j + 1);
                    if (!mat[i][j]) {
                        if (front == 0 && top == 0) {
                            next[k] += prev[k];
                        }
                        continue;
                    }
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
                            next[to] += prev[k];
                        }
                    } else if (Integer.signum(front) != Integer.signum(top)) {
                        int v = Math.max(front, top);
                        //to r
                        {
                            int to = set(state, j, 0, v);
                            assert map.containKey(to);
                            to = map.get(to);
                            next[to] += prev[k];
                        }
                        //to b
                        {
                            int to = set(state, j, v, 0);
                            assert map.containKey(to);
                            to = map.get(to);
                            next[to] += prev[k];
                        }
                    } else {
                        //rb
                        int to = set(state, j, 3, 1);
                        assert map.containKey(to);
                        to = map.get(to);
                        next[to] += prev[k];
                    }
                }
                long[] tmp = prev;
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
        long ans = prev[0];
        return ans;
    }
}
