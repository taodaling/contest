package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntVersionArray;
import template.JosephusCircle;

public class StrangeNim {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();


        int xor = 0;
        for (int i = 0; i < n; i++) {
            int stones = in.readInt();
            int k = in.readInt();

            int group = stones / k + 1;
            int lastPosition = k - 1;
            int curPosition = DigitUtils.mod(lastPosition - stones % k, group);
            int dieRound = JosephusCircle.dieTime(group, k, curPosition);
            int numberOnIt = group - dieRound;

            xor ^= numberOnIt;
        }

        if (xor == 0) {
            out.println("Aoki");
        } else {
            out.println("Takahashi");
        }
    }

    public int[] sgOf(int n, int k) {
        int[] mex = new int[n + 1];
        IntVersionArray iva = new IntVersionArray(n + 1);
        for (int i = 0; i <= n; i++) {
            iva.clear();
            for (int j = 1; j <= i / k; j++) {
                iva.set(mex[i - j], 1);
            }
            for (int j = 0;; j++) {
                if (iva.get(j) == 0) {
                    mex[i] = j;
                    break;
                }
            }
        }
        return mex;
    }
}
