package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.Debug;

public class TaskD {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] A = new int[n];
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            B[i] = in.readInt();
        }
        long ans = 0;
        for (int i = 0; i < 29; i++) {
            int mask = (1 << (i + 1)) - 1;
            SortUtils.radixSort(A, 0, n - 1, a -> a & mask);
            SortUtils.radixSort(B, 0, n - 1, b -> b & mask);
            int am = separate(A, 0, n - 1, 1 << i);
            int bm = separate(B, 0, n - 1, 1 << i);

            long cnt = countNotExceed(A, 0, am, B, bm + 1, n - 1, mask >>> 1)
                    + countNotExceed(A, am + 1, n - 1, B, 0, bm, mask >>> 1)
                    + countExceed(A, 0, am, B, 0, bm, mask >>> 1)
                    + countExceed(A, am + 1, n - 1, B, bm + 1, n - 1, mask >>> 1);

            ans = Bits.setBit(ans, i, (cnt % 2) == 1);
            debug.debug("cnt", cnt);
        }

        out.println(ans);
    }

    public int separate(int[] A, int l, int r, int bit) {
        int am = l - 1;
        while (am + 1 <= r && (A[am + 1] & bit) == 0) {
            am++;
        }
        return am;
    }

    public long countExceed(int[] a, int al, int ar, int[] b, int bl, int br, int mask) {
        int l = br + 1;
        long ans = 0;
        for (int i = al; i <= ar; i++) {
            while (l > bl && (b[l - 1] & mask) + (a[i] & mask) > mask) {
                l--;
            }
            ans += br - l + 1;
        }
        return ans;
    }

    public long countNotExceed(int[] a, int al, int ar, int[] b, int bl, int br, int mask) {
        int l = br;
        long ans = 0;
        for (int i = al; i <= ar; i++) {
            while (l >= bl && (b[l] & mask) + (a[i] & mask) > mask) {
                l--;
            }
            ans += l - bl + 1;
        }
        return ans;
    }
}
