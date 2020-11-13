package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.Arrays;

public class MeetInTheMiddle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        int lh = n / 2;
        int rh = n - lh;

        long ans = 0;
        LongHashMap left = gen(Arrays.copyOf(a, lh));
        LongHashMap right = gen(Arrays.copyOfRange(a, lh, n));
        for (LongEntryIterator iterator = left.iterator(); iterator.hasNext(); ) {
            iterator.next();
            long key = iterator.getEntryKey();
            long value = iterator.getEntryValue();
            ans += value * right.get(x - key);
        }
        out.println(ans);
    }

    LongHashMap gen(int[] set) {
        int n = set.length;
        LongHashMap map = new LongHashMap(1 << n, false);
        for (int i = 0; i < 1 << n; i++) {
            long sum = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    sum += set[j];
                }
            }
            map.put(sum, map.get(sum) + 1);
        }
        return map;
    }
}
