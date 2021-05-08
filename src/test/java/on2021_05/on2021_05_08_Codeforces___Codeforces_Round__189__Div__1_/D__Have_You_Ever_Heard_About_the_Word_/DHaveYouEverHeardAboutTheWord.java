package on2021_05.on2021_05_08_Codeforces___Codeforces_Round__189__Div__1_.D__Have_You_Ever_Heard_About_the_Word_;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.rand.HashData;
import template.rand.HashSeed;
import template.rand.IntRangeHash;
import template.rand.RollingHash;
import template.utils.Debug;
import template.utils.Pair;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class DHaveYouEverHeardAboutTheWord {
    Debug debug = new Debug(true);
    HashData[] hds = HashData.doubleHashData((int) 1e5);
    IntRangeHash rh = new IntRangeHash(hds[0], hds[1], (int) 1e5);
    RollingHash dqL = new RollingHash(hds[0], hds[1], (int) 1e5);
    RollingHash dqR = new RollingHash(hds[0], hds[1], (int) 1e5);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        rh.populate(i -> s[i], n);
        for (int i = 1; i <= n; i++) {
            if (!exist(s, n, i)) {
                continue;
            }
            //cut
            dqL.clear();
            dqR.clear();
            int wpos = 0;
            for (int j = 0; j < i; j++) {
                dqL.addLast(s[j]);
                dqR.addLast(s[i + j]);
            }
            int rpos = 2 * i;
            while (true) {
                if (dqR.size() < i) {
                    if (rpos >= n) {
                        break;
                    }
                    dqR.addLast(s[rpos++]);
                    continue;
                }
                if (dqR.hash() == dqL.hash()) {
                    dqR.clear();
                    continue;
                }
                s[wpos++] = (char) dqL.removeFirst();
                dqL.addLast(dqR.removeFirst());
            }
            while (dqL.size() > 0) {
                s[wpos++] = (char) dqL.removeFirst();
            }
            while (dqR.size() > 0) {
                s[wpos++] = (char) dqR.removeFirst();
            }
            if (n == wpos) {
                throw new IllegalStateException();
            }
            n = wpos;
            rh.populate(j -> s[j], n);
        }

        for (int i = 0; i < n; i++) {
            out.append(s[i]);
        }
    }

    private boolean exist(char[] s, int n, int d) {
        for (int i = d - 1; i + d < n; i += d) {
            if (s[i] != s[i + d]) {
                continue;
            }
            int l, r;
            l = 1;
            r = d;
            while (l < r) {
                int m = (l + r + 1) / 2;
                if (rh.hash(i - m + 1, i) == rh.hash(i + d - m + 1, i + d)) {
                    l = m;
                } else {
                    r = m - 1;
                }
            }
            int L = l;
            l = 1;
            r = Math.min(d, n - (i + d));
            while (l < r) {
                int m = (l + r + 1) / 2;
                if (rh.hash(i, i + m - 1) == rh.hash(i + d, i + d + m - 1)) {
                    l = m;
                } else {
                    r = m - 1;
                }
            }
            int R = l;
            if (L + R > d) {
                return true;
            }
        }
        return false;
    }

}
