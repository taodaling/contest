package contest;

import com.sun.media.sound.DLSSample;
import template.binary.Bits;
import template.binary.Log2;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.ModPrimeRoot;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.*;

public class AllInclusiveString {
    boolean[][] edge;
    boolean[] visited;
    boolean[] instk;
    int n;
    //Debug debug = new Debug(true);

    public int[] shortest(String[] a) {
        int m = a.length;
        boolean[] used = new boolean['z' - 'a' + 1];
        for (String s : a) {
            for (int i = 0; i < 2; i++) {
                used[s.charAt(i) - 'a'] = true;
            }
        }

        IntegerPreSum ips = new IntegerPreSum(i -> used[i] ? 1 : 0, used.length);
        int[][] conn = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                conn[i][j] = ips.prefix(a[i].charAt(j) - 'a') - 1;
            }
        }
        n = ips.prefix(used.length);
        edge = new boolean[n][n];
        visited = new boolean[n];
        instk = new boolean[n];


        boolean[] valid = new boolean[1 << n];
        for (int s = 0; s < (1 << n); s++) {
            SequenceUtils.deepFill(edge, false);
            SequenceUtils.deepFill(visited, false);
            SequenceUtils.deepFill(instk, false);

            for (int[] c : conn) {
                if (Bits.bitAt(s, c[0]) + Bits.bitAt(s, c[1]) == 0) {
                    edge[c[0]][c[1]] = true;
                }
            }

            valid[s] = true;
            for (int i = 0; i < n; i++) {
                if (Bits.bitAt(s, i) == 1) {
                    continue;
                }
                valid[s] = valid[s] && !dfs(i);
            }
        }

        int inf = (int) 1e9;
        int min = inf;
        for (int i = 0; i < valid.length; i++) {
            if (!valid[i]) {
                continue;
            }
            int cost = n + Integer.bitCount(i);
            min = Math.min(min, cost);
        }

        int cnt = 0;
        for (int i = 0; i < valid.length; i++) {
            if (!valid[i]) {
                continue;
            }
            int cost = n + Integer.bitCount(i);
            if (cost != min) {
                continue;
            }
            int way = solve(conn, i);
//            debug.debug("i", Integer.toBinaryString(i));
//            debug.debug("way", way);
            cnt = mod.plus(cnt, way);
        }

        return new int[]{min, cnt};
    }

    Modular mod = new Modular(998244353);

    int[] mask = new int[32];
    DSU dsu = new DSU(32);

    public int solve(int[][] conn, int s) {

        dsu.reset();
        Arrays.fill(mask, 0);
        for (int[] c : conn) {
            int a = c[0];
            int b = c[1];
            if (Bits.bitAt(s, b) == 1) {
                b += n;
            }
            mask[a] |= 1 << b;
            dsu.merge(a, b);
        }
        for (int i = 0; i < n; i++) {
            if (Bits.bitAt(s, i) == 1) {
                mask[i] |= 1 << (i + n);
                dsu.merge(i, i + n);
            }
        }

        int ans = 1;
        int remain = Integer.bitCount(s) + n;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            int x = dsu.mask[dsu.find(i)];
            int way = solve(mask, x);
            way = mod.mul(way, comb.combination(remain, Integer.bitCount(x)));
            remain -= Integer.bitCount(x);
            ans = mod.mul(ans, way);
        }

        return ans;
    }

    Combination comb = new Combination(100, mod);
    LongList[] lists = new LongList[]{new LongList(1000000), new LongList(1000000)};

    public int solve(int[] mask, int s) {
        int m = Integer.bitCount(s);
        LongList last = lists[0];
        LongList cur = lists[1];
        int log = Log2.floorLog(Integer.highestOneBit(s));
        last.clear();
        last.add(DigitUtils.asLong(s, 1));
        for (int i = m; i > 0; i--) {
            cur.clear();
            last.sort();
            long[] data = last.getData();
            int size = last.size();
            for (int j = 0; j < size; j++) {
                int high = DigitUtils.highBit(data[j]);
                int cnt = DigitUtils.lowBit(data[j]);
                int r = j;
                while (r + 1 < size && high == DigitUtils.highBit(data[r + 1])) {
                    r++;
                    cnt = mod.plus(cnt, DigitUtils.lowBit(data[r]));
                }
                j = r;
                for (int k = 0; k <= log; k++) {
                    if (Bits.bitAt(high, k) == 0 || (mask[k] & high) != 0) {
                        continue;
                    }
                    cur.add(DigitUtils.asLong(Bits.setBit(high, k, false), cnt));
                }
            }
            LongList tmp = cur;
            cur = last;
            last = tmp;
        }

        int ans = 0;
        for (int i = 0; i < last.size(); i++) {
            ans = mod.plus(ans, DigitUtils.lowBit(last.get(i)));
        }
        return ans;
    }

    public boolean dfs(int root) {
        if (visited[root]) {
            return instk[root];
        }
        visited[root] = instk[root] = true;

        for (int i = 0; i < n; i++) {
            if (!edge[root][i]) {
                continue;
            }
            if (dfs(i)) {
                return true;
            }
        }

        instk[root] = false;
        return false;
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    int[] mask;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        mask = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            mask[i] = 1 << i;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }


    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        mask[a] |= mask[b];
        p[b] = a;
    }
}
