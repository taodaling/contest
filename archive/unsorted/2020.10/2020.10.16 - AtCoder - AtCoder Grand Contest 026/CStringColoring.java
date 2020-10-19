package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.math.DigitUtils;
import template.math.LongPollardRho;
import template.rand.HashData;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CStringColoring {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        String s = in.readString();
        HashMap<LongPair, Integer> left = gen(s.substring(0, n).toCharArray());
        HashMap<LongPair, Integer> right = gen(new StringBuilder(s.substring(n)).reverse().toString().toCharArray());
        long ans = 0;
        for (Map.Entry<LongPair, Integer> entry : left.entrySet()) {
            ans += entry.getValue().longValue() * right.getOrDefault(entry.getKey(), 0).longValue();
        }
        out.print(ans);
    }


    int mod = (int) 1e9 + 7;
    HashData hd31 = new HashData(100, mod, 31);
    HashData hd61 = new HashData(100, mod, 61);

    public HashMap<LongPair, Integer> gen(char[] s) {
        int n = s.length;
        HashMap<LongPair, Integer> map = new HashMap<>(1 << n);
        for (int i = 0; i < 1 << n; i++) {
            long front31 = 0;
            long front61 = 0;
            long back31 = 0;
            long back61 = 0;

            int one = 0;
            int zero = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    front31 += (long) s[j] * hd31.pow[one] % mod;
                    front61 += (long) s[j] * hd61.pow[one] % mod;
                    one++;
                } else {
                    back31 += (long) s[j] * hd31.pow[n - 1 - zero] % mod;
                    back61 += (long) s[j] * hd61.pow[n - 1 - zero] % mod;
                    zero++;
                }
            }

            front31 %= mod;
            front61 %= mod;
            back31 %= mod;
            back61 %= mod;
            LongPair p = new LongPair(DigitUtils.asLong((int) front31, (int) front61),
                    DigitUtils.asLong((int) back31, (int) back61));
            map.put(p, map.getOrDefault(p, 0) + 1);
        }

        return map;
    }
}

class LongPair {
    long a;
    long b;

    public LongPair(long a, long b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(a * 31 + b);
    }

    @Override
    public boolean equals(Object obj) {
        LongPair p = (LongPair) obj;
        return a == p.a && b == p.b;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", a, b);
    }
}