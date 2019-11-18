package on2019_11.on2019_11_18_AtCoder_Regular_Contest_067.F___Yakiniku_Restaurants;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.ModMatrix;
import template.NumberTheory;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long L = in.readLong();
        long a = in.readLong();
        long b = in.readLong();

        NumberTheory.Modular mod = new NumberTheory.Modular(in.readInt());
        int maxDigit = base10.ceilLog(a + b * (L - 1));
        int minDigit = base10.ceilLog(a);
        int ans = 0;
        for (int i = minDigit; i <= maxDigit; i++) {
            long l = getL(i, a, b, L);
            long r = getR(i, a, b, L);
            ModMatrix mat = new ModMatrix(new int[][] {{mod.valueOf(base10.setBit(0, i, 1)), 1, 0},
                            {0, 1, mod.valueOf(b)}, {0, 0, 1}});
            ModMatrix vec = new ModMatrix(3, 1);
            vec.set(0, 0, ans);
            vec.set(1, 0, mod.valueOf(a + b * l));
            vec.set(2, 0, 1);
            ModMatrix transform = ModMatrix.pow(mat, r - l + 1, mod);
            ModMatrix after = ModMatrix.mul(transform, vec, mod);
            ans = after.get(0, 0);
        }

        out.println(ans);
    }

    DigitUtils.DigitBase base10 = new DigitUtils.DigitBase(10);

    public long getL(int digit, long a, long b, long L) {
        long l = 0;
        long r = L - 1;
        while (l < r) {
            long m = (l + r) >>> 1;
            long val = a + b * m;
            if (base10.ceilLog(val) >= digit) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    public long getR(int digit, long a, long b, long L) {
        long l = 0;
        long r = L - 1;
        while (l < r) {
            long m = (l + r + 1) >>> 1;
            long val = a + b * m;
            if (base10.ceilLog(val) <= digit) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return l;
    }
}
