package on2020_06.on2020_06_18_Codeforces___Codeforces_Global_Round_8.D__AND__OR_and_square_sum;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class DANDORAndSquareSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] cnts = new int[20];
        for (int x : a) {
            for (int j = 0; j < 20; j++) {
                cnts[j] += Bits.get(x, j);
            }
        }

        long sum = 0;
        while (true) {
            long max = 0;
            for (int i = 0; i < 20; i++) {
                if (cnts[i] > 0) {
                    max |= 1 << i;
                }
            }
            if(max == 0){
                break;
            }
            sum += max * max;
            for (int i = 0; i < 20; i++) {
                if (cnts[i] > 0) {
                    cnts[i]--;
                }
            }
        }

        out.println(sum);
    }
}
