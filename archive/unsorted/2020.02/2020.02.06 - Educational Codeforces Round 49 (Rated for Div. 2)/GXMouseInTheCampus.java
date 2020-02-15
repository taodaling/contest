package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Factorization;
import template.math.ILongModular;
import template.math.LongCachedPow;
import template.math.LongPollardRho;
import template.math.LongPower;
import template.primitve.generated.LongHashMap;
import template.primitve.generated.LongHashSet;
import template.primitve.generated.LongList;

import java.util.Map;

public class GXMouseInTheCampus {
    LongHashMap mu = new LongHashMap(10000, false);
    LongHashMap euler = new LongHashMap(10000, false);
    long[] primes;

    {
        mu.put(1, 1);
        euler.put(1, 1);
    }

    public long oneOfPrimeFactor(long x) {
        for (long p : primes) {
            if (x % p == 0) {
                return p;
            }
        }
        return x;
    }

    public void populate(long x) {
        long factor = oneOfPrimeFactor(x);
        long cnt = 0;
        long y = x;
        while (y % factor == 0) {
            cnt++;
            y /= factor;
        }
        if (cnt > 1) {
            mu.put(x, 0);
        } else {
            mu.put(x, -mu(y));
        }
        euler.put(x, euler(y) * (x / y - x / y / factor));
    }

    public long mu(long x) {
        long ans = mu.getOrDefault(x, -1);
        if (ans == -1) {
            populate(x);
            ans = mu.get(x);
        }
        return ans;
    }

    public long euler(long x) {
        long ans = euler.getOrDefault(x, -1);
        if (ans == -1) {
            populate(x);
            ans = euler.get(x);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long m = in.readLong();
        long x = in.readLong();

        primes = new LongPollardRho().findAllFactors(m).keySet()
                .stream().mapToLong(Long::longValue).toArray();
        LongList allFactorOfM = new LongList(20000);
        LongList tmpList = new LongList(20000);
        LongList allPossiblePrimeFactor = new LongList();
        for (long p : primes) {
            allPossiblePrimeFactor.add(p);
            allPossiblePrimeFactor.addAll(Factorization.factorizeNumberPrime(p - 1));
        }
        allPossiblePrimeFactor.unique();

        collect(allFactorOfM, m, 0);
        LongPower power = new LongPower(ILongModular.getInstance(m));

        long total = 1;
        for (int i = 0; i < allFactorOfM.size(); i++) {
            long g = allFactorOfM.get(i);
            if (g == m) {
                continue;
            }
            long mg = m / g;
            tmpList.clear();
            collect(tmpList, mg, 0);
            long cnt = 0;
            for (int j = tmpList.size() - 1; j >= 0; j--) {
                long t = tmpList.get(j);
                cnt += mu(t) * ((m - 1) / (t * g));
            }

            tmpList.clear();
            long euler = euler(mg);
            LongList primeFactors = tmpList;
            for (int j = 0; j < allPossiblePrimeFactor.size(); j++) {
                long p = allPossiblePrimeFactor.get(j);
                if (euler % p == 0) {
                    primeFactors.add(p);
                }
            }

            long n = euler;
            for (int j = 0; j < primeFactors.size(); j++) {
                long p = primeFactors.get(j);
                while (n % p == 0 && power.pow(x, n / p) % mg == 1) {
                    n /= p;
                }
            }

            if (cnt % n != 0) {
                throw new IllegalStateException();
            }
            total += cnt / n;
        }

        out.println(total);
    }

    public void collect(LongList list, long x, int i) {
        if (i == primes.length) {
            list.add(x);
            return;
        }
        collect(list, x, i + 1);
        while (x % primes[i] == 0) {
            x /= primes[i];
            collect(list, x, i + 1);
        }
    }
}