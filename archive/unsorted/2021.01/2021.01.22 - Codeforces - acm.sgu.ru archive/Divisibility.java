package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.LongArrayList;
import template.rand.Randomized;

import java.util.function.LongConsumer;
import java.util.stream.IntStream;

public class Divisibility {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case %d: ", testNumber);
        String s = in.rs();
        long[] total = new long['z' - 'a' + 1];
        long base = 1;
        for (char c : new StringBuilder(s).reverse().toString().toCharArray()) {
            total[c - 'a'] += base;
            base *= 10;
        }
        LongArrayList cand = new LongArrayList();
        for (long x : total) {
            if (x != 0) {
                cand.add(x);
            }
        }
        cand.sort();
        cand.reverse();
        long[] ws = cand.toArray();
        int[] perm = IntStream.range(0, 10).toArray();
        long g = 0;
        for (int i = 0; i < 100; i++) {
            Randomized.shuffle(perm);
            if (perm[0] == 0) {
                i--;
                continue;
            }
            long sum = 0;
            for (int j = 0; j < ws.length; j++) {
                sum += perm[j] * ws[j];
            }
            g = GCDs.gcd(g, sum);
        }

        primes = LongPollardRho.findAllFactors(g).stream().mapToLong(Long::longValue).toArray();

        int m = primes.length;
        exp = new int[m];
        for (int i = 0; i < m; i++) {
            long x = g;
            while (x % primes[i] == 0) {
                x /= primes[i];
                exp[i]++;
            }
        }

        factor.clear();
        collectAll(m - 1, 1, factor::add);
        factor.sort();
        for (long x : factor.toArray()) {
            out.append(x).append(' ');
        }
        out.println();
    }

    LongArrayList factor = new LongArrayList((int) 1e6);

    public void collectAll(int i, long prod, LongConsumer consumer) {
        if (i < 0) {
            consumer.accept(prod);
            return;
        }
        for (int t = 0; t <= exp[i]; t++, prod *= primes[i]) {
            collectAll(i - 1, prod, consumer);
        }
    }

    long[] primes;
    int[] exp;
}
