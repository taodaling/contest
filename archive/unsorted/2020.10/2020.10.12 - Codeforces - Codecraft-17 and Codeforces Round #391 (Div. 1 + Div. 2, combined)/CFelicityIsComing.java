package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.math.Factorial;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.HashData;

import java.io.PrintWriter;

public class CFelicityIsComing {
    int mod = (int) (1e9 + 7);
    Factorial fact = new Factorial((int) 1e6, mod);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();

        HashData hd1 = new HashData((int) 1e6, mod, 31);
        HashData hd2 = new HashData((int) 1e6, mod, 61);
        long[] hash1 = new long[m];
        long[] hash2 = new long[m];

        for (int i = 0; i < n; i++) {
            int g = in.readInt();
            for (int j = 0; j < g; j++) {
                int t = in.readInt() - 1;
                hash1[t] += hd1.pow[i];
                hash2[t] += hd2.pow[i];
            }
        }

        for (int i = 0; i < m; i++) {
            hash1[i] %= mod;
            hash2[i] %= mod;
        }

        LongHashMap cntMap = new LongHashMap(m, false);
        for (int i = 0; i < m; i++) {
            long key = DigitUtils.asLong((int) hash1[i], (int) hash2[i]);
            cntMap.put(key, cntMap.getOrDefault(key, 0) + 1);
        }

        long ans = 1;
        for (LongEntryIterator iterator = cntMap.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int v = (int) iterator.getEntryValue();
            ans = ans * fact.fact(v) % mod;
        }

        out.println(ans);
    }
}
