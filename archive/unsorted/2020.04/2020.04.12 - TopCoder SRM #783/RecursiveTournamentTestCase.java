package contest;

import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.math.Radix;
import template.rand.RandomWrapper;

import java.util.*;

public class RecursiveTournamentTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> ans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ans.add(gen());
        }
        return ans;
    }

    RandomWrapper rw = new RandomWrapper(new Random(0));

    public NewTopCoderTest gen() {
        int n = rw.nextInt(3, 3);
        int k = rw.nextInt(20, 20);
        boolean[][] next = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (rw.nextInt(0, 1) == 0) {
                    next[i][j] = true;
                    next[j][i] = false;
                } else {
                    next[i][j] = false;
                    next[j][i] = true;
                }
            }
        }

        String[] in = new String[n];

        for (int i = 0; i < n; i++) {
            in[i] = format(next[i]);
        }


        int ans = new Standard().count(in.clone(), k);
        return new NewTopCoderTest(new Object[]{in, k}, ans);
    }

    public String format(boolean[] edge) {
        int n = edge.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (edge[i]) {
                builder.append('Y');
            } else {
                builder.append('N');
            }
        }
        return builder.toString();
    }

//    public int dfs(int[] edges, int root, int whole) {
//        int n = edges.length;
//        int mask = 1 << root;
//        while (true) {
//            int sp = mask;
//            for (int i = 0; i < n; i++) {
//                if (Bits.bitAt(mask, i) == 0 || Bits.bitAt(whole, i) == 0) {
//                    continue;
//                }
//                mask = (mask | edges[i]) & whole;
//            }
//            if (sp == mask) {
//                break;
//            }
//        }
//        return mask;
//    }

    public static class Standard {

        int mod = 998244353;
        int b;
        int k;
        long ans;
        long[] s;
        boolean[][] mat;

        int max(int[] arr) {
            int ret = arr[0];
            for (int x : arr) ret = Math.max(x, ret);
            return ret;
        }

        long mod_exp(long b, long e, long mod) {
            long res = 1;
            while (e > 0) {
                if ((e & 1) == 1)
                    res = (res * b) % mod;
                b = (b * b) % mod;
                e >>= 1;
            }
            return res % mod;
        }

        public void dfs(int[] part, int mask, int idx) {
            if (idx == b) {
                if (Integer.bitCount(mask) > 1 && max(part) == 1) {
                    ans += s[Integer.bitCount(mask)];
                    if (ans >= mod) ans -= mod;
                }
                return;
            }

            dfs(Arrays.copyOf(part, part.length), mask, idx + 1);
            if (mask == 0) {
                part[idx] = 1;
                dfs(part, mask | (1 << idx), idx + 1);
                return;
            }
            int lowest = b, highest = 0;
            for (int i = 0; i < idx; i++) {
                if (((mask >> i) & 1) == 1) {
                    if (mat[i][idx]) lowest = Math.min(lowest, part[i]);
                    else highest = Math.max(highest, part[i]);
                }
            }
            if (lowest == highest) {
                part[idx] = lowest;
            } else if (lowest > highest) {
                // highest+1 == lowest
                for (int i = 0; i < idx; i++) {
                    if (part[i] >= lowest) part[i]++;
                }
                part[idx] = highest + 1;
            } else { // lowest < highest
                for (int i = 0; i < idx; i++) {
                    if (part[i] >= lowest) {
                        if (part[i] > highest) {
                            part[i] += lowest - highest;
                        } else {
                            part[i] = lowest;
                        }
                    }
                }
                part[idx] = lowest;
            }
            dfs(part, mask | (1 << idx), idx + 1);
        }

        public int count(String[] graph, int k) {
            b = graph.length;
            mat = new boolean[b][b];
            for (int i = 0; i < b; i++) {
                for (int j = 0; j < b; j++) {
                    mat[i][j] = graph[i].charAt(j) == 'Y';
                }
            }
            s = new long[b + 1];
            for (int j = 0; j < k; j++) {
                long r = mod_exp(b, k - j - 1, mod);
                long mult = mod_exp(2, mod_exp(b, j, mod - 1), mod) - 1;
                for (int i = 0; i <= b; i++) {
                    s[i] = (s[i] + r) % mod;
                    r = r * mult % mod;
                }
            }
            ans = mod_exp(b, k, mod);
            dfs(new int[b], 0, 0);
            return (int) ans;
        }

    }
    
//    public boolean containEdge(boolean[][] next, int a, int b, Radix radix, int k) {
//        if (a == b) {
//            return false;
//        }
//        int index = -1;
//        for (int i = k - 1; i >= 0; i--) {
//            if (radix.get(a, i) != radix.get(b, i)) {
//                index = i;
//                break;
//            }
//        }
//        return next[radix.get(a, index)][radix.get(b, index)];
//    }
//
//    public long solve(boolean[][] next, int k) {
//        int n = next.length;
//        int nk = 1;
//        for (int i = 1; i <= k; i++) {
//            nk *= n;
//        }
//        int[] edges = new int[nk];
//        Radix radix = new Radix(n);
//        for (int i = 0; i < nk; i++) {
//            for (int j = 0; j < nk; j++) {
//                if (containEdge(next, i, j, radix, k)) {
//                    edges[i] = Bits.setBit(edges[i], j, true);
//                }
//            }
//        }
//        int ans = 0;
//        for (int i = 1; i < (1 << nk); i++) {
//            boolean valid = true;
//            for (int j = 0; j < nk; j++) {
//                if (Bits.bitAt(i, j) == 1) {
//                    valid = valid && dfs(edges, j, i) == i;
//                }
//            }
//            if (valid) {
//                ans++;
//            }
//        }
//        return ans;
//    }
}
