package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ILongModular;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CHackIt {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        BigInteger ba = BigInteger.valueOf(a);
        Map<BigInteger, BigInteger> map = new HashMap<>();
        for (int i = 0; ; i++) {
            BigInteger atLeast = BigInteger.valueOf(i).multiply(ba);
            BigInteger lo = BigInteger.ZERO;
            BigInteger hi = BigInteger.valueOf(10).pow(20);
            while (hi.compareTo(lo) > 0) {
                BigInteger mid = lo.add(hi).shiftRight(1);
                if (count(mid).compareTo(atLeast) >= 0) {
                    hi = mid;
                } else {
                    lo = mid.add(BigInteger.valueOf(1));
                }
            }
            BigInteger mod = count(lo).mod(ba);
            if (map.containsKey(mod) && map.get(mod).compareTo(lo) < 0) {
                out.append(map.get(mod).add(BigInteger.ONE)).append(' ').append(lo);
                return;
            }
            map.put(mod, lo);
        }
    }

    public char[] s;
    public BigInteger[][] dp = new BigInteger[30][2];
    public BigInteger[][] way = new BigInteger[30][2];

    public BigInteger dp(int pos, int ceil) {
        if (pos == s.length) {
            return BigInteger.ZERO;
        }
        if (dp[pos][ceil] == null) {
            BigInteger ans = BigInteger.ZERO;
            int posVal = s[pos] - '0';
            for (int i = 0; i < 10; i++) {
                if (ceil == 1 && i > posVal) {
                    continue;
                }

                ans = ans.add(dp(pos + 1, ceil == 1 && i == posVal ? 1 : 0)).add(BigInteger.valueOf(i).multiply(way(pos + 1, ceil == 1 && i == posVal ? 1 : 0)));
            }
            dp[pos][ceil] = ans;
        }
        return dp[pos][ceil];
    }

    public BigInteger way(int pos, int ceil) {
        if (pos == s.length) {
            return BigInteger.ONE;
        }
        if (way[pos][ceil] == null) {
            BigInteger ans = BigInteger.ZERO;
            int posVal = s[pos] - '0';
            for (int i = 0; i < 10; i++) {
                if (ceil == 1 && i > posVal) {
                    continue;
                }
                ans = ans.add(way(pos + 1, ceil == 1 && i == posVal ? 1 : 0));
            }
            way[pos][ceil] = ans;
        }
        return way[pos][ceil];
    }

    public BigInteger count(BigInteger n) {
        s = n.toString().toCharArray();
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j] = null;
                way[i][j] = null;
            }
        }
        return dp(0, 1);
    }
}
