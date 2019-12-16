package template.algo;

import template.math.DigitUtils;
import template.math.LongPollardRho;

public class MinimumNumberWithMaximumFactors {
    public static void main(String[] args) {
        System.out.println(MinimumNumberWithMaximumFactors.find((long) 5e3));
       // System.out.println(new LongPollardRho().findAllFactors(978217616376000L));
    }

    private static int[] primes = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53};

    /**
     * Find the minimum x, while x <= r and x has many factors as possible
     */
    public static Answer find(long r) {
        Answer answer = new Answer();
        find(r, answer, 1, 1, 0, 65);
        return answer;
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
