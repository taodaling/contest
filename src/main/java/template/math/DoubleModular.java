package template.math;

public class DoubleModular implements Modular {
    private int mod;
    private double div;
    private long threshold;

    public DoubleModular(int mod) {
//        assert (long) mod * mod < Integer.MAX_VALUE;
        this.mod = mod;
        this.div = 1d / mod;
        threshold = 10L * mod;
    }

    public int mod(long x) {
        long floor = (long) Math.floor(x * div);
        long ans = x - floor * mod;
//        if (ans >= threshold || ans <= -threshold) {
//            return mod(ans);
//        }
        if (ans < mod) {
            ans += mod;
        }
        if (ans >= mod) {
            ans -= mod;
        }
        return (int) ans;
    }

    public int mul(int a, int b) {
        return mod((long) a * b);
    }


}
