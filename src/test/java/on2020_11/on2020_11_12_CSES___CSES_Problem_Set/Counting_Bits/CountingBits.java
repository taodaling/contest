package on2020_11.on2020_11_12_CSES___CSES_Problem_Set.Counting_Bits;



import template.binary.BitCount;
import template.io.FastInput;
import template.io.FastOutput;

public class CountingBits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long ans = BitCount.countOne(1, n);
        out.println(ans);
    }
}
