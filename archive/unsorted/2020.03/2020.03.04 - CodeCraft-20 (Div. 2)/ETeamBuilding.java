package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ETeamBuilding {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int k = in.readInt();
        int status = 1 << p;
        int mask = status - 1;

        Person[] people = new Person[n + 1];
        for (int i = 1; i <= n; i++) {
            people[i] = new Person();
            people[i].s = new int[p];
            people[i].a = in.readInt();
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < p; j++) {
                people[i].s[j] = in.readInt();
            }
        }

        Arrays.sort(people, 1, n + 1, (a, b) -> -Integer.compare(a.a, b.a));

        long[][] l2r = new long[n + 1][status];
        long[][] r2l = new long[n + 2][status];

        SequenceUtils.deepFill(l2r, -(long) 1e18);
        SequenceUtils.deepFill(r2l, -(long) 1e18);
        l2r[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < status; j++) {
                l2r[i][j] = l2r[i - 1][j];
                for (int t = 0; t < p; t++) {
                    if (Bits.bitAt(j, t) == 0) {
                        continue;
                    }
                    l2r[i][j] = Math.max(l2r[i - 1][j - (1 << t)] + people[i].s[t] - people[i].a, l2r[i][j]);
                }
            }
        }

        r2l[n + 1][0] = 0;
        for (int i = n; i >= 1; i--) {
            for (int j = 0; j < status; j++) {
                r2l[i][j] = r2l[i + 1][j];
                for (int t = 0; t < p; t++) {
                    if (Bits.bitAt(j, t) == 0) {
                        continue;
                    }
                    r2l[i][j] = Math.max(r2l[i + 1][j - (1 << t)] + people[i].s[t], r2l[i][j]);
                }
            }
        }

        long[] sum = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = people[i].a;
        }

        debug.debug("l2r", l2r);
        debug.debug("r2l", r2l);
        long ans = (long) -1e18;
        LongPreSum ps = new LongPreSum(sum);
        for (int i = 0; i <= p; i++) {
            int l = i + k;
            int r = l + 1;
            long total = ps.intervalSum(1, l);
            long best = (long) -1e18;
            for (int j = 0; j < status; j++) {
                if (Integer.bitCount(j) != i) {
                    continue;
                }
                int t = mask - j;
                best = Math.max(best, l2r[l][j] + r2l[r][t]);
            }
            ans = Math.max(ans, total + best);
            debug.debug("i", i);
            debug.debug("total + best", total + best);
        }

        out.println(ans);
    }
}

class Person {
    int a;
    int[] s;
}
