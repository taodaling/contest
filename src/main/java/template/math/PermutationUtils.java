package template.math;

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
     * find next permutation (strict increasing order)
     */
    public static boolean nextPermutation(int[] p) {
        for (int a = p.length - 2; a >= 0; --a) {
            if (p[a] < p[a + 1]) {
                for (int b = p.length - 1; ; --b) {
                    if (p[b] > p[a]) {
                        int t = p[a];
                        p[a] = p[b];
                        p[b] = t;
                        for (++a, b = p.length - 1; a < b; ++a, --b) {
                            t = p[a];
                            p[a] = p[b];
                            p[b] = t;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<int[]> generateAllPermutations(int n) {
        List<int[]> list = new ArrayList<>((int) PERMUTATION_CNT[n]);
        traceAllPermutations(new boolean[n], new int[n], list, 0);
        return list;
    }

    private static void traceAllPermutations(boolean[] used, int[] perm, List<int[]> recorders, int i) {
        if (i == perm.length) {
            recorders.add(perm.clone());
            return;
        }
        for (int j = 0; j < used.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            perm[i] = j;
            traceAllPermutations(used, perm, recorders, i + 1);
            used[j] = false;
        }
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
