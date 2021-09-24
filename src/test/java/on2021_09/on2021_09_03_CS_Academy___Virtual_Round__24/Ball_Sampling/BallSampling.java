package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Ball_Sampling;



import template.binary.FastBitCount2;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.SumOfFloat;

public class BallSampling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] subsetsum = new int[1 << n];
        for (int i = 1; i < 1 << n; i++) {
            int lb = Integer.lowestOneBit(i);
            if (lb == i) {
                subsetsum[i] = a[Log2.floorLog(i)];
                continue;
            }
            subsetsum[i] = subsetsum[lb] + subsetsum[i - lb];
        }
        int mask = subsetsum.length - 1;
        int set = mask + 1;
        SumOfFloat sum = new SumOfFloat();
        while (set > 0) {
            set = (set - 1) & mask;
            if (set == 0) {
                continue;
            }
            double exp = subsetsum[mask] / (double) subsetsum[set];
            if (FastBitCount2.count(set) % 2 == 0) {
                exp = -exp;
            }
            sum.add(exp);
        }
        out.println(sum.sum());
    }
}
