package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__240__Div__1_.C__Mashmokh_and_Reverse_Operation;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class CMashmokhAndReverseOperation {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(1 << n);
        int h = n + 1;
        ordered = new long[h];
        reverse = new long[h];
        dac(0, a.length - 1, a, new int[a.length], h - 1);
        debug.debugArray("a", a);
        debug.debugArray("ordered", ordered);
        debug.debugArray("reverse", reverse);
        int m = in.ri();
        for (int i = 0; i < m; i++) {
            int q = in.ri();
            int beginLevel = q;
            for (int j = beginLevel; j >= 0; j--) {
                long tmp = ordered[j];
                ordered[j] = reverse[j];
                reverse[j] = tmp;
            }
            long sum = 0;
            for(long x : reverse){
                sum += x;
            }
            out.println(sum);
        }
    }

    long[] ordered;
    long[] reverse;

    public void dac(int l, int r, int[] a, int[] buf, int height) {
        if (l >= r) {
            return;
        }
        int m = (l + r) / 2;
        dac(l, m, a, buf, height - 1);
        dac(m + 1, r, a, buf, height - 1);
        int head = l - 1;
        for (int i = m + 1; i <= r; i++) {
            while (head + 1 <= m && a[head + 1] < a[i]) {
                head++;
            }
            ordered[height] += head - l + 1;
        }
        head = m;
        for (int i = l; i <= m; i++) {
            while (head + 1 <= r && a[head + 1] < a[i]) {
                head++;
            }
            reverse[height] += head - (m + 1) + 1;
        }
        //merge
        int lh = l;
        int rh = m + 1;
        int wpos = l;
        while (lh <= m || rh <= r) {
            if (rh > r || lh <= m && a[lh] <= a[rh]) {
                buf[wpos++] = a[lh++];
            } else {
                buf[wpos++] = a[rh++];
            }
        }
        System.arraycopy(buf, l, a, l, r - l + 1);
    }
}