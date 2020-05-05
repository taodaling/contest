package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class CQualificationRounds {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] cnts = new int[16];
        for (int i = 0; i < n; i++) {
            int bit = 0;
            for (int j = 0; j < k; j++) {
                bit = Bits.setBit(bit, j, in.readInt() == 1);
            }
            cnts[bit]++;
        }

        String yes = "YES";
        String no = "NO";
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if (cnts[i] > 0 && cnts[j] > 0 && (i & j) == 0) {
                    out.println(yes);
                    return;
                }
            }
        }

        out.println(no);
    }
}
