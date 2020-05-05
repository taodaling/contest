package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.PollardRho;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.Arrays;
import java.util.Map;

public class CArpaAndAGameWithMojtaba {
    int[] sg = new int[1 << 20];
    IntegerHashMap map = new IntegerHashMap(1 << 20, false);

    {
        Arrays.fill(sg, -1);
    }

    IntegerVersionArray iva = new IntegerVersionArray(100);

    public int sg(int n) {
        int log = Log2.floorLog(n);
        if (log >= 20) {
            return sgLarge(n);
        }
        if (sg[n] == -1) {
            if (n == 0) {
                return sg[n] = 0;
            }
            for (int i = 1; i <= log + 1; i++) {
                int top = n >> (i - 1);
                int next = (n ^ (top << (i - 1))) | (top >> 1);
                sg(next);
            }
            iva.clear();
            for (int i = 1; i <= log + 1; i++) {
                int top = n >> (i - 1);
                int next = (n ^ (top << (i - 1))) | (top >> 1);
                iva.set(sg(next), 1);
            }
            sg[n] = 0;
            while (iva.get(sg[n]) == 1) {
                sg[n]++;
            }
        }
        return sg[n];
    }

    public int sgLarge(int n) {
        int log = Log2.floorLog(n);
        int ans = map.getOrDefault(n, -1);
        if (ans == -1) {
            for (int i = 1; i <= log + 1; i++) {
                int top = n >> (i - 1);
                int next = (n ^ (top << (i - 1))) | (top >> 1);
                sg(next);
            }
            iva.clear();
            for (int i = 1; i <= log + 1; i++) {
                int top = n >> (i - 1);
                int next = (n ^ (top << (i - 1))) | (top >> 1);
                iva.set(sg(next), 1);
            }
            ans = 0;
            while (iva.get(ans) == 1) {
                ans++;
            }
            map.put(n, ans);
        }
        return ans;
    }

    /**
     * log_a b
     */
    public int log(int a, int b) {
        int ans = 0;
        while (b % a == 0) {
            ans++;
            b /= a;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        IntegerHashMap primes = new IntegerHashMap(1000, false);
        PollardRho pr = new PollardRho();
        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> factors = pr.findAllFactors(a[i]);
            for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
                int p = entry.getKey();
                int cnt = log(p, entry.getValue());
                primes.put(p, primes.getOrDefault(p, 0) | (1 << (cnt - 1)));
            }
        }

        int xor = 0;
        for (IntegerEntryIterator iterator = primes.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int key = iterator.getEntryKey();
            int value = iterator.getEntryValue();

            xor ^= sg(value);
        }

        if (xor == 0) {
            out.println("Arpa");
        } else {
            out.println("Mojtaba");
        }
    }
}
