package on2019_10.on2019_10_31_daily_training_on_atcoder.Xor_Sum_3;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.LinearBasis;

public class XorSum3 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
        }

        DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
        int[] bitCnts = new int[64];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < 64; j++){
                bitCnts[j] += bo.bitAt(a[i], j);
            }
        }

        LinearBasis lb = new LinearBasis();
        for(int i = 0; i < n; i++){
            long v = a[i];
            for(int j = 0; j < 64; j++){
                if(bitCnts[j] % 2 == 1){
                    v = bo.setBit(v, j, false);
                }
            }
            lb.add(v);
        }

        long maxAdd = lb.theMaximumNumberXor(0);
        long xor = 0;
        for(int i = 0; i < n; i++){
            xor ^= a[i];
        }

        long ans = xor + maxAdd * 2;
        out.println(ans);
    }
}
