package template.math;

import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.primitve.generated.datastructure.LongList;
import template.problem.MinimumNumberWithMaximumFactors;

public class Factorization {
    public static void main(String[] args){
        System.out.println(new PollardRho().findAllFactors(985661440));
    }

    /**
     * factorize all number in [1, n], and only return their prime factors
     */
    public static IntegerMultiWayStack factorizeRangePrime(int n) {
        int maxFactorCnt = (int) MinimumNumberWithMaximumFactors.maximumPrimeFactor(n)[1];
        IntegerMultiWayStack stack = new IntegerMultiWayStack(n + 1, n * maxFactorCnt);
        boolean[] isComp = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            if (isComp[i]) {
                continue;
            }
            for (int j = i; j <= n; j += i) {
                isComp[j] = true;
                stack.addLast(j, i);
            }
        }
        return stack;
    }

    /**
     * factorize all number in [1, n]
     */
    public static IntegerMultiWayStack factorizeRange(int n) {
        int size = 0;
        for (int i = 1; i <= n; i++) {
            size += n / i;
        }
        IntegerMultiWayStack stack = new IntegerMultiWayStack(n + 1, size);
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j += i) {
                stack.addLast(j, i);
            }
        }
        return stack;
    }

    /**
     * Find all factors of x, and return them unordered.
     */
    public static LongList factorizeNumber(long x) {
        LongList ans = new LongList();
        factorizeNumber(x, ans);
        return ans;
    }

    public static void factorizeNumber(long x, LongList ans) {
        for (long i = 1; i * i <= x; i++) {
            if (x % i != 0) {
                continue;
            }
            ans.add(i);
            if (i * i != x) {
                ans.add(x / i);
            }
        }
    }

    /**
     * Find all prime factors of x, and return them ordered.
     */
    public static LongList factorizeNumberPrime(long x) {
        LongList ans = new LongList();
        factorizeNumberPrime(x, ans);
        return ans;
    }


    /**
     * Find all prime factors of x, and return them ordered.
     */
    public static LongList factorizeNumberPrime(long x, LongList ans) {
        for (long i = 2; i * i <= x; i++) {
            if (x % i != 0) {
                continue;
            }
            ans.add(i);
            while (x % i == 0) {
                x /= i;
            }
        }
        if (x > 1) {
            ans.add(x);
        }
        return ans;
    }

    /**
     * Find all factors of x, and return them unordered.
     */
    public static IntegerList factorizeNumber(int x) {
        IntegerList ans = new IntegerList();
        factorizeNumber(x, ans);
        return ans;
    }

    public static void factorizeNumber(int x, IntegerList ans) {
        for (int i = 1; i * i <= x; i++) {
            if (x % i != 0) {
                continue;
            }
            ans.add(i);
            if (i * i != x) {
                ans.add(x / i);
            }
        }
    }

    /**
     * Find all prime factors of x, and return them ordered.
     */
    public static IntegerList factorizeNumberPrime(int x) {
        IntegerList ans = new IntegerList();
        for (int i = 2; i * i <= x; i++) {
            if (x % i != 0) {
                continue;
            }
            ans.add(i);
            while (x % i == 0) {
                x /= i;
            }
        }
        if (x > 1) {
            ans.add(x);
        }
        return ans;
    }
}
