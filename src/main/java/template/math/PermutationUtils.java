package template.math;

import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All permutation start with 0
 */
public class PermutationUtils {
    private static final long[] PERMUTATION_CNT = new long[21];

    static {
        PERMUTATION_CNT[0] = 1;
        for (int i = 1; i <= 20; i++) {
            PERMUTATION_CNT[i] = PERMUTATION_CNT[i - 1] * i;
        }
    }

    /**
     * <p>find next permutation (strict increasing order)</p>
     * <p>O(e) average for s contains only distinct values</p>
     * <p>O(n) worse case without restraints on s</p>
     * @param s
     * @return
     */
    public static boolean nextPermutation(int[] s) {
        int n = s.length;
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] < s[i + 1]) {
                SequenceUtils.reverse(s, i + 1, n - 1);
                //find
                for (int j = i + 1; ; j++) {
                    if (s[j] > s[i]) {
                        int tmp = s[j];
                        s[j] = s[i];
                        s[i] = tmp;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * <p>find prev permutation (strict increasing order)</p>
     * <p>O(e) average for s contains only distinct values</p>
     * <p>O(n) worse case without restraints on s</p>
     * @param s
     * @return
     */
    public static boolean prevPermutation(int[] s){
        int n = s.length;
        for (int i = n - 2; i >= 0; i--) {
            if (s[i] > s[i + 1]) {
                //find
                for (int j = n - 1; ; j--) {
                    if (s[j] < s[i]) {
                        int tmp = s[j];
                        s[j] = s[i];
                        s[i] = tmp;
                        break;
                    }
                }
                SequenceUtils.reverse(s, i + 1, n - 1);
                return true;
            }
        }
        return false;
    }

    /**
     * Find the kth(start from 1) smallest permutation in all permutations formed by n elements(0, 1, ... , n-1)
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

    public static long rankOf(int[] perm) {
        return rankOf(perm, 0);
    }

    private static long rankOf(int[] perm, int i) {
        if (perm.length == i) {
            return 1;
        }
        int remain = perm.length - i - 1;
        long cnt = perm[i] * PERMUTATION_CNT[remain];
        for (int j = i + 1; j < perm.length; j++) {
            if (perm[j] > perm[i]) {
                perm[j]--;
            }
        }
        cnt += rankOf(perm, i + 1);
        for (int j = i + 1; j < perm.length; j++) {
            if (perm[j] >= perm[i]) {
                perm[j]++;
            }
        }
        return cnt;
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

    public static class TailPermutation {
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
