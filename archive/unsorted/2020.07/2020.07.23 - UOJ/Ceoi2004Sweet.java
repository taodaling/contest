package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ExtLucas;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerIterator;
import template.utils.Debug;

public class Ceoi2004Sweet {
    Modular mod = new Modular(2004);
    ExtLucas lucas = new ExtLucas(mod.getMod(), (long) 1e18);

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int[] ms = new int[n + 1];
        for (int i = 0; i < n; i++) {
            ms[i] = in.readInt();
        }
        ms[n] = b - a;
        debug.debug("ms", ms);

        IntegerHashMap[] top = new IntegerHashMap[n + 1];
        for (int i = 0; i <= n; i++) {
            top[i] = new IntegerHashMap(2, false);
            top[i].put(0, 1);
            top[i].put(ms[i] + 1, mod.valueOf(-1));
        }

        IntegerHashMap topMerge = top[0];
        for(int i = 1; i <= n; i++){
            topMerge = mul(topMerge, top[i]);
        }

        int k = n;
        int ans = 0;
        for(IntegerEntryIterator iterator = topMerge.iterator(); iterator.hasNext(); ){
            iterator.next();
            int i = iterator.getEntryKey();
            int ai = iterator.getEntryValue();
            int j = b - i;
            if(j < 0){
                continue;
            }
            int bi = lucas.combination(k + j, j);
            int contrib = mod.mul(ai, bi);
            ans = mod.plus(ans, contrib);
        }

        out.println(ans);
    }

    public IntegerHashMap mul(IntegerHashMap a, IntegerHashMap b) {
        IntegerHashMap ans = new IntegerHashMap(a.size() * b.size(), false);
        for (IntegerEntryIterator bi = b.iterator(); bi.hasNext(); ) {
            bi.next();
            for (IntegerEntryIterator ai = a.iterator(); ai.hasNext(); ) {
                ai.next();
                int exp = bi.getEntryKey() + ai.getEntryKey();
                int val = mod.mul(bi.getEntryValue(), ai.getEntryValue());
                val = mod.plus(val, ans.getOrDefault(exp, 0));
                ans.put(exp, val);
            }
        }
        return ans;
    }

}
