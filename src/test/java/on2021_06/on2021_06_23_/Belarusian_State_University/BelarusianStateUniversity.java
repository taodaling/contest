package on2021_06.on2021_06_23_.Belarusian_State_University;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FWTLayers;
import template.polynomial.FastWalshHadamardTransform;

public class BelarusianStateUniversity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        FWTLayers fwt = new FWTLayers();
        int[][][] mats = new int[n][2][2];
        for (int i = 0; i < n; i++) {
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    mats[i][x][y] = in.rc() - '0';
                }
            }
        }
        long[] A = in.rl(1 << n);
        long[] B = in.rl(1 << n);
        int maskXorA = 0;
        int maskXorB = 0;
        int maskAndA = (1 << n) - 1;
        int maskAndB = (1 << n) - 1;
        int maskOrA = 0;
        int maskOrB = 0;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    sum += mats[i][x][y];
                }
            }
            if (sum == 0) {
                //cool all zero
                maskAndA = Bits.clear(maskAndA, i);
                fwt.addAndLayer();
            } else if (sum == 4) {
                maskOrA = Bits.set(maskOrA, i);
                fwt.addOrLayer();
            } else if (sum == 1) {
                //and
                int a = -1;
                int b = -1;
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < 2; y++) {
                        if (mats[i][x][y] == 1) {
                            a = x;
                            b = y;
                        }
                    }
                }
                fwt.addAndLayer();
                maskXorA ^= ((a ^ 1) << i);
                maskXorB ^= ((b ^ 1) << i);
            } else if (sum == 2) {
                //xor
                if (mats[i][0][0] == mats[i][1][1]) {
                    if (mats[i][0][0] == 1) {
                        maskXorA ^= 1 << i;
                    }
                    fwt.addXorLayer();
                }else if(mats[i][0][0] + mats[i][0][1] == 2){
                    maskXorA = Bits.set(maskXorA, i);
                    maskAndB = Bits.clear(maskAndB, i);
                    fwt.addOrLayer();
                }else if(mats[i][1][0] + mats[i][1][1] == 2){
                    maskAndB = Bits.clear(maskAndB, i);
                    fwt.addOrLayer();
                }else if(mats[i][0][0] + mats[i][1][0] == 2){
                    maskXorB ^= 1 << i;
                    maskAndA = Bits.clear(maskAndA, i);
                    fwt.addOrLayer();
                }else {
                    maskAndA = Bits.clear(maskAndA, i);
                    fwt.addOrLayer();
                }
            } else {
                //or
                int a = -1;
                int b = -1;
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < 2; y++) {
                        if (mats[i][x][y] == 0) {
                            a = x;
                            b = y;
                        }
                    }
                }
                fwt.addOrLayer();
                maskXorA ^= (a << i);
                maskXorB ^= (b << i);
            }
        }
        long[] mA = migrate(A, maskXorA, maskOrA, maskAndA);
        long[] mB = migrate(B, maskXorB, maskOrB, maskAndB);
        fwt.apply(mA, 0, mA.length - 1);
        fwt.apply(mB, 0, mB.length - 1);
        long[] mC = new long[1 << n];
        FastWalshHadamardTransform.dotMul(mA, mB, mC, 0, mA.length - 1);
        fwt.inverse(mC, 0, mC.length - 1);
        for (long x : mC) {
            out.append(x).append(' ');
        }
    }

    public long[] migrate(long[] A, int xor, int or, int and) {
        long[] ans = new long[A.length];
        for (int i = 0; i < A.length; i++) {
            ans[((i ^ xor) | or) & and] += A[i];
        }
        return ans;
    }
}
