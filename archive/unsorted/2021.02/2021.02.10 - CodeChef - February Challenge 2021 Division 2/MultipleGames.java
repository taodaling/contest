package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;

import java.util.HashMap;
import java.util.Map;

public class MultipleGames {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        LongHashMap map = new LongHashMap(q, false);
        for (int i = 0; i < q; i++) {
            int l = a[in.ri() - 1];
            int r = a[in.ri() - 1];
            int A = l;
            int B = l + r;
            map.modify(DigitUtils.asLong(A, B), 1);
        }
        int[] delta = new int[m + 1];
        for (LongEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int A = DigitUtils.highBit(iterator.getEntryKey());
            int B = DigitUtils.lowBit(iterator.getEntryKey());
            int v = (int) iterator.getEntryValue();
            for (int i = 0; i <= m; i += B) {
                if (i + A <= m) {
                    delta[i + A] += v;
                }
                if (i + B <= m) {
                    delta[i + B] -= v;
                }
            }
        }
        for (int i = 1; i <= m; i++) {
            delta[i] += delta[i - 1];
        }
        int max = 0;
        for(int x : delta){
            max = Math.max(x, max);
        }
        out.println(max);
    }
}
