package on2020_01.on2020_01_04_Educational_Codeforces_Round_50__Rated_for_Div__2_.F__Relatively_Prime_Powers;



import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongList;

import java.util.Arrays;
import java.util.TreeMap;

public class FRelativelyPrimePowers {
    long[][] dp;
    int[] ptr = new int[64];

    public FRelativelyPrimePowers() {
        dp = new long[64][];
        LongList list = new LongList(1000000);
        for (int j = 0; ; j++) {
            long x = pow(j, 3);
            if (x > (long) 1e18) {
                break;
            }
            list.add(x);
        }
        dp[3] = list.toArray();
        for (int i = 4; i < 64; i++) {
            list.clear();
            for (int j = 0; j < dp[i - 1].length; j++) {
                long x = mul(dp[i - 1][j], j);
                if (x > (long) 1e18) {
                    break;
                }
                list.add(x);
            }
            dp[i] = list.toArray();
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.readInt();
        Query[] qs = new Query[t];
        for (int i = 0; i < t; i++) {
            qs[i] = new Query();
            qs[i].n = in.readLong();
        }
        Query[] sorted = qs.clone();
        Arrays.sort(sorted, (a, b) -> Long.compare(a.n, b.n));
        for (Query q : sorted) {
            long n = q.n;
            int limit = 64;
            long[] cnts = new long[limit];
            cnts[1] = n - 1;
            for (int i = 2; i < limit; i++) {
                cnts[i] = sqrt(n, i) - 1;
            }
            for (int i = limit - 1; i >= 1; i--) {
                for (int j = i + i; j < limit; j += i) {
                    cnts[i] -= cnts[j];
                }
            }

            long ans = cnts[1];
            q.ans = ans;
        }

        for(Query q : qs){
            out.println(q.ans);
        }
    }

    public long mul(long a, long b) {
        return DigitUtils.isMultiplicationOverflow(a, b, (long) 2e18) ?
                (long) 2e18 : a * b;
    }

    /**
     * return floor(x^(1/t))
     */
    public long sqrt(long x, int t) {
        if (t == 2) {
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    return mid * mid > x;
                }
            };
            return lbs.binarySearch(1, (long) (1e9 + 1)) - 1;
        } else {
            while (ptr[t] + 1 < dp[t].length && dp[t][ptr[t] + 1] <= x) {
                ptr[t]++;
            }
            return ptr[t];
        }
    }

    public long pow(long x, long n) {
        if (n == 0) {
            return 1;
        }
        long ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }
}

class Query {
    long ans;
    long n;
}
