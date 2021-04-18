package on2021_03.on2021_03_25_Codeforces___Coder_Strike_2014___Finals__online_edition__Div__1_.C__Bug_in_Code;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.Randomized;

import java.util.Arrays;

public class CBugInCode {

    public long id(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        LongHashMap map = new LongHashMap(n, false);
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            size[a]++;
            size[b]++;
            map.modify(id(a, b), 1);
        }
        int[] sizeSorted = size.clone();
        Randomized.shuffle(sizeSorted);
        Arrays.sort(sizeSorted);
        long ans = 0;
        for (int i = 0, r = n; i < n; i++) {
            while (r - 1 >= 0 && r - 1 >= i + 1 && sizeSorted[i] + sizeSorted[r - 1] >= p) {
                r--;
            }
            r = Math.max(r, i + 1);
            int cand = n - r;
            ans += cand;
        }
        for (LongEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            long key = iterator.getEntryKey();
            long value = iterator.getEntryValue();
            int a = DigitUtils.highBit(key);
            int b = DigitUtils.lowBit(key);
            if (size[a] + size[b] >= p && size[a] + size[b] - value < p) {
                ans--;
            }
        }
        out.println(ans);
    }
}
