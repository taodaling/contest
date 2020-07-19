package on2020_07.on2020_07_18_.Task0;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long w = in.readInt();
        long x = in.readInt();
        long y = in.readInt();
        int z = in.readInt();
        int m = in.readInt();

        out.println(Math.min(findK(x, w, y, m), z));
    }

    public long findK(long x, long w, long y, long m){
        if (x == 1) {
           return m / GCDs.gcd(m, y);
        }

        m = m * GCDs.gcd(m, x - 1);
        m = m / GCDs.gcd(m, DigitUtils.mod(w * (x - 1) + y, m));
        x %= m;
        y %= m;
        w %= m;
        if (x % 2 == 0) {
            long prod = 1 % m;
            int power = 0;
            while (prod != 0) {
                prod = prod * x % m;
                power++;
            }
            return power + 1;
        }


        long prod = x % m;
        long power = 1;
        while (prod % m != 1) {
            prod = prod * prod % m;
            power *= 2;
        }
        return power;
    }
}
