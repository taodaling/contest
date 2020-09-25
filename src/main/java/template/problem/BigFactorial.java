package template.problem;

import template.math.Factorial;
import template.math.Modular;
import template.math.Power;
import template.polynomial.FastFourierTransform;

import java.util.Arrays;

/**
 * Find n! mod p in O(n^{0.5}\log_2n) while p is a prime
 */
public class BigFactorial {
    Modular mod;
    Power pow;
    Factorial factorial;

    public BigFactorial(Modular mod) {
        this.mod = mod;
        this.pow = new Power(mod);
        int sqrt = (int) Math.ceil(Math.sqrt(mod.getMod()));
        factorial = new Factorial(sqrt, mod.getMod());
    }


    public int solve(int n) {
        if (n >= mod.getMod()) {
            return 0;
        }
        int sqrt = (int) Math.ceil(Math.sqrt(n));
//        if (sqrt % 2 == 0) {
//         //   sqrt++;
//        }
        int[] ans = binaryLifting(sqrt, sqrt);
        int prod = 1;
        int i;
        for (i = 0; i + sqrt <= n; i += sqrt) {
            prod = mod.mul(prod, ans[i / sqrt]);
        }
        for (i++; i <= n; i++) {
            prod = mod.mul(prod, i);
        }
        return prod;
    }


    private int[] add(int[] f, int d, int s) {
        int[] ans = new int[d + 2];
        for (int k = 0; k <= d; k++) {
            ans[k] = mod.mul(f[k], k * s + d + 1);
        }
        int last = 1;
        for (int i = (d + 1) * s, k = 1; k <= d + 1; k++) {
            last = mod.mul(last, i + k);
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
            fact[i] = mod.valueOf(min + i);
            if (i > 0) {
                fact[i] = mod.mul(fact[i], fact[i - 1]);
            }
        }
        invFact[2 * d] = pow.inverse(fact[2 * d]);
        for (int i = 2 * d - 1; i >= 0; i--) {
            invFact[i] = mod.mul(invFact[i + 1], min + i + 1);
        }
        for (int i = 0; i <= d; i++) {
            int high = i + delta - min;
            int low = high - d;
            left[i] = fact[high];
            if (low > 0) {
                left[i] = mod.mul(left[i], invFact[low - 1]);
            }
        }

        //calc right
        int[] a = new int[2 * d + 1];
        int[] b = new int[d + 1];
        for (int i = 0; i <= 2 * d; i++) {
            int val = i + min;
            a[i] = pow.inverse(val);
        }
        for (int i = 0; i <= d; i++) {
            b[i] = f[i];
            //i!
            b[i] = mod.mul(b[i], factorial.invFact(i));
            //1,2,...,d-i
            b[i] = mod.mul(b[i], factorial.invFact(d - i));
            //i+1,...,d
            if ((d - i) % 2 == 1) {
                b[i] = mod.valueOf(-b[i]);
            }
        }

        int[] right = FastFourierTransform.multiplyMod(a, b, mod.getMod());
        int[] ans = new int[d + 1];
        for (int i = 0; i <= d; i++) {
            int val = i + delta;
//            if (val <= d) {
//                ans[i] = f[i];
//                continue;
//            }
            ans[i] = mod.mul(left[i], right[val - min]);
        }

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
        int[] b = expand(a, mod.mul(pow.inverse(s), d));
        ans = a;
        for (int i = 0; i <= k; i++) {
            ans[i] = mod.mul(a[i], b[i]);
        }

        if (n > k) {
            ans = add(ans, k, s);
        }

        return ans;
    }
}
