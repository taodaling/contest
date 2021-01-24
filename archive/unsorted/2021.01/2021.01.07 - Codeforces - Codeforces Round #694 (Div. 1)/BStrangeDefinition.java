package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.DenseMultiSetHasher;
import template.rand.MultiSetHasher;
import template.rand.SparseMultiSetHasher;

public class BStrangeDefinition {
    MultiSetHasher hasher = new SparseMultiSetHasher((int) 1e6);
    LongHashMap map = new LongHashMap((int) 1e6, false);
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve((int) 1e6);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        map.clear();
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        int[] factors = sieve.getSmallestPrimeFactor();

        for (int x : a) {
            long sum = 0;
            while (x != 1) {
                int factor = factors[x];
                int xor = 0;
                while (x % factor == 0) {
                    x /= factor;
                    xor ^= 1;
                }
                if (xor == 1) {
                    sum = hasher.merge(sum, hasher.hash(factor));
                }
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        long max = 0;
        long sumOfZero = 0;
        for (LongEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            long k = iterator.getEntryKey();
            long v = iterator.getEntryValue();
            max = Math.max(v, max);
            if (k == 0 || v % 2 == 0) {
                sumOfZero += v;
            }
        }
        long max2 = Math.max(max, sumOfZero);
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            long w = in.rl();
            if (w == 0) {
                out.println(max);
            } else {
                out.println(max2);
            }
        }
    }
}
