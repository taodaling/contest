package template.math;

import template.polynomial.IntPoly;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

/**
 * Find n! mod p in O(n^{0.5}\log_2n) while p is a prime
 */
public class BigFactorial {
    int mod;
    Power pow;
    Factorial factorial;
    IntPoly poly;

    public BigFactorial(int mod, IntPoly poly) {
        this.poly = poly;
        this.mod = mod;
        this.pow = new Power(mod);
        int sqrt = (int) Math.ceil(Math.sqrt(mod));
        factorial = new Factorial(sqrt, mod);
    }


    public int solve(int n) {
        if (n >= mod) {
            return 0;
        }
        int sqrt = (int) Math.ceil(Math.sqrt(n));
//        if (sqrt % 2 == 0) {
//         //   sqrt++;
//        }
        int[] ans = binaryLifting(sqrt, sqrt);
        long prod = 1;
        int i;
        for (i = 0; i + sqrt <= n; i += sqrt) {
            prod = prod * ans[i / sqrt] % mod;
        }
        for (i++; i <= n; i++) {
            prod = prod * i % mod;
        }
        return (int) prod;
    }


    private int[] add(int[] f, int d, int s) {
        int[] ans = new int[d + 2];
        for (int k = 0; k <= d; k++) {
            ans[k] = (int) ((long) f[k] * (k * s + d + 1) % mod);
        }
        int last = 1;
        for (int i = (d + 1) * s, k = 1; k <= d + 1; k++) {
            last = (int) ((long) last * (i + k) % mod);
        }
        ans[ans.length - 1] = last;
        return ans;
    }

    private int[] expand(int[] f, int delta) {
        int d = f.length - 1;
        //h(i) = f(i), find h(i+delta) for i <= d
        int[] left = new int[d + 1];

        //calc left
        int min = delta - d;
        int max = delta + d;
        int[] fact = new int[2 * d + 1];
        int[] invFact = new int[2 * d + 1];
        for (int i = 0; i <= 2 * d; i++) {
            fact[i] = DigitUtils.mod(min + i, mod);
            if (i > 0) {
                fact[i] = (int) ((long) fact[i] * fact[i - 1] % mod);
            }
        }
        invFact[2 * d] = pow.inverse(fact[2 * d]);
        for (int i = 2 * d - 1; i >= 0; i--) {
            invFact[i] = (int) ((long) invFact[i + 1] * (min + i + 1) % mod);
        }
        for (int i = 0; i <= d; i++) {
            int high = i + delta - min;
            int low = high - d;
            left[i] = fact[high];
            if (low > 0) {
                left[i] = (int) ((long) left[i] * invFact[low - 1] % mod);
            }
        }

        //calc right
        int[] a = PrimitiveBuffers.allocIntPow2(2 * d + 1);
        int[] b = PrimitiveBuffers.allocIntPow2(d + 1);
        for (int i = 0; i <= 2 * d; i++) {
            int val = i + min;
            a[i] = pow.inverse(val);
        }
        for (int i = 0; i <= d; i++) {
            b[i] = f[i];
            //i!
            b[i] = (int) ((long) b[i] * factorial.invFact(i) % mod);
            //1,2,...,d-i
            b[i] = (int) ((long) b[i] * factorial.invFact(d - i) % mod);
            //i+1,...,d
            if ((d - i) % 2 == 1) {
                b[i] = DigitUtils.negate(b[i], mod);
            }
        }

        int[] right = poly.convolution(a, b);
        int[] ans = new int[d + 1];
        for (int i = 0; i <= d; i++) {
            int val = i + delta;
//            if (val <= d) {
//                ans[i] = f[i];
//                continue;
//            }
            ans[i] = (int) ((long) left[i] * right[val - min] % mod);
        }

        PrimitiveBuffers.release(a, b, right);
        return ans;
    }

    private int[] binaryLifting(int n, int s) {
        if (n == 0) {
            int[] ans = new int[]{1};
            return ans;
        }
        if (n == 1) {
            int[] ans = new int[]{1, 1 + s};
            return ans;
        }
        int k = n & (~1);
        int d = k / 2;
        int[] ans = binaryLifting(d, s);
        //expand
        int[] exp1 = expand(ans, d + 1);
        int[] a = Arrays.copyOf(ans, k + 1);
        for (int i = d + 1; i <= k; i++) {
            a[i] = exp1[i - d - 1];
        }
        //g(i)=f(is)
        //g(i+d)=f(is+k/2)
        //s(i+d)=is+k/2
        //sd=k/2
        //d=k/2/s
        int[] b = expand(a, (int) ((long) pow.inverse(s) * d % mod));
        ans = a;
        for (int i = 0; i <= k; i++) {
            ans[i] = (int) ((long) a[i] * b[i] % mod);
        }

        if (n > k) {
            ans = add(ans, k, s);
        }

        return ans;
    }
}
