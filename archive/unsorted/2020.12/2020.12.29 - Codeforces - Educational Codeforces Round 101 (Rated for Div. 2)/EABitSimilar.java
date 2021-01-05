package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.HashData;
import template.rand.IntRangeHash;
import template.rand.ModifiableHash;

public class EABitSimilar {
    int limit = (int) 1e6;
    int mod = (int) 1e9 + 7;
    HashData hd1 = new HashData(limit, mod, 31);
    HashData hd2 = new HashData(limit, mod, 13);
    IntRangeHash rh = new IntRangeHash(hd1, hd2, limit);
    LongHashSet[] hs = new LongHashSet[]{new LongHashSet(limit, false), new LongHashSet(limit, false)};
    ModifiableHash mh = new ModifiableHash(hd1, hd2, limit);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        char[] s = new char[n];
        for (int i = 0; i < n; i++) {
            s[i] = (char) (in.rc() - '0');
        }
        rh.populate(i -> 1 - s[i], n);
        for (int i = 0; i < 2; i++) {
            hs[i].clear();
        }
        mh.init(k - 1);
        for (int i = 0; i + k <= n; i++) {
            int l = i;
            int r = i + k - 1;
            hs[s[r]].add(rh.hash(l, r - 1));
        }

        if (!minHash(0, k - 1)) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < k - 1; i++) {
            out.append(mh.get(i));
        }
        if (hs[1].contain(mh.hash())) {
            out.append(1);
        } else {
            out.append(0);
        }
        out.println();
    }

    public boolean minHash(int i, int n) {
        if (i >= n) {
            long h = mh.hash();
            return !hs[0].contain(h) || !hs[1].contain(h);
        }
        mh.set(i, 0);
        if (minHash(i + 1, n)) {
            return true;
        }
        mh.set(i, 1);
        return minHash(i + 1, n);
    }
}
