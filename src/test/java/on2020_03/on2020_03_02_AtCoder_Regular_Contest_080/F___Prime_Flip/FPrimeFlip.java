package on2020_03.on2020_03_02_AtCoder_Regular_Contest_080.F___Prime_Flip;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.MillerRabin;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

public class FPrimeFlip {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n + 1];
        IntegerList list = new IntegerList();
        for (int i = 1; i <= n; i++) {
            x[i] = in.readInt();
        }
    }

    MillerRabin mr = new MillerRabin();

    public int cost(int i, int j) {
        int len = j - i;
        if (len == 0) {
            return 0;
        }
        if (len == 1) {
            return 3;
        }
        if (len == 2) {
            return 2;
        }
        return mr.mr(len, 10) ? 1 : 2;
    }
}
