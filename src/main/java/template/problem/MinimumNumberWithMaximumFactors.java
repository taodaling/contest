package template.problem;

import template.math.DigitUtils;
import template.math.LongPollardRho;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongList;

import java.util.Arrays;

public class MinimumNumberWithMaximumFactors {
    public static void main(String[] args) {
        System.out.println(MinimumNumberWithMaximumFactors.find((long) 1e15));
        System.out.println(MinimumNumberWithMaximumFactors.divisionRelation(978217616376000L));
        System.out.println(Arrays.toString(MinimumNumberWithMaximumFactors.maximumPrimeFactor((long) 1e15)));
        // System.out.println(new LongPollardRho().findAllFactors(978217616376000L));
    }

    private static int[] primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53};

    /**
     * Find the minimum x, while x <= r and x has many factors as possible
     */
    public static Answer find(long r) {
        Answer answer = new Answer();
        find(r, answer, 1, 1, 0, 65);
        return answer;
    }

    /**
     * 统计[1,n]中的所有数中拥有最多不同素因子的数，以及其素因子数。
     * <br>
     * 返回结果ret，则ret[0]表示该数，ret[1]表示该数包含多少个不同的素因子
     */
    public static long[] maximumPrimeFactor(long n) {
        long[] ans = new long[2];
        ans[0] = 1;
        for (int i = 0; i < primes.length; i++) {
            if (DigitUtils.isMultiplicationOverflow(ans[0], primes[i], n)) {
                break;
            }
            ans[0] *= primes[i];
            ans[1]++;
        }
        return ans;
    }

    /**
     * For all factors of n, find how many pair of factors (x,y) satisfy x | y
     */
    public static long divisionRelation(long n) {
        long[] primes = new LongPollardRho().findAllFactors(n).keySet().stream().mapToLong(Long::longValue).toArray();
        LongList list = new LongList();
        collectAllFactors(primes, n, 1, 0, list);
        long[] factors = list.toArray();

        long cnt = 0;
        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (factors[i] % factors[j] == 0) {
                    cnt++;
                }
            }
        }

        return cnt;
    }

    public static void collectAllFactors(long[] primes, long n, long val, int i, LongList list) {
        if (i == primes.length) {
            list.add(val);
            return;
        }
        long x = 1;
        while (true) {
            collectAllFactors(primes, n, val * x, i + 1, list);
            if (n / x % primes[i] != 0) {
                break;
            }
            x *= primes[i];
        }
    }

    private static void find(long r, Answer answer, long factorNumber, long value, int step, int last) {
        if (value > r) {
            return;
        }
        if (step == primes.length || DigitUtils.isMultiplicationOverflow(value, primes[step], r)) {
            answer.compareAndSet(factorNumber, value);
            return;
        }

        long mul = 1;
        for (int j = 0; j <= last; j++) {
            if (DigitUtils.isMultiplicationOverflow(mul, value, r)) {
                return;
            }
            find(r, answer, factorNumber * (j + 1), value * mul, step + 1, j);
            if (DigitUtils.isMultiplicationOverflow(mul, mul, Long.MAX_VALUE)) {
                return;
            }
            mul = mul * primes[step];
        }
    }

    public static class Answer {
        private long factorNumber;
        private long value;

        public long getFactorNumber() {
            return factorNumber;
        }

        public long getValue() {
            return value;
        }

        void compareAndSet(long fn, long v) {
            if (fn > factorNumber) {
                factorNumber = fn;
                value = v;
            }
            if (fn == factorNumber && value < v) {
                value = v;
            }
        }

        @Override
        public String toString() {
            return String.format("%d has %d factors", value, factorNumber);
        }
    }
}
