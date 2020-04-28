package on2020_04.on2020_04_28_2020_TopCoder_Open_Algo.EllysDifferentPrimes;



import template.math.EratosthenesSieve;

import java.util.Arrays;
import java.util.function.IntConsumer;

public class EllysDifferentPrimes {
    public int getClosest(int N) {
        int max = 0;
        EulerSieve sieve = new EulerSieve(100000000);
        int ans = -1000000000;
        for (int i = 0; i < sieve.primeLength; i++) {
            if (check(sieve.primes[i])) {
                if (Math.abs(N - ans) > Math.abs(N - sieve.primes[i])) {
                    ans = sieve.primes[i];
                }
            }
        }
        return ans;
    }

    boolean[] digits = new boolean[10];

    public boolean check(int n) {
        Arrays.fill(digits, false);
        while (n != 0) {
            int tail = n % 10;
            if (digits[tail]) {
                return false;
            }
            digits[tail] = true;
            n /= 10;
        }
        return true;
    }
}


class EulerSieve {
    int[] primes;
    boolean[] isComp;
    int primeLength;

    public int getPrimeCount() {
        return primeLength;
    }

    public int get(int k) {
        return primes[k];
    }

    public boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        return !isComp[x];
    }

    public boolean isComp(int x) {
        if (x <= 1) {
            return false;
        }
        return isComp[x];
    }

    public EulerSieve(int limit) {
        isComp = new boolean[limit + 1];
        primes = new int[limit / 10];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[primeLength++] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                isComp[pi] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
    }
}
