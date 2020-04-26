package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    long limit = (long) 1e10;
    long inf = (long) 1e18;
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    CachedPow cp = new CachedPow(2, mod);
    int[] pre;
    Debug debug = new Debug(true);

    public int interval(int l, int r) {
        int sum = pre[r];
        if (l > 0) {
            sum = mod.subtract(sum, pre[l - 1]);
            sum = mod.mul(sum, cp.inverse(l));
        }
        return sum;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] left = new int[n];
        long[] leftSum = new long[n];

        for (int i = 0; i < n; i++) {
            left[i] = i;
            long sum = a[i];
            while (left[i] > 0 && sum >= 0) {
                long pow2 = DigitUtils.limitPow(2, left[i] - left[left[i] - 1], inf);
                sum = DigitUtils.mul(sum, pow2, inf, inf);
                sum = Math.min(sum + leftSum[left[i] - 1], inf);
                left[i] = left[left[i] - 1];
            }
            leftSum[i] = sum;
        }


        pre = new int[n];
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                pre[i] = pre[i - 1];
            }
            pre[i] = mod.plus(pre[i], mod.mul(a[i], cp.pow(i)));
        }

        int log = 20;
        int[][] jumpIndex = new int[n][log];
        SequenceUtils.deepFill(jumpIndex, -1);
        long[][] jumpSum = new long[n][log];
        for (int i = 0; i < n; i++) {
            jumpIndex[i][0] = left[i] - 1;
            jumpSum[i][0] = interval(left[i], i);
            for (int j = 0; jumpIndex[i][j] >= 0; j++) {
                jumpIndex[i][j + 1] = jumpIndex[jumpIndex[i][j]][j];
                jumpSum[i][j + 1] = mod.plus(jumpSum[i][j], jumpSum[jumpIndex[i][j]][j]);
            }
        }


        debug.debug("left", left);
        while (q-- > 0) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;

            long ans = 0;
            int index = r;
            for (int i = log - 1; i >= 0; i--) {
                if (jumpIndex[index][i] >= l) {
                    ans = mod.plus(ans, jumpSum[index][i]);
                    index = jumpIndex[index][i];
                }
            }

            ans = mod.mul(ans, 2);
            int extra = interval(l, index);
            ans = mod.plus(ans, extra);

            out.println(ans);
        }
    }

}
