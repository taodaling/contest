package on2020_04.on2020_04_12_TopCoder_SRM__783.RecursiveTournament;



import template.binary.Bits;
import template.math.Modular;
import template.math.Power;
import template.utils.Debug;

import java.util.Arrays;
import java.util.BitSet;

public class RecursiveTournament {
    Debug debug = new Debug(true);

    public int count(String[] graph, int k) {
        int n = graph.length;
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (graph[i].charAt(j) == 'Y') {
                    next[i] = Bits.setBit(next[i], j, true);
                }
            }
            next[i] = Bits.setBit(next[i], i, true);
        }
        int[] level1 = new int[1 << n];
        level1[0] = (1 << n) - 1;
        for (int i = 1; i < level1.length; i++) {
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 0) {
                    continue;
                }
                if ((next[j] & level1[Bits.setBit(i, j, false)]) != 0) {
                    level1[i] = Bits.setBit(level1[i], j, true);
                }
            }
        }
        int[] cnt = new int[n + 1];
        for (int i = 1; i < level1.length; i++) {
            if (level1[i] != i) {
                continue;
            }
            debug.debug("scc", Integer.toString(i, 2));
            cnt[Integer.bitCount(i)]++;
        }

        Modular mod = new Modular(998244353);
        Power power = new Power(mod);


        int ans = power.pow(n, k);
        debug.debug("local", ans);
        for (int i = 1; i <= k; i++) {
            int children = power.pow(n, i - 1);
            int tail = mod.subtract(power.pow(2, children), 1);
            int head = power.pow(n, k - i);
            int local = 0;
            for (int j = 2; j <= n; j++) {
                int contrib = power.pow(tail, j);
                contrib = mod.mul(contrib, cnt[j]);
                local = mod.plus(local, contrib);
            }
            debug.debug("local", local);
            ans = mod.plus(ans, mod.mul(head, local));
        }


//        debug.debug("next", next);
//        debug.debug("level1", level1);
        debug.debug("cnt", cnt);
//        debug.debug("dp", dp);
        return ans;
    }

//    int mod = 998244353;
//    int b;
//    int k;
//    long ans;
//    long[] s;
//    boolean[][] mat;
//
//    int max(int[] arr) {
//        int ret = arr[0];
//        for (int x : arr) ret = Math.max(x, ret);
//        return ret;
//    }
//
//    long mod_exp(long b, long e, long mod) {
//        long res = 1;
//        while (e > 0) {
//            if ((e & 1) == 1)
//            res = (res * b) % mod;
//            b = (b * b) % mod;
//            e >>= 1;
//        }
//        return res % mod;
//    }
//
//    public void dfs(int[] part, int mask, int idx) {
//        if (idx == b) {
//            if (Integer.bitCount(mask) > 1 && max(part) == 1) {
//                ans += s[Integer.bitCount(mask)];
//                if (ans >= mod) ans -= mod;
//            }
//            return;
//        }
//
//        dfs(Arrays.copyOf(part, part.length), mask, idx+1);
//        if (mask == 0) {
//            part[idx] = 1;
//            dfs(part, mask | (1 << idx), idx+1);
//            return;
//        }
//        int lowest = b, highest = 0;
//        for (int i = 0; i < idx; i++) {
//            if (((mask >> i) & 1) == 1) {
//                if (mat[i][idx]) lowest = Math.min(lowest, part[i]);
//                else highest = Math.max(highest, part[i]);
//            }
//        }
//        if (lowest == highest) {
//            part[idx] = lowest;
//        } else if (lowest > highest) {
//            // highest+1 == lowest
//            for (int i = 0; i < idx; i++) {
//                if (part[i] >= lowest) part[i]++;
//            }
//            part[idx] = highest + 1;
//        } else { // lowest < highest
//            for (int i = 0; i < idx; i++) {
//                if (part[i] >= lowest) {
//                    if (part[i] > highest) {
//                        part[i] += lowest - highest;
//                    } else {
//                        part[i] = lowest;
//                    }
//                }
//            }
//            part[idx] = lowest;
//        }
//        dfs(part, mask | (1 << idx), idx + 1);
//    }
//
//    public int count(String[] graph, int k) {
//        b = graph.length;
//        mat = new boolean[b][b];
//        for (int i = 0; i < b; i++) {
//            for (int j = 0; j < b; j++) {
//                mat[i][j] = graph[i].charAt(j) == 'Y';
//            }
//        }
//        s = new long[b + 1];
//        for (int j = 0; j < k; j++) {
//            long r = mod_exp(b, k - j - 1, mod);
//            long mult = mod_exp(2, mod_exp(b, j, mod - 1), mod) - 1;
//            for (int i = 0; i <= b; i++) {
//                s[i] = (s[i] + r) % mod;
//                r = r * mult % mod;
//            }
//        }
//        ans = mod_exp(b, k, mod);
//        dfs(new int[b], 0, 0);
//        return (int)ans;
//    }
}
