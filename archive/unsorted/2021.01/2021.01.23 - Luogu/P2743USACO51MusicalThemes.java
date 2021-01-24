package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.HashData;
import template.rand.RollingHash;

import java.util.function.IntPredicate;

public class P2743USACO51MusicalThemes {
    int mod = (int) 1e9 + 7;

    public long merge(long a, long b, int mod, int mul) {
        long ah = DigitUtils.highBit(a);
        long al = DigitUtils.lowBit(a);
        long bh = DigitUtils.highBit(b);
        long bl = DigitUtils.lowBit(b);
        ah -= bh * mul;
        al -= bl * mul;
        ah = DigitUtils.mod(ah, mod);
        al = DigitUtils.mod(al, mod);
        return DigitUtils.asLong(ah, al);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        LongHashMap set = new LongHashMap(n, false);
        HashData[] hd = HashData.doubleHashData(n);
        RollingHash rh = new RollingHash(hd[0], hd[1], n);
        RollingHash rh2 = new RollingHash(hd[0], hd[1], n);
        IntegerMinQueue minQueue = new IntegerMinQueue(n, IntegerComparator.NATURE_ORDER);
        IntPredicate predicate = m -> {
            rh.clear();
            rh2.clear();
            set.clear();
            minQueue.clear();
            for (int i = 0; i < n; i++) {
                rh.addLast(a[i]);
                minQueue.addLast(a[i]);
                if (i < m) {
                    rh2.addLast(1);
                }
                if (minQueue.size() == m) {
                    long hash = merge(rh.hash(), rh2.hash(), mod, minQueue.min());
                    long exist = set.getOrDefault(hash, -1);
                    if (exist >= 0 && i - exist >= m) {
                        return false;
                    }
                    if (exist == -1) {
                        set.put(hash, i);
                    }
                    minQueue.removeFirst();
                    rh.removeFirst();
                }
            }
            return true;
        };
        //predicate.test(1);
        int ans = BinarySearch.lastFalse(predicate, 1, n);
        out.println(ans >= 5 ? ans : 0);
    }
}
