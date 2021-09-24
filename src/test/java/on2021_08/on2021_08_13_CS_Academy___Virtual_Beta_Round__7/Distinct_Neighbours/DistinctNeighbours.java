package on2021_08.on2021_08_13_CS_Academy___Virtual_Beta_Round__7.Distinct_Neighbours;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DistinctNeighbours {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Factorial factorial = new Factorial(n, mod);
        int[] a = in.ri(n);
        int[] cnt = new int[n];
        for (int i = 0; i < n; i++) {
            a[i]--;
            cnt[a[i]]++;
        }
        int maxOccur = Arrays.stream(cnt).max().orElse(-1);
        if (maxOccur + maxOccur - 1 > n) {
            out.println(0);
            return;
        }
        int maxAllow = n / 2;
        long[] prevOuter = new long[maxAllow + 1];
        long[] nextOuter = new long[maxAllow + 1];
        long[][] prevInner = new long[maxAllow + 1][maxOccur + 1];
        long[][] nextInner = new long[maxAllow + 1][maxOccur + 1];
        int ps = 0;
        prevOuter[0] = 1;
        for (int c : cnt) {
            if (c == 0) {
                continue;
            }
            debug.debug("c", c);
            debug.debug("prevOuter", prevOuter);
            Arrays.fill(nextOuter, 0);
            SequenceUtils.deepFill(prevInner, 0L);
            for (int i = 0; i <= maxAllow; i++) {
                prevInner[i][0] = prevOuter[i];
            }
            for (int r = 1; r <= c; r++) {
                debug.debug("r", r - 1);
                debug.debug("prevInner", prevInner);
                SequenceUtils.deepFill(nextInner, 0L);
                for (int i = 0; i <= maxAllow; i++) {
                    for (int j = 0; j < r; j++) {
                        long way = prevInner[i][j];
                        if (way == 0) {
                            continue;
                        }
                        //ps + 1 insertion pos
                        //i is cut point

                        //cut one
                        if (i > 0) {
                            nextInner[i - 1][j + 1] += way * i;
                        }
                        int remain = ps + 1 - i - j;
                        //cut a new
                        if (remain > 0) {
                            nextInner[i][j + 1] += way * remain;
                        }
                        //cut exist
                        nextInner[i][j] += way * (r - 1 + j);
                    }
                }

                for (int i = 0; i <= maxAllow; i++) {
                    for (int j = 0; j <= r; j++) {
                        nextInner[i][j] %= mod;
                    }
                }

                long[][] tmp = prevInner;
                prevInner = nextInner;
                nextInner = tmp;
            }

            debug.debug("r", c);
            debug.debug("prevInner", prevInner);
            for (int i = 0; i <= maxAllow; i++) {
                for (int j = 0; j <= c; j++) {
                    long way = prevInner[i][j];
                    if (way == 0) {
                        continue;
                    }
                    int add = c - j;
                    if (i + add <= maxAllow) {
                        nextOuter[i + add] += way * factorial.invFact(c) % mod;
                    }
                }
            }
            for (int i = 0; i <= maxAllow; i++) {
                nextOuter[i] %= mod;
            }
            long[] tmp = prevOuter;
            prevOuter = nextOuter;
            nextOuter = tmp;
            ps += c;
        }

        debug.log("end");
        debug.debug("prevOuter", prevOuter);
        long ans = prevOuter[0];
        out.println(ans);
    }
}
