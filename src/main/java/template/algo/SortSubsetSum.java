package template.algo;

import template.utils.SortUtils;

public class SortSubsetSum {
    /**
     * O(2^n) get all sorted subset sum
     *
     * @param data
     * @return
     */
    public static long[] sortedSubsetSum(long[] data) {
        int n = data.length;
        long[] a = new long[1 << n];
        long[] b = new long[1 << n];
        a[0] = 0;
        for (int i = 0; i < n; i++) {
            int len = 1 << i;
            for(int j = 0; j < len; j++){
                a[j + len] = a[j] + data[i];
            }
            SortUtils.mergeAscending(a, 0, len - 1, a, len, len + len - 1, b, 0);
            long[] tmp = a;
            a = b;
            b = tmp;
        }
        return a;
    }
}
