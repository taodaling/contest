package contest;

import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongPreSum;

public class LexicographicPartition {
    public int[] positiveSum(int n, int[] Aprefix, int seed, int Arange) {
        long[] A = new long[n];
        for (int i = 0; i < Aprefix.length; i++) {
            A[i] = Aprefix[i];
        }

        long state = seed;
        for (int i = Aprefix.length; i < n; i++) {
            state = (1103515245 * state + 12345);
            A[i] = state % (2L * Arange + 1);
            A[i] = A[i] - Arange;
            state = state % (1L << 31);
        }

        LongPreSum lps = new LongPreSum(A);

        if (lps.prefix(n) <= 0) {
            return new int[]{-1};
        }
        IntegerList ans = new IntegerList(n + 1);
        ans.add(0);
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i;
            while (r + 1 < n && (lps.intervalSum(l, r) <= 0 || lps.post(r + 1) <= 0)) {
                r++;
            }
            ans.add(r - l + 1);
            i = r;
        }
        ans.set(0, ans.size() - 1);
        return ans.toArray();
    }
}
