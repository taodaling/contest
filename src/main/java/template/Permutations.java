package template;

import java.util.Arrays;

/**
 * All permutation start with 0
 */
public class Permutations {
    private static final long[] PERMUTATION_CNT = new long[21];

    static {
        PERMUTATION_CNT[0] = 1;
        for (int i = 1; i <= 20; i++) {
            PERMUTATION_CNT[i] = PERMUTATION_CNT[i - 1] * i;
        }
    }

    /**
     * Find the kth smallest permutation in all permutations formed by n elements,
     */
    public static TailPermutation theKthSmallest(long n, long k) {
        int digitsNeed = Arrays.binarySearch(PERMUTATION_CNT, k);
        if (digitsNeed < 0) {
            digitsNeed = -(digitsNeed + 1);
        }
        if (k == 1) {
            digitsNeed = 1;
        }
        if (digitsNeed > n) {
            throw new IllegalArgumentException();
        }
        long[] seq = new long[digitsNeed];
        genSeq(seq, 0, k);
        long since = n - digitsNeed;
        for (int i = 0; i < digitsNeed; i++) {
            seq[i] += since;
        }
        return new TailPermutation(seq, since);
    }

    private static void genSeq(long[] seq, int offset, long k) {
        int n = seq.length;
        if (offset == n) {
            return;
        }
        int remain = n - offset;
        long pick = (k - 1) / PERMUTATION_CNT[remain - 1];
        seq[offset] = pick;
        genSeq(seq, offset + 1, k - pick * PERMUTATION_CNT[remain - 1]);
        for (int i = offset + 1; i < n; i++) {
            if (seq[i] >= pick) {
                seq[i]++;
            }
        }
    }

    private static class TailPermutation {
        private long[] p;
        private long offset;

        public TailPermutation(long[] p, long offset) {
            this.p = p;
            this.offset = offset;
        }

        public long getElementAt(long i) {
            if (i < offset) {
                return i;
            }
            return p[(int) (i - offset)];
        }

        public long getElementIndex(long e) {
            if (e < offset) {
                return e;
            }
            for (int i = 0; ; i++) {
                if (e == p[i]) {
                    return i + offset;
                }
            }
        }
    }
}