package contest;

import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixedPointReversals {
    public int[] sort(int[] A, int fixed) {
        this.A = A;
        int n = A.length;
        int[] sortedA = A.clone();
        Arrays.sort(sortedA);
        if (A[fixed] != sortedA[fixed]) {
            return new int[]{-1};
        }
        int limit = 50;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < fixed; i++) {
            cnts[sortedA[i]]++;
            cnts[A[i]]--;
        }
        int k = 0;
        for (int i = 0; i <= limit; i++) {
            if (cnts[i] > 0) {
                k += cnts[i];
            }
        }

        move(0, fixed - 1, fixed - k);
        move(fixed + 1, n - 1, k);
        reverse(fixed - k, fixed + k);
        sort(0, fixed - 1);
        sort(fixed + 1, n - 1);

        return seq.stream().mapToInt(Integer::intValue).toArray();
    }

    int[] A;
    List<Integer> seq = new ArrayList<>();

    public void reverse(int l, int r) {
        if (l == r) {
            return;
        }
        seq.add(l);
        seq.add(r);
        SequenceUtils.reverse(A, l, r);
    }

    public void sort(int l, int r) {
        for (int i = l; i <= r; i++) {
            int minIndex = i;
            for (int j = i; j <= r; j++) {
                if (A[j] < A[minIndex]) {
                    minIndex = j;
                }
            }
            reverse(i, minIndex);
        }
    }

    public void move(int l, int r, int k) {
        int n = r - l + 1;
        if (k <= n - k) {
            for (int i = l; i < l + k; i++) {
                int minIndex = i;
                for (int j = i; j <= r; j++) {
                    if (A[j] < A[minIndex]) {
                        minIndex = j;
                    }
                }
                reverse(i, minIndex);
            }
        } else {
            int nk = n - k;
            for (int i = r; i > r - nk; i--) {
                int minIndex = i;
                for (int j = i; j >= l; j--) {
                    if (A[j] < A[minIndex]) {
                        minIndex = j;
                    }
                }
                reverse(minIndex, i);
            }
        }
    }
}

