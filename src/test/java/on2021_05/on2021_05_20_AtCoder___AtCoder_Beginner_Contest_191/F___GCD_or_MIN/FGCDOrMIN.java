package on2021_05.on2021_05_20_AtCoder___AtCoder_Beginner_Contest_191.F___GCD_or_MIN;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.util.Arrays;

public class FGCDOrMIN {

    IntegerHashMap map = new IntegerHashMap((int) 5e6, false);

    void update(int i, int x){
        int v = map.get(i);
        map.put(i, GCDs.gcd(x, v));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for (int x : a) {
            for (int i = 1; i * i <= x; i++) {
                if (x % i != 0) {
                    continue;
                }
                int div = x / i;
                update(i, x);
                if(div != i){
                    update(div, x);
                }
            }
        }
        int min = Arrays.stream(a).min().orElse(-1);
        int ans = 0;
        for(IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ){
            iterator.next();
            int k = iterator.getEntryKey();
            int v = iterator.getEntryValue();
            if(k == v && k <= min){
                ans++;
            }
        }
        out.println(ans);
    }
}
