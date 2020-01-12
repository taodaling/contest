package on2020_01.on2020_01_12_Educational_Codeforces_Round_51__Rated_for_Div__2_.E__Vasya_and_Big_Integers;



import com.sun.org.apache.xpath.internal.operations.Mod;
import strings.SuffixArrayDC3;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.IntegerBIT;
import template.rand.Hash;
import template.string.SAIS;
import template.string.ZAlgorithm;

import java.math.BigInteger;
import java.util.Arrays;

public class EVasyaAndBigIntegers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] a = new char[1000000];
        char[] left = new char[1000000];
        char[] right = new char[1000000];
        int aLen = in.readString(a, 0);
        int leftLen = in.readString(left, 0);
        int rightLen = in.readString(right, 0);

        ZAlgorithm leftLCP = new ZAlgorithm(leftLen + aLen, i -> i < leftLen ? left[i] : a[i - leftLen]);
        ZAlgorithm rightLCP = new ZAlgorithm(rightLen + aLen, i -> i < rightLen ? right[i] : a[i - rightLen]);

        int[] dp = new int[aLen + 1];
        int[] preSum = new int[aLen + 1];
        dp[0] = 1;
        preSum[0] = 1;
        for (int i = 1; i <= aLen; i++) {
            preSum[i] = preSum[i - 1];
            int l = i - rightLen;
            int r = i - leftLen;
            if (l >= 0) {
                int lcp = rightLCP.applyAsInt(rightLen + l);
                if (lcp < rightLen && a[l + lcp] > right[lcp]) {
                    l++;
                }
            } else {
                l = 0;
            }
            if (r < 0) {
                continue;
            }
            int lcp = leftLCP.applyAsInt(leftLen + r);
            if (lcp < leftLen && a[r + lcp] < left[lcp]) {
                r--;
            }
            if (l > r) {
                continue;
            }
            dp[i] = interval(preSum, l, r);
            preSum[i] = mod.plus(preSum[i - 1], dp[i]);

            if (a[i - 1] == '0') {
                preSum[i - 1] = mod.subtract(preSum[i - 1], dp[i - 1]);
                preSum[i] = mod.subtract(preSum[i], dp[i - 1]);
            }
        }


//        System.err.println(Arrays.toString(dp));
//
//        int bf = bf(String.valueOf(a, 0, aLen), String.valueOf(left, 0, leftLen), String.valueOf(right, 0, rightLen));
//        if (bf != dp[aLen]) {
//            //      throw new RuntimeException();
//        }
        int ans = dp[aLen];
        out.println(ans);
    }

    Modular mod = new Modular(998244353);

    public int bf(String a, String l, String r) {
        BigInteger bl = new BigInteger(l);
        BigInteger br = new BigInteger(r);
        int[] dp = new int[a.length() + 1];
        dp[0] = 1;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = i; j >= 1; j--) {
                if (j < i && a.charAt(j - 1) == '0') {
                    continue;
                }
                BigInteger value = new BigInteger(a.substring(j - 1, i));
                if (value.compareTo(bl) >= 0 && value.compareTo(br) <= 0) {
                    dp[i] = mod.plus(dp[i], dp[j - 1]);
                }
            }
        }
        System.err.println(Arrays.toString(dp));
        return dp[a.length()];
    }


    public int interval(int[] preSum, int l, int r) {
        if (l == 0) {
            return preSum[r];
        }
        return mod.subtract(preSum[r], preSum[l - 1]);
    }

}
