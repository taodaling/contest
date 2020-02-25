package on2020_02.on2020_02_23_Codeforces_Round__511__Div__1_.A__Enlarge_GCD;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class AEnlargeGCD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int g = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            g = GCDs.gcd(a[i], g);
        }
        int limit = (int) 15e6;
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit, false, false, false);
        int[] cnt = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            int v = a[i] / g;
            while (v != 1) {
                int f = sieve.smallestPrimeFactor[v];
                while (v % f == 0) {
                    v /= f;
                }
                cnt[f]++;
            }
        }
        int ans = -1;
        for (int i = 2; i <= limit; i++) {
            if (cnt[i] > 0) {
                ans = Math.max(ans, cnt[i]);
            }
        }

        if (ans == -1) {
            out.println(-1);
        } else {
            out.println(n - ans);
        }
    }
}


/**
 * Euler sieve for multiplicative function
 */
class MultiplicativeFunctionSieve {
    public int[] primes;
    public boolean[] isComp;
    public int primeLength;
    public int[] mobius;
    public int[] euler;
    public int[] factors;
    public int[] smallestPrimeFactor;
    //public int[] numberOfSmallestPrimeFactor;

    public MultiplicativeFunctionSieve(int limit, boolean enableMobius, boolean enableEuler, boolean enableFactors) {
        isComp = new boolean[limit + 1];
        primes = new int[limit + 1];
        //numberOfSmallestPrimeFactor = new int[limit + 1];
        smallestPrimeFactor = new int[limit + 1];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[primeLength++] = i;
                smallestPrimeFactor[i] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                smallestPrimeFactor[pi] = primes[j];
                isComp[pi] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }


    }
}
