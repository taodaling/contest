package on2021_01.on2021_01_02_Codeforces___Good_Bye_2020.G__Song_of_the_Sirens;



import template.datastructure.ModPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.DigitUtils;
import template.math.Power;
import template.string.AhoCorasick;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class GSongOfTheSirens {
    int mod = (int) 1e9 + 7;
    char[] buf = new char[(int) 3e6];
    int[] topology;
    int[] tag;
    AhoCorasick ac;
    int[] pow2;
    int[] inv2;

    {
        pow2 = new int[(int) 1e6];
        inv2 = new int[(int) 1e6];
        pow2[0] = 1;
        inv2[0] = 1;
        int inv = (mod + 1) / 2;
        for (int i = 1; i < 1e6; i++) {
            pow2[i] = (int) ((long) pow2[i - 1] * 2 % mod);
            inv2[i] = (int) ((long) inv2[i - 1] * inv % mod);
        }
    }

    public void pushUp() {
        for (int i = topology.length - 1; i >= 1; i--) {
            int node = topology[i];
            int p = ac.fails[node];
            tag[p] += tag[node];
            if (tag[p] < 0) {
                tag[p] += mod;
            } else if (tag[p] >= mod) {
                tag[p] -= mod;
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        ac = new AhoCorasick('a', 'z', (int) 1e6);
        char[] s0 = in.rs().toCharArray();
        char[] t = new char[n + 1];
        in.rs(t, 1);
        Query[] qs = new Query[q];
        int limit = (int) 1e6;
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].k = in.ri();
            int m = in.rs(buf, 0);
            ac.prepareBuild();
            for (int j = 0; j < m; j++) {
                ac.build(buf[j]);
            }
            qs[i].node = ac.buildLast;
        }
        topology = ac.endBuild();
        tag = new int[topology.length];
        int len;
        {
            System.arraycopy(s0, 0, buf, 0, s0.length);
            len = s0.length;
            ac.prepareMatch();
            Arrays.fill(tag, 0);
            for (int j = 0; j < len; j++) {
                ac.match(buf[j]);
                tag[ac.matchLast]++;
            }

            pushUp();
            for (Query query : qs) {
                query.ans += (long)tag[query.node] * pow2[query.k] % mod;
            }
        }

        int prevLen = -1;
        int now = 1;
        for (; now <= n && len <= limit; now++) {
            prevLen = len;
            buf[prevLen] = t[now];
            len = len * 2 + 1;
            for (int j = 0; j < prevLen; j++) {
                buf[j + prevLen + 1] = buf[j];
            }

            ac.prepareMatch();
            Arrays.fill(tag, 0);
            for (int j = 0; j < prevLen; j++) {
                ac.match(buf[j]);
                tag[ac.matchLast]--;
            }
            for (int j = prevLen; j < len; j++) {
                ac.match(buf[j]);
                tag[ac.matchLast]++;
            }

            pushUp();
            for (Query query : qs) {
                if (query.k >= now) {
                    query.ans += (long)tag[query.node] * pow2[query.k - now] % mod;
                }
            }
        }

        long[] ps = new long[n + 1];
        for (int c = 'a'; c <= 'z'; c++) {
            Arrays.fill(ps, 0);
            for (int j = now; j <= n; j++) {
                if (t[j] == c) {
                    ps[j] = inv2[j];
                }
            }
            for (int i = 1; i <= n; i++) {
                ps[i] += ps[i - 1];
                if(ps[i] >= mod){
                    ps[i] -= mod;
                }
            }
            buf[prevLen] = (char) c;
            ac.prepareMatch();
            Arrays.fill(tag, 0);
            for (int j = 0; j < prevLen; j++) {
                ac.match(buf[j]);
                tag[ac.matchLast]--;
            }
            for (int j = prevLen; j < len; j++) {
                ac.match(buf[j]);
                tag[ac.matchLast]++;
            }

            pushUp();
            for (Query query : qs) {
                if (query.k >= now) {
                    query.ans += ps[query.k] * pow2[query.k] % mod *
                            tag[query.node] % mod;
                }
            }
        }

        for (Query query : qs) {
            int ans = DigitUtils.mod(query.ans, mod);
            out.println(ans);
        }
    }
}

class Query {
    int k;
    int node;
    long ans;
}